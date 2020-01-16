package sample.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import sample.model.importmodel.Batch;
import sample.model.importmodel.ImportRecording;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * An Event describes an entire conference, and includes information about locations, lectures, and authors.
 */
public class Event {
    public final EventPreferences prefs = new EventPreferences();

    public ObservableList<Author> getAuthors() {
        return authors;
    }

    public ObservableList<Lecture> getLectures() {
        return lectures;
    }

    public ObservableList<Recording> getRecordings() {
        return recordings;
    }

    public ObservableList<Location> getLocations() {
        return locations;
    }

    public ObservableList<Device> getRecDevs() {
        return recDevs;
    }

    public DualHashBidiMap<Lecture, Recording> getMapping() { return mapping; }

    private ObservableList<Author> authors = FXCollections.observableArrayList();
    private ObservableList<Lecture> lectures = FXCollections.observableArrayList();
    private ObservableList<Recording> recordings = FXCollections.observableArrayList();
    private ObservableList<Location> locations = FXCollections.observableArrayList();
    private ObservableList<Device> recDevs = FXCollections.observableArrayList();
    private DualHashBidiMap<Lecture, Recording> mapping = new DualHashBidiMap<>();

    public Event(List<Lecture> lectures, List<Recording> recordings, List<Location> locations, List<Author> authors, List<Device> recDevs) {
        this.lectures.setAll(lectures);
        this.authors.setAll(authors);
        this.recordings.setAll(recordings);
        this.locations.setAll(locations);
        this.recDevs.setAll(recDevs);
    }

    public void importBatch(Batch currentBatch) {
        HashMap<String, Device> deviceMap = new HashMap<>();
        for (Device dev : getRecDevs()) {
            deviceMap.put(dev.name, dev);
        }

        for (ImportRecording newrec : currentBatch.getRecordings()) {
            Device dev = null;
            if (deviceMap.containsKey(newrec.getDevice()))
                dev = deviceMap.get(newrec.getDevice());
            else {
                dev = new Device(newrec.getDevice());
                deviceMap.put(newrec.getDevice(), dev);
                recDevs.add(dev);
            }

            recordings.add(new Recording(newrec.getStart(), newrec.getEnd(), newrec.getFile(), newrec.getGuid(), dev));
        }
    }

    public static class EventPreferences {
        public Path storageDirectory = Path.of(".");
        public Path descriptionTemplate = null;
        public Path emailTemplate = null;
    }
}
