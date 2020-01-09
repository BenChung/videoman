package sample.ux.vidimp;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import sample.model.Event;
import sample.model.importmodel.Import;

/*
Logical workflow:
    search for new folders in import drive
->  read CSV manifests, validate checksums
->  generate instance of Import
->  display import dialog
    column for each scheduled event
    drop down with recording device selection
    objective: assign Batches to rooms
UI idea:
    radio button for each Batch above calendar
    show Batch's recordings in calendar preview
 */

public class EventImport extends Control {
    public Event getEvent() {
        return event;
    }

    private final Event event;

    public Import getImp() {
        return imp;
    }
    private final Import imp;

    public EventImport(Import imp, Event event) {
        this.imp = imp;
        this.event = event;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new EventImportSkin(this);
    }
}
