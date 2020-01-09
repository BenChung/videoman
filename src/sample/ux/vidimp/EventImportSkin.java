package sample.ux.vidimp;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import sample.model.Event;
import sample.model.Location;
import sample.model.importmodel.Batch;

public class EventImportSkin extends SkinBase<EventImport> {
    public EventImportSkin(EventImport eventImport) {
        super(eventImport);
        VBox layout = new VBox(2);

        ListView<Batch> batchListView = new ListView<>();
        batchListView.setItems(FXCollections.observableArrayList(eventImport.getImp().getBatches()));
        batchListView.setCellFactory(batch -> new ImportCell(eventImport.getEvent()));

        getChildren().add(layout);
    }

    static class ImportCell extends ListCell<Batch> {
        private final Event into;

        public ImportCell(Event into) {
            this.into = into;
        }

        @Override
        protected void updateItem(Batch batch, boolean b) {
            super.updateItem(batch, b);
            if (batch == null)
                return;
            // name textarea
            Text machineName = new Text();
            machineName.setText(batch.getDeviceName());
            machineName.setTextAlignment(TextAlignment.CENTER);

            // room combobox
            ComboBox<Location> locations = new ComboBox<>();
            locations.setConverter(new StringConverter<>() {
                @Override
                public String toString(Location location) {
                    if (location == null)
                        return "";
                    return location.getName();
                }

                @Override
                public Location fromString(String s) {
                    return null;
                }
            });
            locations.setItems(into.getLocations());

            // | machine name      |
            // | combobox of rooms |
            // | ------- | ------- |
            // | events  | recordings |
            GridPane layout = new GridPane();
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

            RowConstraints row3 = new RowConstraints();
            row1.setVgrow(Priority.ALWAYS);
            row1.setFillHeight(true);
            row1.setPrefHeight(Region.USE_COMPUTED_SIZE);

            ColumnConstraints col0 = new ColumnConstraints();
            col0.setHgrow(Priority.ALWAYS);
            col0.setFillWidth(true);
            col0.setPrefWidth(Region.USE_COMPUTED_SIZE);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setHgrow(Priority.NEVER);
            col1.setFillWidth(true);
            col1.setPrefWidth(Region.USE_COMPUTED_SIZE);

            ColumnConstraints col2 = new ColumnConstraints();
            col2.setHgrow(Priority.ALWAYS);
            col2.setFillWidth(true);
            col2.setPrefWidth(Region.USE_COMPUTED_SIZE);
            layout.getRowConstraints().setAll(row0, row1, row2, row3);
            layout.getColumnConstraints().setAll(col0, col1, col2);
        }
    }
}
// me 20 | 20
// me 17 | 11
// me  7 | 7
// me -3 | -5
