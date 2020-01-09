package sample.ux.dragging;

import javafx.collections.MapChangeListener;
import javafx.scene.control.SkinBase;
import sample.ux.entries.DragPoint;

public class LinkDisplaySkin extends SkinBase<LinkDisplay> {
    protected LinkDisplaySkin(LinkDisplay linkDisplay) {
        super(linkDisplay);
        linkDisplay.getLinks().addListener(this::onChanged);
        linkDisplay.getLinks().forEach(this::addLink);
    }

    private void onChanged(MapChangeListener.Change<? extends DragPoint, ? extends DragPoint> change) {
        DragPoint key = change.getKey();
        DragPoint pointAdded = change.getValueAdded();
        if (change.wasAdded()) {
            addLink(key, pointAdded);
        }
        if (change.wasRemoved()) {
            DragLink sourceLink = getSkinnable().lookupLinkedPoint(key);
            getChildren().remove(sourceLink);
            getSkinnable().removeLinkedPoint(key);
            getSkinnable().removeLinkedPoint(change.getValueRemoved());
        }
    }

    private void addLink(DragPoint key, DragPoint valueAdded) {
        var points = getSkinnable().points;
        DragPoint source = points.get(key.boundObj);
        DragPoint target = points.get(valueAdded.boundObj);
        DragLink link = new DragLink();
        link.startProperty().bind(source.centerBindingProperty());
        link.endProperty().bind(target.centerBindingProperty());
        getSkinnable().linkPoint(key, link);
        getSkinnable().linkPoint(target, link);
        link.setLinked(key, target);
        getChildren().add(link);
    }
}
