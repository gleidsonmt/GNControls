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

import com.gn.behavior.GNDatePickerBehavior;
import com.gn.control.GNDatePicker;
import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/03/2019
 */
public class GNDatePickerSkin extends ComboBoxPopupControl {

    private GNDatePicker datePicker;
    private TextField displayNode;
    private GNDatePickerContent datePickerContent;

    public GNDatePickerSkin(final GNDatePicker datePicker) {
        super(datePicker, new GNDatePickerBehavior(datePicker));

        try {
            Field helper = datePicker.focusedProperty().getClass().getSuperclass().getDeclaredField("helper");
            helper.setAccessible(true);
            ExpressionHelper value = (ExpressionHelper)helper.get(datePicker.focusedProperty());
            Field changeListenersField = value.getClass().getDeclaredField("changeListeners");
            changeListenersField.setAccessible(true);
            ChangeListener[] changeListeners = (ChangeListener[])((ChangeListener[])changeListenersField.get(value));

            for(int i = changeListeners.length - 1; i > 0; --i) {
                if (changeListeners[i] != null && changeListeners[i].getClass().getName().contains("ComboBoxPopupControl")) {
                    datePicker.focusedProperty().removeListener(changeListeners[i]);
                    break;
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException var7) {
            var7.printStackTrace();
        }

        TextField textField = datePicker.getEditor();

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.isEmpty() && newValue.length() < 11){

                    String value = textField.getText();

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
                ((DatePickerEditor)datePicker.getEditor()).setFakeFocus(hasFocus);

                if (!hasFocus) {
                    setTextFromTextFieldIntoComboBoxValue();
                }
            }
        });

        this.datePicker = datePicker;

        // The "arrow" is actually a rectangular svg icon resembling a calendar.
        // Round the size of the icon to whole integers to get sharp edges.
        arrow.paddingProperty().addListener(new InvalidationListener() {
            // This boolean protects against unwanted recursion.
            private boolean rounding = false;

            @Override
            public void invalidated(Observable observable) {
                if (!rounding) {
                    Insets padding = arrow.getPadding();
                    Insets rounded = new Insets(Math.round(padding.getTop()), Math.round(padding.getRight()),
                            Math.round(padding.getBottom()), Math.round(padding.getLeft()));
                    if (!rounded.equals(padding)) {
                        rounding = true;
                        arrow.setPadding(rounded);
                        rounding = false;
                    }
                }
            }
        });

        registerChangeListener(datePicker.chronologyProperty(), "CHRONOLOGY");
        registerChangeListener(datePicker.converterProperty(), "CONVERTER");
        registerChangeListener(datePicker.dayCellFactoryProperty(), "DAY_CELL_FACTORY");
        registerChangeListener(datePicker.showWeekNumbersProperty(), "SHOW_WEEK_NUMBERS");
        registerChangeListener(datePicker.valueProperty(), "VALUE");

        getPopup().getScene().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                today(event);
            }
        });
    }

    @Override protected void handleControlPropertyChanged(String p) {

        if ("CHRONOLOGY".equals(p) ||
                "DAY_CELL_FACTORY".equals(p)) {

            updateDisplayNode();
             if (datePickerContent != null) {
                 datePickerContent.refresh();
             }
            datePickerContent = null;
            popup = null;
        } else if ("CONVERTER".equals(p)) {
            updateDisplayNode();
        } else if ("EDITOR".equals(p)) {
            getEditableInputNode();
        } else if ("SHOWING".equals(p)) {
            if (datePicker.isShowing()) {
                if (datePickerContent != null) {
                    LocalDate date = datePicker.getValue();
                    datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
                    datePickerContent.updateValues();
                }
                show();
            } else {
                hide();
            }
        } else if ("SHOW_WEEK_NUMBERS".equals(p)) {
            if (datePickerContent != null) {
                datePickerContent.updateGrid();
                datePickerContent.updateWeeknumberDateCells();
            }
        } else if ("VALUE".equals(p)) {
            updateDisplayNode();
            if (datePickerContent != null) {
                LocalDate date = datePicker.getValue();
                datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
                datePickerContent.updateValues();
            }
            datePicker.fireEvent(new ActionEvent());
        } else {
            super.handleControlPropertyChanged(p);
        }
    }

    public void today(KeyEvent event){
        if (getPopup().isShowing()) {
            if (event.getCode() == KeyCode.ENTER) {
                datePicker.setValue(LocalDate.now());
                datePicker.hide();
            }
        }
    }

    @Override
    protected Node getPopupContent() {
        if (datePickerContent == null) {
//            if (datePicker.getChronology() instanceof HijrahChronology) {
//                datePickerContent = new DatePickerH(datePicker);
//            } else {
                datePickerContent = new GNDatePickerContent(datePicker);
//            }
        }
        return datePickerContent;
    }

    @Override
    protected TextField getEditor() {
        return ((GNDatePicker)getSkinnable()).getEditor();
    }

    @Override
    protected StringConverter<LocalDate> getConverter() {
        return ((GNDatePicker)getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {

        if (displayNode == null) {
            displayNode = getEditableInputNode();
            displayNode.getStyleClass().add("date-picker-display-node");
            updateDisplayNode();
        }
        displayNode.setEditable(datePicker.isEditable());

        return displayNode;
    }

    public void syncWithAutoUpdate() {

        if (!getPopup().isShowing() && datePicker.isShowing()) {
            // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
            // Make sure DatePicker button is in sync.
            datePicker.hide();
        }
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
