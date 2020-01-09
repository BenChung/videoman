package sample.model;

/**
 * A location describes a specific room.
 */
public class Location {
    public String getName() {
        return name;
    }

    public Location(String name) {
        this.name = name;
    }

    String name;
}
