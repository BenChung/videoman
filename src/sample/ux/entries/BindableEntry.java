package sample.ux.entries;

import com.calendarfx.model.Entry;
import com.calendarfx.view.DayEntryView;
import javafx.scene.control.Skin;

public class BindableEntry extends DayEntryView {

    public BindableEntry(Entry<?> entry) {
        super(entry);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new BindableEntryViewSkin(this);
    }
}
