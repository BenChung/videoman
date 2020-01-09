package sample.ux;

import com.calendarfx.view.DayViewBase;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.fxmisc.easybind.EasyBind;

import java.util.function.Function;

public class CalendarUtils {
    public static void bindPartial(DayViewBase a, DayViewBase b) {
        Bindings.bindBidirectional(a.earlyLateHoursStrategyProperty(), b.earlyLateHoursStrategyProperty());
        Bindings.bindBidirectional(a.hoursLayoutStrategyProperty(), b.hoursLayoutStrategyProperty());
        Bindings.bindBidirectional(a.hourHeightProperty(), b.hourHeightProperty());
        Bindings.bindBidirectional(a.hourHeightCompressedProperty(), b.hourHeightCompressedProperty());
        Bindings.bindBidirectional(a.visibleHoursProperty(), b.visibleHoursProperty());
        Bindings.bindBidirectional(a.enableCurrentTimeMarkerProperty(), b.enableCurrentTimeMarkerProperty());
        Bindings.bindBidirectional(a.trimTimeBoundsProperty(), b.trimTimeBoundsProperty());
        Bindings.bindBidirectional(a.dateProperty(), b.dateProperty());
    }
}
