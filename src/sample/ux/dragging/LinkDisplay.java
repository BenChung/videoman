package sample.ux.dragging;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import sample.model.Bindable;
import sample.ux.entries.DragPoint;
import sample.model.Lecture;
import sample.model.Recording;

import java.util.HashMap;
import java.util.LinkedList;

// the job of the display is to enable the creation/modification of links
// and the restoration of existing links
public class LinkDisplay extends Control {
    public DualHashBidiMap<Lecture, Recording> linkMap; // here temporarily

    public LinkDisplay(DualHashBidiMap<Lecture, Recording> mapping) {
        this.linkMap = mapping;
        setMouseTransparent(true);
    }

    // dragpoint management
    HashMap<Object, DragPoint> points = new HashMap<>();
    public void registerPoint(Object ref, DragPoint point) {
        // remember the association
        points.put(ref, point);
    }
    public void unregisterPoint(Object ref) {
        points.remove(ref);
    }

    public DragPoint getPoint(Object ref) {
        return points.get(ref);
    }

    public void showLink(DragLink link) {
        getChildren().add(link);
    }

    public void removeLink(DragLink link) {
        getChildren().remove(link);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LinkDisplaySkin(this);
    }

    // visual link management
    private ObservableMap<DragPoint, DragPoint> links = FXCollections.observableHashMap();
    public ObservableMap<DragPoint, DragPoint> getLinks() {
        return links;
    }

    private HashMap<DragPoint, DragLink> linkedPoints = new HashMap<>();

    public void linkPoint(DragPoint point, DragLink link) {
        linkedPoints.put(point, link);
    }

    public DragLink lookupLinkedPoint(DragPoint point) {
        return linkedPoints.get(point);
    }

    public void removeLinkedPoint(DragPoint point) {
        linkedPoints.remove(point);
    }

    private void clearLink(DragLink toClear) {
        links.remove(toClear.getStartPoint(), toClear.getEndPoint());
        toClear.getStartPoint().setConnected(null);
        toClear.getEndPoint().setConnected(null);
        linkedPoints.remove(toClear.getStartPoint());
        linkedPoints.remove(toClear.getEndPoint());
    }

    // drag and drop
    DragLink currentLink = null; DragPoint source = null; DragPoint snapTarget = null; boolean isEndPoint = true;
    public void startDrag(DragPoint dragPoint, MouseEvent mouseEvent) {
        if (linkedPoints.containsKey(dragPoint)) {
            currentLink = lookupLinkedPoint(dragPoint);
            isEndPoint = currentLink.getEndPoint() == dragPoint;
            if (isEndPoint) {
                source = currentLink.getStartPoint();
                currentLink.endProperty().unbind();
            } else {
                source = currentLink.getEndPoint();
                currentLink.startProperty().unbind();
            }
            clearLink(currentLink);
        } else {
            source = dragPoint;
            isEndPoint = true;
        }
        currentLink = new DragLink();
        currentLink.startProperty().bind(source.centerBindingProperty());

        updateEndPos(mouseEvent);
        showLink(currentLink);
    }


    private void updateEndPos(MouseEvent event) {
        if (currentLink == null)
            return;
        currentLink.endProperty().set(new Point2D(event.getSceneX(), event.getSceneY()));
    }
    private void snapEndToPoint(DragPoint to) {
        currentLink.endProperty().bind(to.centerBindingProperty());
    }
    private void unsnapEndFromPoint(DragPoint to) {
        currentLink.endProperty().unbind();
    }

    public void continueDrag(DragPoint dragPoint, MouseEvent mouseEvent) {
        if (snapTarget == null)
            updateEndPos(mouseEvent);

    }

    public void dragOver(DragPoint dragPoint, MouseDragEvent mouseDragEvent) {
        if (source != dragPoint) {
            snapTarget = dragPoint;
            snapEndToPoint(dragPoint);
        }
    }

    public void dragOff(DragPoint dragPoint, MouseDragEvent mouseDragEvent) {
        if (source != dragPoint) {
            snapTarget = null;
            unsnapEndFromPoint(dragPoint);
        }
    }

    public void dragRelease(DragPoint dragPoint, MouseEvent mouseEvent) {
        if (currentLink != null)
            removeLink(currentLink);
    }

    public void dragDropped(DragPoint dragPoint, MouseDragEvent mouseDragEvent) {
        if (source != dragPoint && linkValid(source, dragPoint)) {
            if (linkedPoints.containsKey(dragPoint)) { // target point is already linked
                DragLink link = lookupLinkedPoint(dragPoint);
                links.remove(link.getStartPoint(), link.getEndPoint());
            }
            if (dragPoint.getConnected() != null)
                dragPoint.getConnected().setConnected(null);
            if (source.getConnected() != null)
                source.getConnected().setConnected(null);
            dragPoint.setConnected(source);
            source.setConnected(dragPoint);
            links.put(source, dragPoint);
        }
    }

    private boolean linkValid(DragPoint source, DragPoint target) {
        return (source.boundObj instanceof Lecture && target.boundObj instanceof Recording) ||
                (source.boundObj instanceof Recording && target.boundObj instanceof Lecture);
    }

    // invalidate existing links, to be recreated as events get loaded in
    public void clearLinks(Class<?> pointType) {
        LinkedList<Object> toRemove = new LinkedList<>();
        for (Object p : points.keySet()) {
            if (pointType.isAssignableFrom(p.getClass()))
                toRemove.add(p);
        }
        toRemove.forEach(points::remove);

        links.clear();
        getChildren().clear();
    }

    public boolean hasLink(DragPoint dragPoint, DragPoint other) {
        return links.containsKey(dragPoint) || links.containsKey(other);
    }
}
