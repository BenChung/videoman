package sample.model;

import java.time.Instant;
import java.util.List;

public class Lecture {
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

    public Lecture(String title, List<Author> authors, Location location, Instant start, Instant end) {
        this.title = title;
        this.authors = authors;
        this.location = location;
        this.start = start;
        this.end = end;
    }
}
