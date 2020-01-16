package sample.model;

import javax.xml.bind.annotation.*;

/**
 * Describes known information about an author.
 */

@XmlRootElement(name = "Author")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Author {
    @XmlAttribute(name = "name")
    @XmlID
    private String name;

    public String getName() {
        return name;
    }

    public Author() {}
    public Author(String name) {
        this.name = name;
    }
}
