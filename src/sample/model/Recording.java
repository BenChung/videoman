package sample.model;

import java.nio.file.Path;
import java.time.Instant;

public class Recording implements Bindable {
    public Recording(Instant start, Instant end, Path file, Device device) {
        this.start = start;
        this.end = end;
        this.file = file;
        this.device = device;
    }

    private Instant start, end;
    private Path file;
    private Device device;
    private Lecture lecture;

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public Path getFile() {
        return file;
    }

    public Device getDevice() {
        return device;
    }

    public Lecture getLecture() { return lecture; }

    public void setLecture(Lecture lecture) { this.lecture = lecture; }

    @Override
    public void setBinding(Object other) {
        if (!(other instanceof Lecture))
            throw new RuntimeException("Tried to bind a recording to a non-lecture");
        this.lecture = (Lecture) other;

    }
}
