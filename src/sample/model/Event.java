package sample.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * An Event describes an entire conference, and includes information about locations, lectures, and authors.
 */
public class Event {
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

    private ObservableList<Author> authors = FXCollections.observableArrayList();
    private ObservableList<Lecture> lectures = FXCollections.observableArrayList();
    private ObservableList<Recording> recordings = FXCollections.observableArrayList();
    private ObservableList<Location> locations = FXCollections.observableArrayList();
    private ObservableList<Device> recDevs = FXCollections.observableArrayList();

    public Event(List<Lecture> lectures, List<Recording> recordings, List<Location> locations, List<Author> authors, List<Device> recDevs) {
        this.lectures.setAll(lectures);
        this.authors.setAll(authors);
        this.recordings.setAll(recordings);
        this.locations.setAll(locations);
        this.recDevs.setAll(recDevs);
    }
}
