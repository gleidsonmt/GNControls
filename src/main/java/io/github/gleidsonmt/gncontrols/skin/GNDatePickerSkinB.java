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
package io.github.gleidsonmt.gncontrols.skin;

import io.github.gleidsonmt.gncontrols.GNDatePickerB;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/03/2019
 */
public class GNDatePickerSkinB extends DatePickerSkin {

    private GNDatePickerB datePicker;
    private TextField displayNode;
    private GNDatePickerContent datePickerContent;

    public GNDatePickerSkinB(final GNDatePickerB control) {
        super(control);

        this.datePicker = control;

        // install default input map for the control
//        this.behavior = new DatePickerBehavior(control);
//        control.setInputMap(behavior.getInputMap());

        // The "arrow" is actually a rectangular svg icon resembling a calendar.
        // Round the size of the icon to whole integers to get sharp edges.
//        arrow.paddingProperty().addListener(new InvalidationListener() {
//            // This boolean protects against unwanted recursion.
//            private boolean rounding = false;
//            @Override public void invalidated(Observable observable) {
//                if (!rounding) {
//                    Insets padding = arrow.getPadding();
//                    Insets rounded = new Insets(Math.round(padding.getTop()), Math.round(padding.getRight()),
//                            Math.round(padding.getBottom()), Math.round(padding.getLeft()));
//                    if (!rounded.equals(padding)) {
//                        rounding = true;
//                        arrow.setPadding(rounded);
//                        rounding = false;
//                    }
//                }
//            }
//        });

        registerChangeListener(control.chronologyProperty(), e -> {
//            updateDisplayNode();
            datePickerContent = null;
//            popup = null;
        });
//        registerChangeListener(control.converterProperty(), e -> updateDisplayNode());
        registerChangeListener(control.dayCellFactoryProperty(), e -> {
//            updateDisplayNode();
            datePickerContent = null;
//            popup = null;
        });
        registerChangeListener(control.showWeekNumbersProperty(), e -> {
            if (datePickerContent != null) {
                datePickerContent.updateGrid();
                datePickerContent.updateWeeknumberDateCells();
            }
        });
        registerChangeListener(control.valueProperty(), e -> {
//            updateDisplayNode();
            if (datePickerContent != null) {
                LocalDate date = control.getValue();
                datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
                datePickerContent.updateValues();
            }
            control.fireEvent(new ActionEvent());
        });
        registerChangeListener(control.showingProperty(), e -> {
            if (control.isShowing()) {
                if (datePickerContent != null) {
                    LocalDate date = control.getValue();
                    datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
                    datePickerContent.updateValues();
                }
                show();
            } else {
                hide();
            }
        });

        if (control.isShowing()) {
            show();
        }

        TextField textField = datePicker.getEditor();

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty() && newValue.length() < 11){

                    String value = textField.getText();

                    System.out.println("locale = " + Locale.getDefault());
                    if(Locale.getDefault().toString().equals("pt_BR")){
                        value = value.replaceAll("[^0-9]", "");  // Troque tudo que não for numero por ""
                        value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2"); // \\d ocorrencia de um digito
                        value = value.replaceFirst("(\\d{2})\\/(\\d{2})(\\d)", "$1/$2/$3");
                        textField.setText(value);
                    }
                }
            }
        });

        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || newValue.length() > 10) {
                textField.setText(oldValue);
            }
        });


        datePicker.focusedProperty().addListener((ov, t, hasFocus) -> {
            if (getEditor() != null) {
//                ((DatePickerEditor)datePicker.getEditor()).setFakeFocus(hasFocus);

                if (!hasFocus) {
//                    setTextFromTextFieldIntoComboBoxValue();
                }
            }
        });

        this.datePicker = datePicker;

        // The "arrow" is actually a rectangular svg icon resembling a calendar.
        // Round the size of the icon to whole integers to get sharp edges.
//        arrow.paddingProperty().addListener(new InvalidationListener() {
//            // This boolean protects against unwanted recursion.
//            private boolean rounding = false;
//
//            @Override
//            public void invalidated(Observable observable) {
//                if (!rounding) {
//                    Insets padding = arrow.getPadding();
//                    Insets rounded = new Insets(Math.round(padding.getTop()), Math.round(padding.getRight()),
//                            Math.round(padding.getBottom()), Math.round(padding.getLeft()));
//                    if (!rounded.equals(padding)) {
//                        rounding = true;
//                        arrow.setPadding(rounded);
//                        rounding = false;
//                    }
//                }
//            }
//        });

//        registerChangeListener(datePicker.chronologyProperty(), "CHRONOLOGY");
//        registerChangeListener(datePicker.converterProperty(), "CONVERTER");
//        registerChangeListener(datePicker.dayCellFactoryProperty(), "DAY_CELL_FACTORY");
//        registerChangeListener(datePicker.showWeekNumbersProperty(), "SHOW_WEEK_NUMBERS");
//        registerChangeListener(datePicker.valueProperty(), "VALUE");

//        getPopup().getScene().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                today(event);
//            }
//        });
    }

    protected void handleControlPropertyChanged(String p) {

//        if ("CHRONOLOGY".equals(p) ||
//                "DAY_CELL_FACTORY".equals(p)) {
//
//            updateDisplayNode();
//             if (datePickerContent != null) {
//                 datePickerContent.refresh();
//             }
//            datePickerContent = null;
//            popup = null;
//        } else if ("CONVERTER".equals(p)) {
//            updateDisplayNode();
//        } else if ("EDITOR".equals(p)) {
//            getEditableInputNode();
//        } else if ("SHOWING".equals(p)) {
//            if (datePicker.isShowing()) {
//                if (datePickerContent != null) {
//                    LocalDate date = datePicker.getValue();
//                    datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
//                    datePickerContent.updateValues();
//                }
//                show();
//            } else {
//                hide();
//            }
//        } else if ("SHOW_WEEK_NUMBERS".equals(p)) {
//            if (datePickerContent != null) {
//                datePickerContent.updateGrid();
//                datePickerContent.updateWeeknumberDateCells();
//            }
//        } else if ("VALUE".equals(p)) {
//            updateDisplayNode();
//            System.out.println("Locale = " + Locale.getDefault());
//            if (datePickerContent != null) {
//                LocalDate date = datePicker.getValue();
//                datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
//                datePickerContent.updateValues();
//            }
//            datePicker.fireEvent(new ActionEvent());
//        } else {
//            super.handleControlPropertyChanged(p);
//        }
    }

    public void today(KeyEvent event){
//        if (getPopup().isShowing()) {
//            if (event.getCode() == KeyCode.ENTER) {
//                datePicker.setValue(LocalDate.now());
//                datePicker.hide();
//            }
//        }
    }

    @Override
    public Node getPopupContent() {
        if (datePickerContent == null) {
//            if (datePicker.getChronology() instanceof HijrahChronology) {
//                datePickerContent = new DatePickerH(datePicker);
//            } else {
//            datePickerContent = new GNDatePickerContent(datePicker);
//            }
        }
        return datePickerContent;
    }

    @Override
    protected TextField getEditor() {
        return ((GNDatePickerB)getSkinnable()).getEditor();
    }

    @Override
    protected StringConverter<LocalDate> getConverter() {
        return ((GNDatePickerB)getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {

        if (displayNode == null) {
            displayNode = getEditor();
//            displayNode.getStyleClass().add("date-picker-display-node");
//            updateDisplayNode();
        }
        displayNode.setEditable(datePicker.isEditable());

        return displayNode;
    }

    public void syncWithAutoUpdate() {

//        if (!getPopup().isShowing() && datePicker.isShowing()) {
//            // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
//            // Make sure DatePicker button is in sync.
//            datePicker.hide();
//        }
    }

    public static final class DatePickerEditor extends TextField {

//        public DatePickerEditor(){
//            this.getStylesheets().add(GNDatePicker.class.getResource("/com/gn/css/simple.css").toExternalForm());
//        }

        @Override
        public String getText(int start, int end) {
            if (start > end) {
                return getContent().get(start - 1, end);
            }

            if (start < 0
                    || end > getLength()) {
                throw new IndexOutOfBoundsException();
            }

            return getContent().get(start, end);
        }

        @Override public void requestFocus() {
            if (getParent() != null) {
                getParent().requestFocus();
            }
        }

        public void setFakeFocus(boolean b) {
            setFocused(b);
        }

        @Override
        public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
            switch (attribute) {
                case FOCUS_ITEM:
                    /* Internally comboBox reassign its focus the text field.
                     * For the accessibility perspective it is more meaningful
                     * if the focus stays with the comboBox control.
                     */
                    return getParent();
                default: return super.queryAccessibleAttribute(attribute, parameters);
            }
        }
    }
}
