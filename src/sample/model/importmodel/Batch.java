package sample.model.importmodel;

import sample.model.Recording;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A single batch of videos to import. The result of parsing a descriptor CSV file.
 */
public class Batch {
    public Batch(List<ImportRecording> recordings) {
        this.recordings = recordings;
    }

    public List<ImportRecording> getRecordings() {
        return recordings;
    }

    List<ImportRecording> recordings;


    public static Batch fromManifest(File inp) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(inp));
        String row = "";
        int nrow = 0;
        List<ImportRecording> recordings = new ArrayList<>();
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            if (nrow++ == 0) continue; // skip the header


            Path videoFile = Path.of(inp.getParentFile().getPath(), data[0]);
            String guid = data[1];
            Instant start = Instant.ofEpochMilli(Long.parseLong(data[2]));
            Instant end = Instant.ofEpochMilli(Long.parseLong(data[3]));
            String machine = data[4];
            if (!Files.exists(videoFile)) {
                System.err.println("Missing referenced video file " + videoFile);
            }
            recordings.add(new ImportRecording(start, end, videoFile, guid, machine));
        }
        csvReader.close();
        return new Batch(recordings);
    }
}
