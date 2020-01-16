package sample.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Device")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Device {
    public Device() {}
    public Device(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute(name = "name")
    String name;
}
