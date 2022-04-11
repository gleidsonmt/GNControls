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
package io.gleidsonmt.controls.skin;

import io.gleidsonmt.controls.behavior.GNDatePickerBehavior;
import io.gleidsonmt.controls.behavior.GNTimePickerBehavior;
import io.gleidsonmt.controls.control.GNDatePicker;
import io.gleidsonmt.controls.control.GNTimePicker;
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
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/03/2019
 */
public class GNTimePickerSkin extends ComboBoxPopupControl {

    private GNTimePicker timePicker;
    private TextField displayNode;
    private TimerContent timeContent;

    public GNTimePickerSkin(final GNTimePicker timePicker) {
        super(timePicker, new GNTimePickerBehavior(timePicker));

        this.timePicker = timePicker;

        timeContent = new TimerContent(timePicker);

        try {
            Field helper = timePicker.focusedProperty().getClass().getSuperclass().getDeclaredField("helper");
            helper.setAccessible(true);
            ExpressionHelper value = (ExpressionHelper)helper.get(timePicker.focusedProperty());
            Field changeListenersField = value.getClass().getDeclaredField("changeListeners");
            changeListenersField.setAccessible(true);
            ChangeListener[] changeListeners = (ChangeListener[])((ChangeListener[])changeListenersField.get(value));

            for(int i = changeListeners.length - 1; i > 0; --i) {
                if (changeListeners[i] != null && changeListeners[i].getClass().getName().contains("ComboBoxPopupControl")) {
                    timePicker.focusedProperty().removeListener(changeListeners[i]);
                    break;
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException var7) {
            var7.printStackTrace();
        }

        this.timePicker.timeProperty().bindBidirectional(timeContent.valueProperty());

        getPopup().getScene().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                today(event);
            }
        });
    }

    public void today(KeyEvent event){
        if (getPopup().isShowing()) {
            if (event.getCode() == KeyCode.ENTER) {
                timePicker.hide();
            }
        }
    }

    public void syncWithAutoUpdate() {

        if (!getPopup().isShowing() && timePicker.isShowing()) {
            // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
            // Make sure DatePicker button is in sync.
            timePicker.hide();
        }
    }
    @Override
    protected Node getPopupContent() {
        return timeContent;
    }

    @Override
    protected TextField getEditor() {
        return ((GNTimePicker)getSkinnable()).getEditor();
    }

    @Override
    protected StringConverter getConverter() {
        return ((GNTimePicker)getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        if (displayNode == null) {
            displayNode = getEditableInputNode();
            displayNode.getStyleClass().add("timer-picker-display-node");
            updateDisplayNode();
        }
        displayNode.setEditable(timePicker.isEditable());

        return displayNode;
    }

    public static final class TimePickerEditor extends TextField {
        public TimePickerEditor(){
            this.setStyle("-fx-background-color : transparent;");
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