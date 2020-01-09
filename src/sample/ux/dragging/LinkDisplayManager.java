package sample.ux.dragging;

public class LinkDisplayManager {
    private LinkDisplayManager() {}

    private static LinkDisplayManager manager = new LinkDisplayManager();
    public static LinkDisplayManager getInstance() {
        return manager;
    }

    private LinkDisplay display = null;
    public void setDisplay(LinkDisplay display) {
        this.display = display;
    }
    public LinkDisplay getDisplay() {
        return display;
    }
}
