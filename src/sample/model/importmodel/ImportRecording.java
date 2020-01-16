package sample.model.importmodel;

import sample.model.Device;

import java.nio.file.Path;
import java.time.Instant;

public class ImportRecording {
    public ImportRecording(Instant start, Instant end, Path file, String guid, String device) {
        this.start = start;
        this.end = end;
        this.file = file;
        this.guid = guid;
        this.device = device;
    }

    private Instant start, end;
    private Path file;
    private String guid;
    private String device;

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public Path getFile() {
        return file;
    }

    public String getDevice() {
        return device;
    }

    public String getGuid() { return guid; }
}
