package sample.model.importmodel;

import java.io.*;
import java.time.Instant;
import java.util.List;

/**
 * Describes a batch of data to import
 * The result of parsing one (or more) descriptor CSV files from the recording devices
 */
public class Import {
    public List<Batch> getBatches() {
        return batches;
    }
    List<Batch> batches;
}
