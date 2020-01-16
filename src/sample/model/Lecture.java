package sample.model;

import sample.model.binding.BidiMapAdapter;
import sample.model.binding.InstantAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Lecture")
@XmlAccessorType(XmlAccessType.PROPERTY)
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

    @XmlAttribute(name = "title")
    private String title;

    @XmlElement(name = "Author")
    @XmlIDREF
    private List<Author> authors;

    @XmlElement(name = "Location")
    @XmlIDREF
    private Location location;

    @XmlAttribute(name = "start")
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant start;

    @XmlAttribute(name = "end")
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant end;

    public Lecture() { }

    public Lecture(String title, List<Author> authors, Location location, Instant start, Instant end) {
        this.title = title;
        this.authors = authors;
        this.location = location;
        this.start = start;
        this.end = end;
    }
}
