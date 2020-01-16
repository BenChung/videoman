package sample.model;

import java.nio.file.Path;
import java.time.Instant;

public class Recording {
    public Recording(Instant start, Instant end, Path file, String guid, Device device) {
        this.start = start;
        this.end = end;
        this.file = file;
        this.guid = guid;
        this.device = device;
    }

    private Instant start, end;
    private Path file;
    private String guid;
    private Device device;

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

    public String getGuid() { return guid; }
}
