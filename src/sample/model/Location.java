package sample.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A location describes a specific room.
 */
@XmlRootElement(name = "Location")
public class Location {
    public String getName() {
        return name;
    }

    public Location() {}
    public Location(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "name")
    @XmlID
    String name;
}
