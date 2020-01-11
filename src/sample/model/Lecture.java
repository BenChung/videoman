package sample.model;

import java.time.Instant;
import java.util.List;

public class Lecture implements Bindable {
    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Location getLocation() {
        return location;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    private String title;
    private List<Author> authors;
    private Location location;
    private Instant start, end;

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    private Recording recording;

    public Lecture(String title, List<Author> authors, Location location, Instant start, Instant end) {
        this.title = title;
        this.authors = authors;
        this.location = location;
        this.start = start;
        this.end = end;
    }

    @Override
    public void setBinding(Bindable other) {
        if (!(other instanceof Recording) && (other != null))
            throw new RuntimeException("Tried to bind a lecture to a non-recording");
        this.recording = (Recording)other;
    }

    @Override
    public Bindable getBound() {
        return this.recording;
    }
}
