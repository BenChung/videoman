package sample.ux.upload;

import javafx.scene.control.*;
import sample.model.Event;
import sample.model.Lecture;

public class UploadControl extends Control {
    TableView<Lecture> videosToUpload = new TableView<>();
    Button uploadButton = new Button("Upload");
    Button metadataButton = new Button("Set Metadata");

    private Event currentEvent;
    public UploadControl(Event eventToUpload) {
        this.currentEvent = eventToUpload;

        TableColumn<Lecture, String> title = new TableColumn<>();
        title.setText("Title");
        videosToUpload.getColumns().add(title);
        videosToUpload.setRowFactory(tvl -> {
            return new TableRow<>();
        });
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new UploadControlSkin(this);
    }
}
