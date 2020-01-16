package sample.ux.entries;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import sample.model.Bindable;
import sample.ux.dragging.DragLink;
import sample.ux.dragging.LinkDisplayManager;

public class DragPoint extends Control {
    public final double radius = 10.0;
    public final double border = 2.0;
    public final Object boundObj;

    // logic:
    // if the dragpoint is bound AND has a link, then it is fully visible
    // if the dragpoint is bound BUT no link, then it should be filled
    // if the dragpoint is not bound BUT is linked, then inconsistent state
    // if the dragpoint is not bound AND is not linked, then it is ready

    public ObservableValue<DragPoint> connectedProperty() { return connected; }

    public DragPoint getConnected() {
        return connected.get();
    }

    public void setConnected(DragPoint connected) {
        this.connected.set(connected);
    }

    private ObjectProperty<DragPoint> connected = new SimpleObjectProperty<>();

    ObjectBinding<Point2D> centerBinding;
    public ObservableValue<Point2D> centerBindingProperty() {
        return centerBinding;
    }

    public DragPoint(Object bound) {
        centerBinding = Bindings.createObjectBinding(() -> {
                    var bounds = localToScene(boundsInLocalProperty().get());
                    return new Point2D(bounds.getCenterX(), bounds.getCenterY()); },
                localToSceneTransformProperty(), onScrollStartedProperty());
        localToSceneTransformProperty().addListener((obs, old, newv) -> {}); // make sure the property is updated

        boundObj = bound;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DragPointSkin(this);
    }

    public void dragDetected(MouseEvent mouseEvent) {
        startFullDrag();
        mouseEvent.consume();
        LinkDisplayManager.getInstance().getDisplay().startDrag(this, mouseEvent);
    }

    public void dragContinued(MouseEvent mouseEvent) {
        mouseEvent.consume();
        LinkDisplayManager.getInstance().getDisplay().continueDrag(this, mouseEvent);
    }
    public void snapMouseTarget(MouseDragEvent mouseDragEvent) {
        LinkDisplayManager.getInstance().getDisplay().dragOver(this, mouseDragEvent);
        mouseDragEvent.consume();
    }
    public void unsnapTarget(MouseDragEvent mouseDragEvent) {
        LinkDisplayManager.getInstance().getDisplay().dragOff(this, mouseDragEvent);
        mouseDragEvent.consume();
    }
    public void release(MouseEvent mouseEvent) {
        LinkDisplayManager.getInstance().getDisplay().dragRelease(this, mouseEvent);
        mouseEvent.consume();
    }
    public void dropped(MouseDragEvent mouseDragEvent) {
        LinkDisplayManager.getInstance().getDisplay().dragDropped(this, mouseDragEvent);
    }
}
