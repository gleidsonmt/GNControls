/*
 * Copyright (C) Gleidson Neves da Silveira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gn.skin;

import com.gn.control.GNDatePicker;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.sun.javafx.PlatformUtil.isMac;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoUnit.*;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/03/2019
 */
public class GNDatePickerContent extends VBox {

    protected GNDatePicker datePicker;
    private Button backMonthButton;
    private Button forwardMonthButton;
    private Button backYearButton;
    private Button forwardYearButton;
    private Label monthLabel;
    private Label yearLabel;
    protected GridPane gridPane;
    private VBox header = new VBox();
    private HBox actions = new HBox();
    private HBox monthSpinner = new HBox();
    private SVGPath rightArrow = new SVGPath();
    private SVGPath leftArrow = new SVGPath();
    private HBox yearSpinner = new HBox();

    Label info = new Label();

    private int daysPerWeek;
    private List<DateCell> dayNameCells = new ArrayList<DateCell>();
    private List<DateCell> weekNumberCells = new ArrayList<DateCell>();
    protected List<DateCell> dayCells = new ArrayList<DateCell>();
    private LocalDate[] dayCellDates;
    private DateCell lastFocusedDayCell = null;

    final DateTimeFormatter monthFormatter =
            DateTimeFormatter.ofPattern("MMMM");

    final DateTimeFormatter monthFormatterSO =
            DateTimeFormatter.ofPattern("LLLL"); // Standalone month name

    final DateTimeFormatter yearFormatter =
            DateTimeFormatter.ofPattern("y");

    final DateTimeFormatter yearWithEraFormatter =
            DateTimeFormatter.ofPattern("GGGGy"); // For Japanese. What to use for others??

    final DateTimeFormatter weekNumberFormatter =
            DateTimeFormatter.ofPattern("w");

    final DateTimeFormatter weekDayNameFormatter =
            DateTimeFormatter.ofPattern("ccc"); // Standalone day name

    final DateTimeFormatter dayCellFormatter =
            DateTimeFormatter.ofPattern("d");

    static String getString(String key) {
        return ControlResources.getString("DatePicker."+key);
    }




    GNDatePickerContent(final GNDatePicker datePicker) {
        this.datePicker = datePicker;

        getStyleClass().add("date-picker-popup");

        this.getStylesheets().add(GNDatePickerContent.class.getResource("/com/gn/css/datePopup.css").toExternalForm());

        daysPerWeek = getDaysPerWeek();

        {
            LocalDate date = datePicker.getValue();
            displayedYearMonth.set((date != null) ? YearMonth.from(date) : YearMonth.now());
        }

        displayedYearMonth.addListener((observable, oldValue, newValue) -> {
            updateValues();
        });

        getChildren().add(createMonthYearPane());

        gridPane = new GridPane() {
            @Override protected double computePrefWidth(double height) {
                final double width = super.computePrefWidth(height);

                // RT-30903: Make sure width snaps to pixel when divided by
                // number of columns. GridPane doesn't do this with percentage
                // width constraints. See GridPane.adjustColumnWidths().
                final int nCols = daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);
                final double snaphgap = snapSpace(getHgap());
                final double left = snapSpace(getInsets().getLeft());
                final double right = snapSpace(getInsets().getRight());
                final double hgaps = snaphgap * (nCols - 1);
                final double contentWidth = width - left - right - hgaps;
                return ((snapSize(contentWidth / nCols)) * nCols) + left + right + hgaps;
            }

            @Override protected void layoutChildren() {
                // Prevent AssertionError in GridPane
                if (getWidth() > 0 && getHeight() > 0) {
                    super.layoutChildren();
                }
            }
        };
        gridPane.setFocusTraversable(true);
        gridPane.getStyleClass().add("calendar-grid");
        gridPane.setVgap(-1);
        gridPane.setHgap(-1);

        gridPane.setMinHeight(179);
        gridPane.setPrefHeight(179);
        gridPane.setMaxHeight(179);

        // Add a focus owner listener to Scene when it becomes available.
        final WeakChangeListener<Node> weakFocusOwnerListener =
                new WeakChangeListener<Node>((ov2, oldFocusOwner, newFocusOwner) -> {
                    if (newFocusOwner == gridPane) {
                        if (oldFocusOwner instanceof DateCell) {
                            // Backwards traversal, skip gridPane.
//                            gridPane.impl_traverse(Direction.PREVIOUS);
                        } else {
                            // Forwards traversal, pass focus to day cell.
                            if (lastFocusedDayCell != null) {
                                Platform.runLater(() -> {
                                    lastFocusedDayCell.requestFocus();
                                });
                            } else {
                                clearFocus();
                            }
                        }
                    }
                });
        gridPane.sceneProperty().addListener(new WeakChangeListener<Scene>((ov, oldScene, newScene) -> {
            if (oldScene != null) {
                oldScene.focusOwnerProperty().removeListener(weakFocusOwnerListener);
            }
            if (newScene != null) {
                newScene.focusOwnerProperty().addListener(weakFocusOwnerListener);
            }
        }));
        if (gridPane.getScene() != null) {
            gridPane.getScene().focusOwnerProperty().addListener(weakFocusOwnerListener);
        }

        // get the weekday labels starting with the weekday that is the
        // first-day-of-the-week according to the locale in the
        // displayed LocalDate
        for (int i = 0; i < daysPerWeek; i++) {
            DateCell cell = new DateCell();
            cell.getStyleClass().add("day-name-cell");
            dayNameCells.add(cell);
        }

        // Week number column
        for (int i = 0; i < 6; i++) {
            DateCell cell = new DateCell();
            cell.getStyleClass().add("week-number-cell");
            weekNumberCells.add(cell);
        }

        createDayCells();
        updateGrid();
        getChildren().add(gridPane);
        getChildren().add(createViewActions());

        refresh();

        // RT-30511: This prevents key events from reaching the popup's owner.
        addEventHandler(KeyEvent.ANY, e -> {
            Node node = getScene().getFocusOwner();
            if (node instanceof DateCell) {
                lastFocusedDayCell = (DateCell)node;
            }

            if (e.getEventType() == KeyEvent.KEY_PRESSED) {
                switch (e.getCode()) {
                    case HOME:
                        goToDate(LocalDate.now(), true);
                        e.consume();
                        break;


                    case PAGE_UP:
                        if ((isMac() && e.isMetaDown()) || (!isMac() && e.isControlDown())) {
                            if (!backYearButton.isDisabled()) {
                                forward(-1, YEARS, true);
                            }
                        } else {
                            if (!backMonthButton.isDisabled()) {
                                forward(-1, MONTHS, true);
                            }
                        }
                        e.consume();
                        break;

                    case PAGE_DOWN:
                        if ((isMac() && e.isMetaDown()) || (!isMac() && e.isControlDown())) {
                            if (!forwardYearButton.isDisabled()) {
                                forward(1, YEARS, true);
                            }
                        } else {
                            if (!forwardMonthButton.isDisabled()) {
                                forward(1, MONTHS, true);
                            }
                        }
                        e.consume();
                        break;
                }

                node = getScene().getFocusOwner();
                if (node instanceof DateCell) {
                    lastFocusedDayCell = (DateCell)node;
                }
            }

            // Consume all key events except those that control
            // showing the popup and traversal.
            switch (e.getCode()) {
                case F4:
                case F10:
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                case TAB:
                    break;

                case ESCAPE:
                    datePicker.hide();
                    e.consume();
                    break;

                default:
                    e.consume();
            }
        });
    }

    private VBox createViewActions(){

        VBox content = new VBox();

        Button today = new Button("Today");
        today.setDefaultButton(true);
        today.getStyleClass().add("today");

        today.setOnMouseClicked(event -> {
            datePicker.setValue(LocalDate.now());
            datePicker.hide();
        });

        content.getChildren().add(today);
        content.setAlignment(Pos.CENTER);
        content.getStyleClass().add("today-content");
        return content;
    }

    private ObjectProperty<YearMonth> displayedYearMonth =
            new SimpleObjectProperty<YearMonth>(this, "displayedYearMonth");

    ObjectProperty<YearMonth> displayedYearMonthProperty() {
        return displayedYearMonth;
    }

    void updateValues() {
        // Note: Preserve this order, as DatePickerHijrahContent needs
        // updateDayCells before updateMonthYearPane().
        updateWeeknumberDateCells();
        updateDayCells();
        updateMonthYearPane();
        updateToday();
    }

    void updateToday(){
        info.setText(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + ", " + LocalDate.now().getDayOfMonth());
    }

    void updateDayCells() {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        int firstOfMonthIdx = determineFirstOfMonthDayOfWeek();
        YearMonth curMonth = displayedYearMonth.get();

        // RT-31075: The following are now set in the try-catch block.
        YearMonth prevMonth = null;
        YearMonth nextMonth = null;
        int daysInCurMonth = -1;
        int daysInPrevMonth = -1;
        int daysInNextMonth = -1;

        for (int i = 0; i < 6 * daysPerWeek; i++) {
            DateCell dayCell = dayCells.get(i);
            dayCell.getStyleClass().setAll("cell", "date-cell", "day-cell");
            dayCell.setDisable(false);
            dayCell.setStyle(null);
            dayCell.setGraphic(null);
            dayCell.setTooltip(null);

            try {
                if (daysInCurMonth == -1) {
                    daysInCurMonth = curMonth.lengthOfMonth();
                }
                YearMonth month = curMonth;
                int day = i - firstOfMonthIdx + 1;
                //int index = firstOfMonthIdx + i - 1;
                if (i < firstOfMonthIdx) {
                    if (prevMonth == null) {
                        prevMonth = curMonth.minusMonths(1);
                        daysInPrevMonth = prevMonth.lengthOfMonth();
                    }
                    month = prevMonth;
                    day = i + daysInPrevMonth - firstOfMonthIdx + 1;
                    dayCell.getStyleClass().add("previous-month");
                } else if (i >= firstOfMonthIdx + daysInCurMonth) {
                    if (nextMonth == null) {
                        nextMonth = curMonth.plusMonths(1);
                        daysInNextMonth = nextMonth.lengthOfMonth();
                    }
                    month = nextMonth;
                    day = i - daysInCurMonth - firstOfMonthIdx + 1;
                    dayCell.getStyleClass().add("next-month");
                }
                LocalDate date = month.atDay(day);
                dayCellDates[i] = date;
                ChronoLocalDate cDate = chrono.date(date);

                dayCell.setDisable(false);

                if (isToday(date)) {
                    dayCell.getStyleClass().add("today");
                }

                if (date.equals(datePicker.getValue())) {
                    dayCell.getStyleClass().add("selected");
                }

                String cellText =
                        dayCellFormatter.withLocale(locale)
                                .withChronology(chrono)
                                .withDecimalStyle(DecimalStyle.of(locale))
                                .format(cDate);
                dayCell.setText(cellText);

                dayCell.updateItem(date, false);
            } catch (DateTimeException ex) {
                // Date is out of range.
                // System.err.println(dayCellDate(dayCell) + " " + ex);
                dayCell.setText(" ");
                dayCell.setDisable(true);
            }
        }
    }

    protected GridPane createMonthYearPane() {
        GridPane monthYearPane = new GridPane();

        // create header
        monthYearPane.getStyleClass().add("month-year-pane");

        // Month spinner
        monthSpinner.getStyleClass().add("spinner");

        backMonthButton = new Button();
        backMonthButton.getStyleClass().add("left-button");

        forwardMonthButton = new Button();
        forwardMonthButton.getStyleClass().add("right-button");

        StackPane leftMonthArrow = new StackPane();
        leftMonthArrow.setMinSize(10, 10);
        leftMonthArrow.getStyleClass().add("left-arrow");
        leftMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        backMonthButton.setGraphic(leftMonthArrow);
        backMonthButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        leftArrow.getStyleClass().add("left-arrow");
        leftArrow.setContent("M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z");
        backMonthButton.setGraphic(leftArrow);

        StackPane rightMonthArrow = new StackPane();

        rightMonthArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        rightArrow.getStyleClass().add("right-arrow");
        rightArrow.setContent("M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z");
        forwardMonthButton.setGraphic(rightArrow);

        backMonthButton.setOnAction(t -> {
            forward(-1, MONTHS, false);
        });

        monthLabel = new Label();
        monthLabel.getStyleClass().add("month-label");

        forwardMonthButton.setOnAction(t -> {
            forward(1, MONTHS, false);
        });


        yearSpinner.getStyleClass().add("spinner");

        backYearButton = new Button();
        backYearButton.getStyleClass().add("left-button");

        forwardYearButton = new Button();
        forwardYearButton.getStyleClass().add("right-button");

        StackPane leftYearArrow = new StackPane();
        leftYearArrow.getStyleClass().add("left-arrow");
        leftYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        backYearButton.setGraphic(leftYearArrow);

        StackPane rightYearArrow = new StackPane();
        rightYearArrow.getStyleClass().add("right-arrow");
        rightYearArrow.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        forwardYearButton.setGraphic(rightYearArrow);

        backYearButton.setOnAction(t -> {
            forward(-10, YEARS, false);
        });

        yearLabel = new Label();
        yearLabel.getStyleClass().add("year-label");
        yearLabel.setCursor(Cursor.HAND);

        monthLabel.setStyle("-fx-font-size : 22px;");

        yearLabel.setOnMouseClicked(event -> {

            yearLabel.setStyle("-fx-font-size : 22px;");
            monthLabel.setStyle("-fx-font-size : 12px;");

            yearLabel.setCursor(Cursor.DEFAULT);
            monthLabel.setCursor(Cursor.HAND);

            header.getChildren().clear();

            header.getChildren().addAll(monthLabel, yearLabel);
            gridPane.getChildren().clear();
            actions.getChildren().clear();

            actions.getChildren().add(yearSpinner);

            SVGPath yearLeftArrow = new SVGPath();
            yearLeftArrow.getStyleClass().add("left-arrow");
            yearLeftArrow.setContent("M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z");

            SVGPath yearRightArrow = new SVGPath();
            yearRightArrow.getStyleClass().add("right-arrow");
            yearRightArrow.setContent("M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z");

            forwardYearButton.setGraphic(yearRightArrow);
            backYearButton.setGraphic(yearLeftArrow);

//            if(!yearLabel.getText().equals(String.valueOf(LocalDate.now().getYear())))
                composeAltLayout();

        });

        monthLabel.setOnMouseClicked(event -> {
            altLayoutMonth();
        });

        forwardYearButton.setOnAction(t -> {
            forward(10, YEARS, false);
        });

        yearSpinner.getStyleClass().add("spinner");
        yearSpinner.getChildren().addAll(backYearButton, yearLabel, forwardYearButton);
        yearSpinner.setFillHeight(false);

        backMonthButton.setPadding(new Insets(10));
        forwardMonthButton.setPadding(new Insets(10));
        backYearButton.setPadding(new Insets(10));
        forwardYearButton.setPadding(new Insets(10));

        monthSpinner.getChildren().addAll(backMonthButton, forwardMonthButton);

        actions.getChildren().add(monthSpinner);

        header.getChildren().addAll(yearLabel, monthLabel);

        monthYearPane.setMinHeight(40);
        actions.setMinHeight(40);

        monthSpinner.setAlignment(Pos.BOTTOM_RIGHT);

        monthYearPane.add(header, 0,0);
        monthYearPane.add(actions, 1, 0);



//        monthYearPane.setGridLinesVisible(true);

//        monthYearPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
//        monthYearPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        header.setPrefWidth(180);
//
//        header.setMinHeight(40);
        header.setMaxHeight(40);

        return monthYearPane;
    }


    void altLayoutMonth(){
        monthLabel.setStyle("-fx-font-size : 22px;");
        yearLabel.setStyle("-fx-font-size : 12px;");

        yearLabel.setCursor(Cursor.HAND);
        monthLabel.setCursor(Cursor.DEFAULT);

        header.getChildren().clear();
        header.getChildren().addAll(yearLabel, monthLabel);

        gridPane.getChildren().clear();
        actions.getChildren().clear();

        actions.getChildren().add(monthSpinner);
        forwardMonthButton.setGraphic(rightArrow);
        backMonthButton.setGraphic(leftArrow);

        yearSpinner.setAlignment(Pos.BOTTOM_RIGHT);

        updateGrid();
    }

    private void composeAltLayout(){

        gridPane.getChildren().clear();


        LocalDate date = LocalDate.of(Integer.valueOf(yearLabel.getText()) - 10, 1,1);

            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < daysPerWeek; col++) {
                    date = date.plusYears(1);
                    Label year = new Label(String.valueOf(date.getYear()));
                    year.getStyleClass().add("years");
                    year.setPadding(new Insets(6,4, 6,4));

                    year.setOnMouseClicked(event ->{
                        if(event.getSource() instanceof Label){
                            yearLabel.setText( ((Label) event.getSource()).getText());

                            goYear(
                                    Integer.valueOf(yearLabel.getText()),
                                    false
                            );
                            altLayoutMonth();
                        }
                    });

                    year.setPrefHeight(20);
                    gridPane.add(year, col, row);
            }

                gridPane.setAlignment(Pos.CENTER);
        }
    }

    private void switchAction(){
        Timeline timeline = new Timeline();

        timeline.getKeyFrames().addAll(

        new KeyFrame(Duration.ZERO,
            new KeyValue(yearLabel.translateYProperty(), 0)),

        new KeyFrame(Duration.ZERO,
            new KeyValue(yearLabel.scaleXProperty(), yearLabel.getScaleX())),

        new KeyFrame(Duration.ZERO,
                new KeyValue(yearLabel.scaleYProperty(), yearLabel.getScaleY())),

        new KeyFrame(Duration.ZERO,
                new KeyValue(monthLabel.scaleXProperty(), monthLabel.getScaleX())),

        new KeyFrame(Duration.ZERO,
                new KeyValue(monthLabel.scaleYProperty(), monthLabel.getScaleY())),

        new KeyFrame(Duration.ZERO,
            new KeyValue(monthLabel.translateYProperty(), monthLabel.getTranslateY())),

        new KeyFrame(Duration.millis(1000),
                new KeyValue(yearLabel.translateYProperty(), 35)),

        new KeyFrame(Duration.millis(1000),
                new KeyValue(monthLabel.translateYProperty(), -20)),

        new KeyFrame(Duration.millis(100),
                new KeyValue(yearLabel.scaleYProperty(), 1.5)),

        new KeyFrame(Duration.millis(100),
                new KeyValue(yearLabel.scaleXProperty(), 1.5)),

        new KeyFrame(Duration.millis(100),
                new KeyValue(monthLabel.scaleYProperty(), 0.8)),

        new KeyFrame(Duration.millis(100),
                new KeyValue(monthLabel.scaleXProperty(), 0.8))

        );


        timeline.play();
    }

    protected void refresh() {
        updateMonthLabelWidth();
        updateDayNameCells();
        updateValues();
    }

    void updateDayNameCells() {
        // first day of week, 1 = monday, 7 = sunday
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();

        // july 13th 2009 is a Monday, so a firstDayOfWeek=1 must come out of the 13th
        LocalDate date = LocalDate.of(2009, 7, 12 + firstDayOfWeek);
        for (int i = 0; i < daysPerWeek; i++) {
            String name = weekDayNameFormatter.withLocale(getLocale()).format(date.plus(i, DAYS));
            dayNameCells.get(i).setText(titleCaseWord(name));
        }
    }

    void updateWeeknumberDateCells() {
        if (datePicker.isShowWeekNumbers()) {
            final Locale locale = getLocale();
            final int maxWeeksPerMonth = 6; // TODO: Get this from chronology?

            LocalDate firstOfMonth = displayedYearMonth.get().atDay(1);
            for (int i = 0; i < maxWeeksPerMonth; i++) {
                LocalDate date = firstOfMonth.plus(i, WEEKS);
                // Use a formatter to ensure correct localization,
                // such as when Thai numerals are required.
                String cellText =
                        weekNumberFormatter.withLocale(locale)
                                .withDecimalStyle(DecimalStyle.of(locale))
                                .format(date);
                weekNumberCells.get(i).setText(cellText);
            }
        }
    }

    private int getDaysPerWeek() {
        ValueRange range = getPrimaryChronology().range(DAY_OF_WEEK);
        return (int)(range.getMaximum() - range.getMinimum() + 1);
    }

    private int getMonthsPerYear() {
        ValueRange range = getPrimaryChronology().range(MONTH_OF_YEAR);
        return (int)(range.getMaximum() - range.getMinimum() + 1);
    }

    private void updateMonthLabelWidth() {
        if (monthLabel != null) {
            int monthsPerYear = getMonthsPerYear();
            double width = 0;
            for (int i = 0; i < monthsPerYear; i++) {
                YearMonth yearMonth = displayedYearMonth.get().withMonth(i + 1);
                String name = monthFormatterSO.withLocale(getLocale()).format(yearMonth);
                if (Character.isDigit(name.charAt(0))) {
                    // Fallback. The standalone format returned a number, so use standard format instead.
                    name = monthFormatter.withLocale(getLocale()).format(yearMonth);
                }
//                width = Math.max(width, Utils.computeTextWidth(monthLabel.getFont(), name, 0));
            }
            monthLabel.setMinWidth(width);
        }
    }

    protected void updateMonthYearPane() {
        YearMonth yearMonth = displayedYearMonth.get();
        String str = formatMonth(yearMonth);
        monthLabel.setText(str);

        str = formatYear(yearMonth);
        yearLabel.setText(str);

        yearLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue != null){
//                    displayedYearMonth.setValue(
//                            YearMonth.of(Integer.valueOf(yearLabel.getText()),
//                                    displayedYearMonth.get()
//                            )
//                    );
                    composeAltLayout();
                }
            }
        });
//        double width = Utils.computeTextWidth(yearLabel.getFont(), str, 0);
//        if (width > yearLabel.getMinWidth()) {
//            yearLabel.setMinWidth(width);
//        }

        Chronology chrono = datePicker.getChronology();
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        backMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, DAYS));
        forwardMonthButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, MONTHS));
        backYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, -1, YEARS));
        forwardYearButton.setDisable(!isValidDate(chrono, firstDayOfMonth, +1, YEARS));
    }

    private String formatMonth(YearMonth yearMonth) {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));

            String str = monthFormatterSO.withLocale(getLocale())
                    .withChronology(chrono)
                    .format(cDate);
            if (Character.isDigit(str.charAt(0))) {
                // Fallback. The standalone format returned a number, so use standard format instead.
                str = monthFormatter.withLocale(getLocale())
                        .withChronology(chrono)
                        .format(cDate);
            }
            return titleCaseWord(str);
        } catch (DateTimeException ex) {
            // Date is out of range.
            return "";
        }
    }

    private String formatYear(YearMonth yearMonth) {
        Locale locale = getLocale();
        Chronology chrono = getPrimaryChronology();
        try {
            DateTimeFormatter formatter = yearFormatter;

            ChronoLocalDate cDate = chrono.date(yearMonth.atDay(1));
            int era = cDate.getEra().getValue();
            int nEras = chrono.eras().size();

            /*if (cDate.get(YEAR) < 0) {
                formatter = yearForNegYearFormatter;
            } else */
            if ((nEras == 2 && era == 0) || nEras > 2) {
                formatter = yearWithEraFormatter;
            }

            // Fixme: Format Japanese era names with Japanese text.
            String str = formatter.withLocale(getLocale())
                    .withChronology(chrono)
                    .withDecimalStyle(DecimalStyle.of(getLocale()))
                    .format(cDate);

            return str;
        } catch (DateTimeException ex) {
            // Date is out of range.
            return "";
        }
    }

    // Ensures that month and day names are titlecased (capitalized).
    private String titleCaseWord(String str) {
        if (str.length() > 0) {
            int firstChar = str.codePointAt(0);
            if (!Character.isTitleCase(firstChar)) {
                str = new String(new int[] { Character.toTitleCase(firstChar) }, 0, 1) +
                        str.substring(Character.offsetByCodePoints(str, 0, 1));
            }
        }
        return str;
    }



    /**
     * determine on which day of week idx the first of the months is
     */
    private int determineFirstOfMonthDayOfWeek() {
        // determine with which cell to start
        int firstDayOfWeek = WeekFields.of(getLocale()).getFirstDayOfWeek().getValue();
        int firstOfMonthIdx = displayedYearMonth.get().atDay(1).getDayOfWeek().getValue() - firstDayOfWeek;
        if (firstOfMonthIdx < 0) {
            firstOfMonthIdx += daysPerWeek;
        }
        return firstOfMonthIdx;
    }

    private boolean isToday(LocalDate localDate) {
        return (localDate.equals(LocalDate.now()));
    }

    protected LocalDate dayCellDate(DateCell dateCell) {
        assert (dayCellDates != null);
        return dayCellDates[dayCells.indexOf(dateCell)];
    }

    public void goToYear(DateCell dateCell, int offset, boolean focusDayCell) {
        goToDate(dayCellDate(dateCell).withYear(offset), focusDayCell);
    }

    protected void goYear(int offset, boolean focusDayCell) {
        YearMonth yearMonth = displayedYearMonth.get();
        DateCell dateCell = lastFocusedDayCell;
        if (dateCell == null || !dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
            dateCell = findDayCellForDate(yearMonth.atDay(1));
        }
        goToYear(dateCell, offset, focusDayCell);
    }

    // public for behavior class
    public void goToDayCell(DateCell dateCell, int offset, ChronoUnit unit, boolean focusDayCell) {
        goToDate(dayCellDate(dateCell).plus(offset, unit), focusDayCell);
    }

    protected void forward(int offset, ChronoUnit unit, boolean focusDayCell) {
        YearMonth yearMonth = displayedYearMonth.get();
        DateCell dateCell = lastFocusedDayCell;
        if (dateCell == null || !dayCellDate(dateCell).getMonth().equals(yearMonth.getMonth())) {
            dateCell = findDayCellForDate(yearMonth.atDay(1));
        }
        goToDayCell(dateCell, offset, unit, focusDayCell);
    }

    // public for behavior class
    public void goToDate(LocalDate date, boolean focusDayCell) {
        if (isValidDate(datePicker.getChronology(), date)) {
            displayedYearMonth.set(YearMonth.from(date));
            if (focusDayCell) {
                findDayCellForDate(date).requestFocus();
            }
        }
    }

    // public for behavior class
    public void selectDayCell(DateCell dateCell) {
        datePicker.setValue(dayCellDate(dateCell));
        datePicker.hide();
    }

    private DateCell findDayCellForDate(LocalDate date) {
        for (int i = 0; i < dayCellDates.length; i++) {
            if (date.equals(dayCellDates[i])) {
                return dayCells.get(i);
            }
        }
        return dayCells.get(dayCells.size()/2+1);
    }

    void clearFocus() {
        LocalDate focusDate = datePicker.getValue();
        if (focusDate == null) {
            focusDate = LocalDate.now();
        }
        if (YearMonth.from(focusDate).equals(displayedYearMonth.get())) {
            // focus date
            goToDate(focusDate, true);
        } else {
            // focus month spinner (should not happen)
            backMonthButton.requestFocus();
        }

        // RT-31857
        if (backMonthButton.getWidth() == 0) {
            backMonthButton.requestLayout();
            forwardMonthButton.requestLayout();
            backYearButton.requestLayout();
            forwardYearButton.requestLayout();
        }
    }

    protected void createDayCells() {
        final EventHandler<MouseEvent> dayCellActionHandler = ev -> {
            if (ev.getButton() != MouseButton.PRIMARY) {
                return;
            }

            DateCell dayCell = (DateCell)ev.getSource();
            selectDayCell(dayCell);
            lastFocusedDayCell = dayCell;
        };

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                DateCell dayCell = createDayCell();
                dayCell.addEventHandler(MouseEvent.MOUSE_CLICKED, dayCellActionHandler);
                dayCells.add(dayCell);
            }
        }

        dayCellDates = new LocalDate[6 * daysPerWeek];
    }

    void updateGrid() {
        gridPane.getColumnConstraints().clear();
        gridPane.getChildren().clear();

        int nCols = daysPerWeek + (datePicker.isShowWeekNumbers() ? 1 : 0);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100); // Treated as weight
        for (int i = 0; i < nCols; i++) {
            gridPane.getColumnConstraints().add(columnConstraints);
        }

        for (int i = 0; i < daysPerWeek; i++) {
            gridPane.add(dayNameCells.get(i), i + nCols - daysPerWeek, 1);  // col, row
        }

        // Week number column
        if (datePicker.isShowWeekNumbers()) {
            for (int i = 0; i < 6; i++) {
                gridPane.add(weekNumberCells.get(i), 0, i + 2);  // col, row
            }
        }

        // setup: 6 rows of daysPerWeek (which is the maximum number of cells required in the worst case layout)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < daysPerWeek; col++) {
                gridPane.add(dayCells.get(row*daysPerWeek+col), col + nCols - daysPerWeek, row + 2);
            }
        }
    }

    private DateCell createDayCell() {
        DateCell cell = null;
        if (datePicker.getDayCellFactory() != null) {
            cell = datePicker.getDayCellFactory().call(datePicker);
        }
        if (cell == null) {
            cell = new DateCell();
        }

        return cell;
    }

    protected Locale getLocale() {
        return Locale.getDefault(Locale.Category.FORMAT);
    }

    /**
     * The primary chronology for display. This may be overridden to
     * be different than the DatePicker chronology. For example
     * DatePickerHijrahContent uses ISO as primary and Hijrah as a
     * secondary chronology.
     */
    protected Chronology getPrimaryChronology() {
        return datePicker.getChronology();
    }

    protected boolean isValidDate(Chronology chrono, LocalDate date, int offset, ChronoUnit unit) {
        if (date != null) {
            try {
                return isValidDate(chrono, date.plus(offset, unit));
            } catch (DateTimeException ex) {
            }
        }
        return false;
    }

    protected boolean isValidDate(Chronology chrono, LocalDate date) {
        try {
            if (date != null) {
                chrono.date(date);
            }
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }
}
