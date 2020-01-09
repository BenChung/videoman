package sample.ux;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import sample.model.*;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Controller {
    public ComboBox<Location> roomSelect;
    public EventPane events;
    public ComboBox<Device> deviceSelect;
    private ObjectProperty<Event> loadedEvent = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        roomSelect.setConverter(new StringConverter<Location>() {
            @Override
            public String toString(Location location) {
                if (location == null)
                    return "";
                return location.getName();
            }

            @Override
            public Location fromString(String s) {
                return null;
            }
        });

        deviceSelect.setConverter(new StringConverter<Device>() {
            @Override
            public String toString(Device device) {
                if (device == null)
                    return "";
                return device.getName();
            }

            @Override
            public Device fromString(String s) {
                return null;
            }
        });

        events.getEvent().bind(loadedEvent);

        loadedEvent.addListener((bnd, ov, nv) -> {
            Bindings.bindContent(roomSelect.getItems(), nv.getLocations());
            roomSelect.setValue(null);

            Bindings.bindContent(deviceSelect.getItems(), nv.getRecDevs());
            deviceSelect.setValue(null);
        });

        roomSelect.valueProperty().addListener((bnd, ov, nv) -> {
            if (nv == null)
                return;
            var locs = loadedEvent.get().getLectures().stream().filter(x -> x.getLocation() == nv)
                    .collect(Collectors.toList());
            events.showEvents(locs);
        });


        deviceSelect.valueProperty().addListener((bnd, ov, nv) -> {
            if (nv == null)
                return;
            var locs = loadedEvent.get().getRecordings().stream().filter(x -> x.getDevice() == nv)
                    .collect(Collectors.toList());
            events.showRecordings(locs);
        });
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

            HashMap<String, Device> fakeDeviceMap = new HashMap<>();
            locations.forEach(l -> fakeDeviceMap.put(l.getName(), new Device(l.getName())));

            List<Recording> recordings = lectures.stream()
                    .map(l -> new Recording(l.getStart(), l.getEnd(), Path.of(""), fakeDeviceMap.get(l.getLocation().getName())))
                    .collect(Collectors.toList());

            List<Device> devices = new ArrayList<>(fakeDeviceMap.values());
            loadedEvent.set(new Event(lectures, recordings, locations, authors, devices));
            fis.close();
        } catch (Exception e) {

        }
    }
}
