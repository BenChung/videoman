package sample.ux.dragging;

import javafx.beans.binding.Bindings;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class DragLinkSkin extends SkinBase<DragLink> {
    Circle startCircle, endCircle;
    Line connector;

    protected DragLinkSkin(DragLink dragLink) {
        super(dragLink);

        var start = Bindings.createObjectBinding(() -> dragLink.sceneToLocal(dragLink.startProperty().get()),
                dragLink.startProperty(), dragLink.localToSceneTransformProperty());
        var end = Bindings.createObjectBinding(() -> dragLink.sceneToLocal(dragLink.endProperty().get()),
                dragLink.endProperty(), dragLink.localToSceneTransformProperty());
        var startx = Bindings.createDoubleBinding(() -> start.get().getX(), start);
        var starty = Bindings.createDoubleBinding(() -> start.get().getY(), start);
        var endx = Bindings.createDoubleBinding(() -> end.get().getX(), end);
        var endy = Bindings.createDoubleBinding(() -> end.get().getY(), end);
        this.startCircle = new Circle();
        startCircle.setRadius(9.0);
        startCircle.setFill(Color.DARKGREY);
        startCircle.centerXProperty().bind(startx);
        startCircle.centerYProperty().bind(starty);
        startCircle.setMouseTransparent(true);
        startCircle.setManaged(false);
        startCircle.toFront();

        this.endCircle = new Circle();
        endCircle.setRadius(10.0);
        endCircle.setFill(Color.DARKGREY);
        endCircle.centerXProperty().bind(endx);
        endCircle.centerYProperty().bind(endy);
        endCircle.setMouseTransparent(true);
        endCircle.setManaged(false);
        endCircle.toFront();

        this.connector = new Line();
        connector.setStroke(Color.DARKGREY);
        connector.setStrokeWidth(2.0);
        connector.startXProperty().bind(startx);
        connector.startYProperty().bind(starty);
        connector.endXProperty().bind(endx);
        connector.endYProperty().bind(endy);
        connector.setMouseTransparent(true);
        connector.setManaged(false);
        connector.toFront();


        getChildren().addAll(startCircle, endCircle, connector);
    }
}
