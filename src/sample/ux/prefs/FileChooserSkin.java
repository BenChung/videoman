package sample.ux.prefs;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;

public class FileChooserSkin extends SkinBase<FilePickerControl> {
    public FileChooserSkin(FilePickerControl fileChooser) {
        super(fileChooser);
        HBox layout = new HBox(5);
        layout.getChildren().add(fileChooser.getOpenPicker());
        layout.getChildren().add(fileChooser.getTextPath());
        getChildren().add(layout);
    }
}
