package sample.ux;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DayView;
import com.calendarfx.view.RequestEvent;
import com.calendarfx.view.TimeScaleView;
import impl.com.calendarfx.view.DayViewScrollPane;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.fxmisc.easybind.EasyBind;
import sample.model.Event;
import sample.model.Lecture;
import sample.model.Location;
import sample.model.util.ExtractedObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EventPaneSkin extends SkinBase<EventPane> {
    private final ScrollBar scrollBar;
    private final DayViewScrollPane eventViewScrollPane;
    private final DayViewScrollPane recordingViewScrollPane;
    private final DayViewScrollPane timeScaleScrollPane;
    private final Separator separator;
    private final GridPane gridPane;
    private final StackPane stackPane;

    protected EventPaneSkin(EventPane eventPane) {
        super(eventPane);

        scrollBar = new ScrollBar();
        DayView eventView = eventPane.getEventView();
        eventViewScrollPane = new DayViewScrollPane(eventView, scrollBar);

        DayView recordingView = eventPane.getRecordingView();
        recordingViewScrollPane = new DayViewScrollPane(recordingView, scrollBar);

        TimeScaleView timeScaleView = eventPane.getTimeScaleView();
        timeScaleScrollPane = new DayViewScrollPane(timeScaleView, scrollBar);


        Bindings.bindBidirectional(timeScaleView.dateProperty(), eventPane.getWeekDayHeader().dateProperty());
        eventPane.getWeekDayHeader().todayProperty().bind(timeScaleView.dateProperty());
        eventPane.getWeekDayHeader().addEventHandler(RequestEvent.REQUEST_DATE, x -> {
            timeScaleView.setDate(x.getDate());
        });

        HBox weekButtons = new HBox(2);
        weekButtons.getChildren().add(eventPane.prevButton);
        weekButtons.getChildren().add(eventPane.nextButton);

        separator = new Separator(Orientation.VERTICAL);

        RowConstraints row0 = new RowConstraints();
        row0.setVgrow(Priority.NEVER);
        row0.setFillHeight(true);
        row0.setPrefHeight(Region.USE_COMPUTED_SIZE);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.NEVER);
        row1.setFillHeight(true);
        row1.setPrefHeight(Region.USE_COMPUTED_SIZE);

        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        row2.setFillHeight(true);
        row2.setPrefHeight(Region.USE_COMPUTED_SIZE);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHgrow(Priority.NEVER);
        col0.setFillWidth(true);
        col0.setPrefWidth(Region.USE_COMPUTED_SIZE);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setFillWidth(true);
        col1.setPrefWidth(Region.USE_COMPUTED_SIZE);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.NEVER);
        col2.setFillWidth(true);
        col2.setPrefWidth(Region.USE_COMPUTED_SIZE);

        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.ALWAYS);
        col3.setFillWidth(true);
        col3.setPrefWidth(Region.USE_COMPUTED_SIZE);

        HBox deviceSelect = new HBox(4);
        Text devText = new Text("Device: ");
        deviceSelect.getChildren().add(devText);
        deviceSelect.getChildren().add(eventPane.getDeviceSelect());

        HBox roomSelect = new HBox(4);
        Text roomText = new Text("Room: ");
        roomSelect.getChildren().add(roomText);
        roomSelect.getChildren().add(eventPane.getRoomSelect());


        gridPane = new GridPane();
        gridPane.getRowConstraints().setAll(row0, row1, row2);
        gridPane.getColumnConstraints().setAll(col0, col1, col2, col3);
        gridPane.add(weekButtons, 0, 0);
        gridPane.add(deviceSelect, 1, 0);
        gridPane.add(roomSelect, 3, 0);
        gridPane.add(eventPane.getWeekDayHeader(), 0,1);
        GridPane.setColumnSpan(eventPane.getWeekDayHeader(), 4);
        gridPane.add(timeScaleScrollPane, 0, 2);
        gridPane.add(recordingViewScrollPane, 1, 2);
        gridPane.add(separator, 2, 2);
        gridPane.add(eventViewScrollPane, 3, 2);

        stackPane = new StackPane();
        stackPane.getChildren().addAll(gridPane, eventPane.getLinkDisplay());
        getChildren().add(stackPane);
    }
}
