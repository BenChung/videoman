package sample.ux.prefs;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import sample.model.Event;

import java.nio.file.Path;

public class PreferencesControl extends Control {
    FilePickerControl storDirPicker = new FilePickerControl(Path.of("."), FilePickerControl.FileOrDirectory.DIRECTORY);
    FilePickerControl descTemplate = new FilePickerControl(Path.of("."), FilePickerControl.FileOrDirectory.FILE);
    FilePickerControl emailTemplate = new FilePickerControl(Path.of("."), FilePickerControl.FileOrDirectory.FILE);

    public PreferencesControl(Event currentEvent) {
        storDirPicker.selectedFileProperty().set(currentEvent.prefs.storageDirectory);
        descTemplate.selectedFileProperty().set(currentEvent.prefs.descriptionTemplate);
        emailTemplate.selectedFileProperty().set(currentEvent.prefs.emailTemplate);

        storDirPicker.selectedFileProperty().addListener((obs, ov, nv) -> currentEvent.prefs.storageDirectory = nv);
        descTemplate.selectedFileProperty().addListener((obs, ov, nv) -> currentEvent.prefs.descriptionTemplate = nv);
        emailTemplate.selectedFileProperty().addListener((obs, ov, nv) -> currentEvent.prefs.emailTemplate = nv);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PreferencesControlSkin(this);
    }
}
