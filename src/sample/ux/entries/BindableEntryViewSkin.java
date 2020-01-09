package sample.ux.entries;

import impl.com.calendarfx.view.DayEntryViewSkin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import sample.model.Bindable;
import sample.ux.dragging.LinkDisplayManager;

public class BindableEntryViewSkin extends DayEntryViewSkin {
    BorderPane entryLayout;
    DragPoint dragPoint;
    Pane infoPane;
    public BindableEntryViewSkin(BindableEntry eventEntry) {
        super(eventEntry);
        //clear the children from the original day entry
        getChildren().clear();

        //put them into a new pane
        this.infoPane = new Pane();
        infoPane.getChildren().addAll(titleLabel, startTimeLabel);

        Object userObject = getEntry().getUserObject();
        if (!(userObject instanceof Bindable)) {
            throw new RuntimeException("Invalid bound object for date display");
        }
        Bindable object = (Bindable)userObject;
        this.dragPoint = new DragPoint(userObject);
        dragPoint.boundProperty().addListener((bnd, nv, ov) -> {
            if (nv != null)
                object.setBinding(nv.getBound().boundObj);
            else
                object.setBinding(null);
        });
        LinkDisplayManager.getInstance().getDisplay().registerPoint(userObject, this.dragPoint);

        this.entryLayout = new BorderPane();
        entryLayout.setCenter(infoPane);
        entryLayout.setRight(dragPoint);
        BorderPane.setAlignment(dragPoint, Pos.CENTER_LEFT);
        BorderPane.setMargin(dragPoint, new Insets(0.0,7.0, 0.0, 7.0));
        BorderPane.setAlignment(infoPane, Pos.CENTER_RIGHT);
        entryLayout.setMinWidth(Control.USE_PREF_SIZE);
        entryLayout.setMinHeight(Control.USE_PREF_SIZE);
        entryLayout.prefWidthProperty().bind(eventEntry.widthProperty());
        entryLayout.prefHeightProperty().bind(eventEntry.heightProperty());

        getChildren().add(entryLayout);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
        entryLayout.requestLayout();
    }
}
