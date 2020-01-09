package sample.ux.entries;

import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DragPointSkin extends SkinBase<DragPoint> {
    private final Circle dragCircle;

    protected DragPointSkin(DragPoint dragPoint) {
        super(dragPoint);

        dragCircle = new Circle();
        dragCircle.setRadius(dragPoint.radius);
        dragCircle.setStrokeWidth(dragPoint.border);
        dragCircle.setStroke(Color.GREY);
        dragCircle.setFill(Color.TRANSPARENT);
        dragCircle.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> dragCircle.setFill(Color.DARKGREY));
        dragCircle.addEventFilter(MouseEvent.MOUSE_EXITED, e -> dragCircle.setFill(Color.TRANSPARENT));
        dragCircle.setOnDragDetected(dragPoint::dragDetected);
        dragCircle.setOnMouseDragged(dragPoint::dragContinued);
        dragCircle.setOnMouseDragEntered(dragPoint::snapMouseTarget);
        dragCircle.setOnMouseDragExited(dragPoint::unsnapTarget);
        dragCircle.setOnMouseReleased(dragPoint::release);
        dragCircle.setOnMouseDragReleased(dragPoint::dropped);

        getChildren().add(dragCircle);
    }
}
