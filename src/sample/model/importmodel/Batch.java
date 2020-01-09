package sample.model.importmodel;

import sample.model.Recording;

import java.util.List;

/**
 * A single batch of videos to import. The result of parsing a descriptor CSV file.
 */
public class Batch {
    public String getDeviceName() {
        return deviceName;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    String deviceName;
    List<Recording> recordings;
}
