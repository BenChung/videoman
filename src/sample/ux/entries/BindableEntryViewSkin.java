package sample.ux.entries;

import impl.com.calendarfx.view.DayEntryViewSkin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import sample.model.Lecture;
import sample.model.Recording;
import sample.ux.dragging.LinkDisplay;
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

        this.dragPoint = new DragPoint(userObject);
        LinkDisplay display = LinkDisplayManager.getInstance().getDisplay();
        Object opposite = null;
        if (userObject instanceof Lecture) {
            opposite = display.linkMap.get(userObject);
        } else if (userObject instanceof Recording) {
            opposite = display.linkMap.getKey(userObject);
        } else {
            throw new RuntimeException("Invalid mapped object");
        }

        this.dragPoint.setConnected(display.getPoint(opposite));
        dragPoint.connectedProperty().addListener((bnd, ov, nv) -> {
            if (nv != null) {
                if (userObject instanceof Lecture && nv.boundObj instanceof Recording) {
                    display.linkMap.put((Lecture)userObject, (Recording)nv.boundObj);
                }
                if (userObject instanceof Recording && nv.boundObj instanceof Lecture) {
                    display.linkMap.put((Lecture)nv.boundObj, (Recording)userObject);
                }
            } else {
                if (userObject instanceof Lecture) {
                    display.linkMap.remove(userObject);
                } else {
                    display.linkMap.removeValue(userObject);
                }
            }
        });

        display.registerPoint(userObject, this.dragPoint);
        DragPoint other = display.getPoint(opposite);
        if (other != null && !display.hasLink(dragPoint, other)) {
            display.getLinks().put(dragPoint, other);
        }

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
