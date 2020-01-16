package sample.ux.prefs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

public class FilePickerControl extends Control {
    public Path getSelectedFile() {
        return selectedFile.get();
    }

    public ObjectProperty<Path> selectedFileProperty() {
        return selectedFile;
    }

    private ObjectProperty<Path> selectedFile = new SimpleObjectProperty<>();

    public Button getOpenPicker() {
        return openPicker;
    }

    public TextField getTextPath() {
        return textPath;
    }

    private Button openPicker = new Button("Select");
    private TextField textPath = new TextField();

    private FileChooser fileChooser = new FileChooser();
    private DirectoryChooser directoryChooser = new DirectoryChooser();

    public FilePickerControl(Path defaultPath, FileOrDirectory mode) {
        selectedFile.set(defaultPath);

        textPath.textProperty().bindBidirectional(selectedFile, new PathStringConverter());

        openPicker.setOnAction(evt -> {
            File newFile = null;
            if (mode == FileOrDirectory.FILE)
                newFile = fileChooser.showOpenDialog(((Node) evt.getSource()).getScene().getWindow());
            else
                newFile = directoryChooser.showDialog(((Node) evt.getSource()).getScene().getWindow());
            if (newFile != null)
                selectedFile.set(newFile.toPath());
            else
                selectedFile.set(null);
        });
    }
    @Override
    protected Skin<?> createDefaultSkin() {
        return new FileChooserSkin(this);
    }

    public enum  FileOrDirectory {
        FILE, DIRECTORY
    }

    private class PathStringConverter extends StringConverter<Path> {
        @Override
        public String toString(Path path) {
            if (path != null)
                return path.toString();
            else
                return "";
        }

        @Override
        public Path fromString(String s) {
            try {
                var fill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
                textPath.backgroundProperty().get().getFills().clear();
                textPath.backgroundProperty().get().getFills().add(fill);
                return Path.of(s);
            } catch (InvalidPathException e) {
                var fill = new BackgroundFill(Color.PALEVIOLETRED, CornerRadii.EMPTY, Insets.EMPTY);
                textPath.backgroundProperty().get().getFills().clear();
                textPath.backgroundProperty().get().getFills().add(fill);
                return null;
            }
        }
    }
}
