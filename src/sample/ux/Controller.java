package sample.ux;

import com.sun.prism.impl.Disposer;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import org.fxmisc.easybind.EasyBind;
import sample.model.*;
import sample.model.importmodel.Batch;
import sample.ux.prefs.PreferencesControl;
import sample.ux.progress.ProgressForm;

import javax.xml.bind.*;
import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {
    public EventPane events;
    public MenuItem evtprefs;
    public MenuItem vidimp;
    public MenuItem saveMenu;
    public MenuItem loadMenu;
    public MenuItem saveAsMenu;

    private ObjectProperty<Path> currentFile = new SimpleObjectProperty();
    private ObjectProperty<Event> loadedEvent = new SimpleObjectProperty<>();
    private StringProperty titleProperty;

    private FileChooser fileDialog = new FileChooser();

    @FXML
    public void initialize() {
        events.getEvent().bind(loadedEvent);
        evtprefs.disableProperty().bind(EasyBind.map(loadedEvent, Objects::isNull));
        vidimp.disableProperty().bind(EasyBind.map(loadedEvent, Objects::isNull));
        saveMenu.disableProperty().bind(EasyBind.map(currentFile, Objects::isNull));
        saveAsMenu.disableProperty().bind(EasyBind.map(loadedEvent, Objects::isNull));

        fileDialog.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Event descriptors", "*.evt"),
                new FileChooser.ExtensionFilter("All", "*"));
    }

    public void newEvent(ActionEvent actionEvent) {
        // flow: open file dialog for ics file
        //       -> import ics
        //       -> generate Event model
        //       -> load Event as model

        Task<Event> makeEvent = new Task<Event>() {
            @Override
            protected Event call() throws Exception {
                try {
                    HashMap<String, Location> rooms = new HashMap<>();
                    ArrayList<Lecture> lectures = new ArrayList<>();
                    HashMap<String, Author> authorMap = new HashMap<String, Author>();
                    updateProgress(0, 100);

                    String descPattern = "\\[(.*)\\] (.*) - (.*)";
                    Pattern regex = Pattern.compile(descPattern);
                    FileInputStream fis = new FileInputStream("event-calendar.ics");
                    CalendarBuilder builder = new CalendarBuilder();
                    Calendar cal = builder.build(fis);
                    updateProgress(50, 100);
                    for (Component c : cal.getComponents()) {
                        if (c instanceof VEvent) {
                            VEvent vev = (VEvent) c;
                            // get the location of the event
                            Location loc = rooms.computeIfAbsent(vev.getLocation().getValue(), Location::new);

                            // parse the summary string
                            String summary = vev.getSummary().getValue();
                            Matcher result = regex.matcher(summary);
                            if (!result.matches()) {
                                //System.err.println("Unable to match " + summary);
                                continue;
                            }
                            // title extraction
                            String title = result.group(2);

                            // author extraction
                            String authorsCommas = result.group(3);
                            String[] authorNames = authorsCommas.split(",");
                            List<Author> authors = Arrays.stream(authorNames)
                                    .map(name -> authorMap.computeIfAbsent(name.trim(), Author::new))
                                    .collect(Collectors.toList());

                            lectures.add(new Lecture(title, authors, loc,
                                    vev.getStartDate().getDate().toInstant(),
                                    vev.getEndDate().getDate().toInstant()));
                        }
                    }
                    List<Location> locations = new ArrayList<>(rooms.values());
                    List<Author> authors = new ArrayList<>(authorMap.values());

                    List<Recording> recordings = new ArrayList<>();
                    List<Device> devices = new ArrayList<>();
                    fis.close();
                    return new Event(lectures, recordings, locations, authors, devices);
                } catch (Exception e) {
                    throw new RuntimeException(e);

                }
            }
        };
        ProgressForm loadProgress = new ProgressForm("Importing");
        loadProgress.activateProgressBar(makeEvent);

        loadProgress.getDialogStage().show();
        makeEvent.setOnSucceeded(e -> {
            loadedEvent.set((Event)e.getSource().getValue());
            loadProgress.getDialogStage().close();
        });
        new Thread(makeEvent).start();
    }

    private DirectoryChooser directoryChooser = new DirectoryChooser();

    public void importDirectory(ActionEvent actionEvent) {
        //show directory browser -> check for valid folder -> import & validate manifest
        //                       -> copy videos to storage directory -> add to current Event

        File recDir = directoryChooser.showDialog(((MenuItem) actionEvent.getSource()).getStyleableNode().getScene().getWindow());
        if (recDir == null) return;
        if (!recDir.isDirectory()) return; // todo better error message

        // find the manifest
        File[] files = recDir.listFiles();
        if (files == null) return; // todo better error message

        List<File> manifests = Arrays.stream(files).filter(f -> f.getName().equals("manifest.csv")).collect(Collectors.toList());
        if (manifests.size() != 1) return; // todo better error message
        File manifest = manifests.get(0);

        Batch currentBatch = null;
        try {
            currentBatch = Batch.fromManifest(manifest);
        } catch (IOException e) {
            throw new RuntimeException(e); // todo for now
        }

        Task<Void> loadTask = loadedEvent.get().importBatch(currentBatch, loadedEvent.get().prefs.storageDirectory);

        ProgressForm loadProgress = new ProgressForm("Importing");
        loadProgress.activateProgressBar(loadTask);
        loadProgress.getDialogStage().show();
        var oh = loadTask.getOnSucceeded();
        loadTask.setOnSucceeded(e -> {
            loadProgress.getDialogStage().close();
            oh.handle(e);
        });
        new Thread(loadTask).start();
    }

    public void openPrefs(ActionEvent actionEvent) {
        if (loadedEvent.get() == null)
            return;

        Stage stage = new Stage();
        stage.setScene(new Scene(new PreferencesControl(loadedEvent.get())));
        stage.setTitle("Preferences");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((MenuItem) actionEvent.getSource()).getStyleableNode().getScene().getWindow());
        stage.setResizable(false);
        stage.show();
    }

    PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.evt");
    public void saveAsEvent(ActionEvent actionEvent) {
        File saveTo = fileDialog.showSaveDialog(((MenuItem) actionEvent.getSource()).getStyleableNode().getScene().getWindow());

        if (saveTo == null)
            return;
        Path path = saveTo.toPath();
        if (!matcher.matches(path)) {
            // wrong extension, put the right one on
            path = path.resolveSibling(path.getFileName() + ".evt");
        }

        currentFile.set(path);
        saveEvent(actionEvent);
    }

    public void saveEvent(ActionEvent actionEvent) {
        try {
            JAXBContext context = JAXBContext.newInstance(Event.class, Author.class, Device.class, Lecture.class, Location.class, Recording.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            m.marshal(loadedEvent.get(), currentFile.get().toFile());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadEvent(ActionEvent actionEvent) {
        File openFrom = fileDialog.showOpenDialog(((MenuItem) actionEvent.getSource()).getStyleableNode().getScene().getWindow());
        if (openFrom == null)
            return;
        try {
            JAXBContext context = JAXBContext.newInstance(Event.class, Author.class, Device.class, Lecture.class, Location.class, Recording.class);
            Unmarshaller um = context.createUnmarshaller();
            Event res = (Event) um.unmarshal(openFrom);
            if (res == null)
                return;
            loadedEvent.set(res);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTitleProperty(StringProperty titleProperty) {
        this.titleProperty = titleProperty;
    }
}
