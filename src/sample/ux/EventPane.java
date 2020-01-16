package sample.ux;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DayView;
import com.calendarfx.view.TimeScaleView;
import com.calendarfx.view.WeekDayHeaderView;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SkinBase;
import javafx.util.StringConverter;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import sample.model.*;
import sample.ux.dragging.LinkDisplay;
import sample.ux.dragging.LinkDisplayManager;
import sample.ux.entries.BindableEntry;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventPane extends Control {
    public DayView getEventView() {
        return eventView;
    }

    private DayView eventView = new DayView();

    public DayView getRecordingView() {
        return recordingView;
    }

    private DayView recordingView = new DayView();

    public TimeScaleView getTimeScaleView() {
        return timeScaleView;
    }

    private TimeScaleView timeScaleView = new TimeScaleView();

    private LinkDisplay linkDisplay;

    public LinkDisplay getLinkDisplay() {
        return linkDisplay;
    }

    public WeekDayHeaderView getWeekDayHeader() {
        return weekDayHeader;
    }

    private WeekDayHeaderView weekDayHeader = new WeekDayHeaderView();

    public ComboBox<Location> getRoomSelect() {
        return roomSelect;
    }

    public ComboBox<Device> getDeviceSelect() {
        return deviceSelect;
    }

    private ComboBox<Location> roomSelect = new ComboBox<>();
    private ComboBox<Device> deviceSelect = new ComboBox<>();

    private ObjectProperty<Event> event = new SimpleObjectProperty<>();
    public ObjectProperty<Event> getEvent() {
        return event;
    }


    public EventPane() {
        CalendarUtils.bindPartial(timeScaleView, eventView);
        CalendarUtils.bindPartial(timeScaleView, recordingView);

        eventView.setEntryViewFactory(BindableEntry::new);
        recordingView.setEntryViewFactory(BindableEntry::new);
        eventView.setSelectionMode(SelectionMode.SINGLE);
        recordingView.setSelectionMode(SelectionMode.SINGLE);

        linkDisplay = new LinkDisplay(new DualHashBidiMap<>());
        event.addListener((bnd, ov, nv) -> {
            if (nv != null) linkDisplay.linkMap = nv.getMapping();
        });

        LinkDisplayManager.getInstance().setDisplay(linkDisplay);

        eventView.calendarsProperty().addListener((InvalidationListener) (obs) -> {
            LinkDisplayManager.getInstance().getDisplay().clearLinks(Lecture.class);
        });
        recordingView.calendarsProperty().addListener((InvalidationListener) (obs) -> {
            LinkDisplayManager.getInstance().getDisplay().clearLinks(Recording.class);
        });
        eventView.dateProperty().addListener((bnd, nv, ov) -> {
            LinkDisplayManager.getInstance().getDisplay().clearLinks(Lecture.class);
            LinkDisplayManager.getInstance().getDisplay().clearLinks(Recording.class);
        });


        roomSelect.setConverter(new StringConverter<Location>() {
            @Override
            public String toString(Location location) {
                if (location == null)
                    return "";
                return location.getName();
            }

            @Override
            public Location fromString(String s) {
                return null;
            }
        });

        deviceSelect.setConverter(new StringConverter<Device>() {
            @Override
            public String toString(Device device) {
                if (device == null)
                    return "";
                return device.getName();
            }

            @Override
            public Device fromString(String s) {
                return null;
            }
        });

        event.addListener((bnd, ov, nv) -> {
            Bindings.bindContent(roomSelect.getItems(), nv.getLocations());
            roomSelect.setValue(null);

            Bindings.bindContent(deviceSelect.getItems(), nv.getRecDevs());
            deviceSelect.setValue(null);
        });

        roomSelect.valueProperty().addListener((bnd, ov, nv) -> {
            if (nv == null)
                return;
            List<Lecture> locs = event.get().getLectures().stream().filter(x -> x.getLocation() == nv)
                    .collect(Collectors.toList());
            showEvents(locs);
        });


        deviceSelect.valueProperty().addListener((bnd, ov, nv) -> {
            if (nv == null)
                return;
            List<Recording> locs = event.get().getRecordings().stream().filter(x -> x.getDevice() == nv)
                    .collect(Collectors.toList());
            showRecordings(locs);
        });
    }

    public SkinBase<EventPane> createDefaultSkin() {
        return new EventPaneSkin(this);
    }

    public void showEvents(List<Lecture> locs) {
        var cs = new CalendarSource();
        var cal = new Calendar();
        var date = LocalDate.now();
        for (var loc : locs) {
            var entry = new Entry<>();
            entry.changeStartDate(LocalDate.ofInstant(loc.getStart(), ZoneOffset.UTC));
            entry.changeStartTime(LocalTime.ofInstant(loc.getStart(), ZoneOffset.UTC));
            entry.changeEndDate(LocalDate.ofInstant(loc.getEnd(), ZoneOffset.UTC));
            entry.changeEndTime(LocalTime.ofInstant(loc.getEnd(), ZoneOffset.UTC));
            entry.setUserObject(loc);
            entry.setTitle(loc.getTitle());
            date = LocalDate.ofInstant(loc.getStart(), ZoneId.systemDefault());
            cal.addEntry(entry);
        };
        cs.getCalendars().add(cal);
        eventView.setDate(date);
        eventView.getCalendarSources().setAll(cs);
    }

    public void showRecordings(List<Recording> recordings) {
        var cs = new CalendarSource();
        var cal = new Calendar();
        var date = LocalDate.now();
        for (var recording : recordings) {
            var entry = new Entry<>();
            entry.changeEndDate(LocalDate.ofInstant(recording.getEnd(), ZoneOffset.UTC));
            entry.changeEndTime(LocalTime.ofInstant(recording.getEnd(), ZoneOffset.UTC));
            entry.changeStartDate(LocalDate.ofInstant(recording.getStart(), ZoneOffset.UTC));
            entry.changeStartTime(LocalTime.ofInstant(recording.getStart(), ZoneOffset.UTC));
            entry.setUserObject(recording);
            entry.setTitle("Placeholder title");
            date = LocalDate.ofInstant(recording.getStart(), ZoneId.systemDefault());
            cal.addEntry(entry);
        };
        cs.getCalendars().add(cal);
        recordingView.setDate(date);
        recordingView.getCalendarSources().setAll(cs);
    }
}
