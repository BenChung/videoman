package sample.ux.prefs;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class PreferencesControlSkin extends SkinBase<PreferencesControl> {
    public PreferencesControlSkin(PreferencesControl preferencesControl) {
        super(preferencesControl);
        GridPane baseLayout = new GridPane();


        RowConstraints row0 = new RowConstraints();
        row0.setVgrow(Priority.NEVER);
        row0.setFillHeight(true);
        row0.setPrefHeight(Region.USE_COMPUTED_SIZE);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.NEVER);
        row1.setFillHeight(true);
        row1.setPrefHeight(Region.USE_COMPUTED_SIZE);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.NEVER);
        row2.setFillHeight(true);
        row2.setPrefHeight(Region.USE_COMPUTED_SIZE);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.NEVER);
        col0.setFillWidth(true);
        col0.setPrefWidth(Region.USE_COMPUTED_SIZE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setFillWidth(true);
        col1.setPrefWidth(Region.USE_COMPUTED_SIZE);

        baseLayout.getRowConstraints().setAll(row0, row1, row2);
        baseLayout.getColumnConstraints().setAll(col0, col1);

        baseLayout.add(new Text("Storage Directory:"), 0, 0);
        baseLayout.add(new Text("Description Template:"), 0, 1);
        baseLayout.add(new Text("Email Template:"), 0, 2);
        baseLayout.add(preferencesControl.storDirPicker, 1, 0);
        baseLayout.add(preferencesControl.descTemplate, 1, 1);
        baseLayout.add(preferencesControl.emailTemplate, 1, 2);

        getChildren().add(baseLayout);
    }
}
