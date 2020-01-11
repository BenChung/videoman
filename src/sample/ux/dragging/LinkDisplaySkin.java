package sample.ux.dragging;

import javafx.collections.MapChangeListener;
import javafx.scene.control.SkinBase;
import sample.ux.entries.DragPoint;

import java.util.HashMap;

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

    private void addLink(Object key, Object added) {
        addLink((DragPoint)key, (DragPoint)added);
    }

    private void addLink(DragPoint source, DragPoint target) {
        DragLink link = new DragLink();
        link.startProperty().bind(source.centerBindingProperty());
        link.endProperty().bind(target.centerBindingProperty());
        getSkinnable().linkPoint(source, link);
        getSkinnable().linkPoint(target, link);
        link.setLinked(source, target);
        getChildren().add(link);
    }
}
