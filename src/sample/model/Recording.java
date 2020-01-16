package sample.model;

import sample.model.binding.InstantAdapter;
import sample.model.binding.NioPathAdaptor;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.nio.file.Path;
import java.time.Instant;
@XmlRootElement(name = "Recording")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Recording {
    public Recording() {}
    public Recording(Instant start, Instant end, Path file, String guid, Device device) {
        this.start = start;
        this.end = end;
        this.file = file;
        this.guid = guid;
        this.device = device;
    }

    @XmlAttribute(name = "start")
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant start;

    @XmlAttribute(name = "start")
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant end;

    @XmlJavaTypeAdapter(NioPathAdaptor.class)
    @XmlElement(name="File", type = Object.class)
    private Path file;

    @XmlAttribute(name = "guid")
    private String guid;

    @XmlElement(name = "Device")
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
