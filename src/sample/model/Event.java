package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.reactfx.util.Tuple2;
import sample.model.binding.BidiMapAdapter;
import sample.model.binding.NioPathAdaptor;
import sample.model.binding.ObservableListAdapter;
import sample.model.importmodel.Batch;
import sample.model.importmodel.ImportRecording;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * An Event describes an entire conference, and includes information about locations, lectures, and authors.
 */
@XmlRootElement(name="Event")
public class Event {
    @XmlElement(name = "Preferences")
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

    @XmlElement(name="Authors")
    @XmlJavaTypeAdapter(ObservableListAdapter.class)
    private ObservableList<Author> authors = FXCollections.observableArrayList();

    @XmlElement(name="Lectures")
    @XmlJavaTypeAdapter(ObservableListAdapter.class)
    private ObservableList<Lecture> lectures = FXCollections.observableArrayList();

    @XmlElement(name="Recordings")
    @XmlJavaTypeAdapter(ObservableListAdapter.class)
    private ObservableList<Recording> recordings = FXCollections.observableArrayList();

    @XmlElement(name="Locations")
    @XmlJavaTypeAdapter(ObservableListAdapter.class)
    private ObservableList<Location> locations = FXCollections.observableArrayList();

    @XmlElement(name="Devices")
    @XmlJavaTypeAdapter(ObservableListAdapter.class)
    private ObservableList<Device> recDevs = FXCollections.observableArrayList();

    @XmlElement(name="Mapping")
    @XmlJavaTypeAdapter(BidiMapAdapter.class)
    private DualHashBidiMap<Lecture, Recording> mapping = new DualHashBidiMap<>();


    public Event() { }

    public Event(List<Lecture> lectures, List<Recording> recordings, List<Location> locations, List<Author> authors, List<Device> recDevs) {
        this.lectures.setAll(lectures);
        this.authors.setAll(authors);
        this.recordings.setAll(recordings);
        this.locations.setAll(locations);
        this.recDevs.setAll(recDevs);
    }

    public Task<Void> importBatch(Batch currentBatch, Path saveDir) {
        HashMap<String, Device> deviceMap = new HashMap<>();
        for (Device dev : getRecDevs()) {
            deviceMap.put(dev.name, dev);
        }

        HashSet<String> recGuids = new HashSet<>();
        recordings.stream().map(Recording::getGuid).forEach(recGuids::add);

        var importTask =  new BatchImportTask(currentBatch, deviceMap, saveDir);
        importTask.setOnSucceeded(e -> {
            recDevs.addAll(importTask.newDevs);
            recordings.addAll(importTask.newRecs);
        });
        return importTask;
    }

    @XmlRootElement(name = "Preferences")
    public static class EventPreferences {
        @XmlJavaTypeAdapter(NioPathAdaptor.class)
        @XmlElement(name="StorageDirectory", type = Object.class)
        public Path storageDirectory = Path.of(".");

        @XmlJavaTypeAdapter(NioPathAdaptor.class)
        @XmlElement(name="DescriptionTemplate", type = Object.class)
        public Path descriptionTemplate = null;

        @XmlJavaTypeAdapter(NioPathAdaptor.class)
        @XmlElement(name="EmailTemplate", type = Object.class)
        public Path emailTemplate = null;
    }

    private static class BatchImportTask extends Task<Void> {
        private final Batch currentBatch;
        private final HashMap<String, Device> deviceMap;
        private final Path saveDir;

        public final List<Device> newDevs = new LinkedList<>();
        public final LinkedList<Recording> newRecs = new LinkedList<>();

        public BatchImportTask(Batch currentBatch, HashMap<String, Device> deviceMap, Path saveDir) {
            this.currentBatch = currentBatch;
            this.deviceMap = deviceMap;
            this.saveDir = saveDir;
        }

        @Override
        protected Void call() throws Exception {
            int nrecs = currentBatch.getRecordings().size();
            int crec = 0;
            for (ImportRecording newrec : currentBatch.getRecordings()) {
                updateProgress(crec, nrecs);
                Device dev = null;
                if (deviceMap.containsKey(newrec.getDevice()))
                    dev = deviceMap.get(newrec.getDevice());
                else {
                    dev = new Device(newrec.getDevice());
                    deviceMap.put(newrec.getDevice(), dev);
                    newDevs.add(dev);
                }

                Path target = saveDir.resolve(newrec.getFile().getFileName());
                try {
                    Files.copy(newrec.getFile(), target);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                newRecs.add(new Recording(newrec.getStart(), newrec.getEnd(), newrec.getFile(), newrec.getGuid(), dev));
            }
            return null;
        }
    }
}
