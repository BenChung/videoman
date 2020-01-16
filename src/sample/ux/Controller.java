package sample.ux;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {
    public EventPane events;
    public MenuItem evtprefs;
    public MenuItem vidimp;
    private ObjectProperty<Event> loadedEvent = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        events.getEvent().bind(loadedEvent);
        evtprefs.disableProperty().bind(EasyBind.map(loadedEvent, Objects::isNull));
        vidimp.disableProperty().bind(EasyBind.map(loadedEvent, Objects::isNull));
    }

    public void newEvent(ActionEvent actionEvent) {
        // flow: open file dialog for ics file
        //       -> import ics
        //       -> generate Event model
        //       -> load Event as model

        try {
            HashMap<String, Location> rooms = new HashMap<>();
            ArrayList<Lecture> lectures = new ArrayList<>();
            HashMap<String, Author> authorMap = new HashMap<String, Author>();

            String descPattern = "\\[(.*)\\] (.*) - (.*)";
            Pattern regex = Pattern.compile(descPattern);


            FileInputStream fis = new FileInputStream("event-calendar.ics");
            CalendarBuilder builder = new CalendarBuilder();
            Calendar cal = builder.build(fis);
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
                            .map(name -> authorMap.computeIfAbsent(authorNames[0], Author::new))
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
            loadedEvent.set(new Event(lectures, recordings, locations, authors, devices));
            fis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    private DirectoryChooser directoryChooser = new DirectoryChooser();
    public void importDirectory(ActionEvent actionEvent) {
        //show directory browser -> check for valid folder -> import & validate manifest
        //                       -> copy videos to storage directory -> add to current Event

        File recDir = directoryChooser.showDialog(((MenuItem)actionEvent.getSource()).getStyleableNode().getScene().getWindow());
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
        loadedEvent.get().importBatch(currentBatch);

    }

    public void openPrefs(ActionEvent actionEvent) {
        if (loadedEvent.get() == null)
            return;

        Stage stage = new Stage();
        stage.setScene(new Scene(new PreferencesControl(loadedEvent.get())));
        stage.setTitle("Preferences");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((MenuItem)actionEvent.getSource()).getStyleableNode().getScene().getWindow());
        stage.show();
    }
}
