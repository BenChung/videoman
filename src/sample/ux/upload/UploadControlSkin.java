package sample.ux.upload;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import sample.model.Lecture;

public class UploadControlSkin extends SkinBase<UploadControl> {
    public UploadControlSkin(UploadControl uploadControl) {
        super(uploadControl);
        BorderPane mainPane = new BorderPane();

        ButtonBar actions = new ButtonBar();
        actions.getButtons().add(uploadControl.metadataButton);
        actions.getButtons().add(uploadControl.uploadButton);

        mainPane.setBottom(actions);
        mainPane.setCenter(uploadControl.videosToUpload);
        getChildren().add(mainPane);
    }
}
