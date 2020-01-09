package sample.ux.dragging;

import javafx.geometry.Point2D;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import sample.ux.entries.DragPoint;

public class DragLink extends Control {

    public DragLink() {
        this.start = new SimpleObjectProperty<>(new Point2D(0.0f, 0.0f));
        this.end = new SimpleObjectProperty<>(new Point2D(0.0f, 0.0f));
    }

    private final ObjectProperty<Point2D> start, end;
    private DragPoint startPoint = null, endPoint = null;
    public void setLinked(DragPoint startPoint, DragPoint endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
    public DragPoint getStartPoint() { return startPoint; }
    public DragPoint getEndPoint() { return endPoint; }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DragLinkSkin(this);
    }

    public Point2D getStart() {
        return start.get();
    }

    public ObjectProperty<Point2D> startProperty() {
        return start;
    }

    public Point2D getEnd() {
        return end.get();
    }

    public ObjectProperty<Point2D> endProperty() {
        return end;
    }
}
