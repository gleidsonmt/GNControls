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
package io.gleidsonmt.controls.control;

import com.gn.skin.GNDatePickerSkin;
import com.gn.skin.GNTimePickerSkin;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import javafx.util.converter.LocalTimeStringConverter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/03/2019
 */
public class GNTimePicker extends ComboBoxBase<String> {

    private static final String DEFAULT_STYLE_CLASS = "gn-time-picker";

    public GNTimePicker() {
        this.getStylesheets().add(GNTimePicker.class.getResource("/com/gn/css/simple.css").toExternalForm());
        getStyleClass().add(DEFAULT_STYLE_CLASS);

        timeProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                getEditor().setText(String.valueOf(newValue));
            }
        });

        getEditor().setText("00:00");
    }

    private ReadOnlyObjectWrapper<TextField> editor;

    public final TextField getEditor() {
        return editorProperty().get();
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (editor == null) {
            editor = new ReadOnlyObjectWrapper<TextField>(this, "editor");
            editor.set(new GNTimePickerSkin.TimePickerEditor());
        }
        return editor.getReadOnlyProperty();
    }

    public final ObjectProperty<StringConverter<LocalTime>> converterProperty() { return converter; }
    private ObjectProperty<StringConverter<LocalTime>> converter =
            new SimpleObjectProperty<StringConverter<LocalTime>>(this, "converter", null);
    public final void setConverter(StringConverter<LocalTime> value) { converterProperty().set(value); }
    public final StringConverter<LocalTime> getConverter() {
        StringConverter<LocalTime> converter = converterProperty().get();
        if (converter != null) {
            return converter;
        } else {
            return defaultConverter;
        }
    }
    private StringConverter<LocalTime> defaultConverter =
            new LocalTimeStringConverter();

    @Override protected Skin<?> createDefaultSkin() {
        return new GNTimePickerSkin(this);
    }


    public ObjectProperty<LocalTime> timeProperty() { return time; }
    private ObjectProperty<LocalTime> time = new ObjectPropertyBase<LocalTime>() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "time";
        }
    };

    public final void setTime(LocalTime time) {
        timeProperty().set(time);
    }
    public final LocalTime getTime() { return timeProperty().get(); }

//    @Override
//    public String getUserAgentStylesheet() {
////        return GNDatePicker.class.getResource("/com/gn/css/datePicker.css").toExternalForm();
//        return GNDatePicker.class.getResource("/com/gn/css/simple.css").toExternalForm();
//    }
}
