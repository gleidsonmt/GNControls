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
package com.gn.control;

import com.gn.skin.GNDatePickerSkin;
import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.resources.ControlResources;
import javafx.beans.property.*;
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
import javafx.util.converter.LocalDateStringConverter;

import java.time.DateTimeException;
import java.time.LocalDate;
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
public class GNDatePicker extends ComboBoxBase<LocalDate> {

    private LocalDate lastValidDate = null;
    private Chronology lastValidChronology = IsoChronology.INSTANCE;

    private static final String DEFAULT_STYLE_CLASS = "gn-date-picker";

    public GNDatePicker() {
        this(null);

        valueProperty().addListener(observable -> {
            LocalDate date = getValue();
            Chronology chrono = getChronology();

            if (validateDate(chrono, date)) {
                lastValidDate = date;
            } else {
                System.err.println("Restoring value to " +
                        ((lastValidDate == null) ? "null" : getConverter().toString(lastValidDate)));
                setValue(lastValidDate);
            }
        });

        chronologyProperty().addListener(observable -> {
            LocalDate date = getValue();
            Chronology chrono = getChronology();

            if (validateDate(chrono, date)) {
                lastValidChronology = chrono;
                defaultConverter = new LocalDateStringConverter(FormatStyle.SHORT, null, chrono);
            } else {
                System.err.println("Restoring value to " + lastValidChronology);
                setChronology(lastValidChronology);
            }
        });
    }

    public GNDatePicker(LocalDate localDate) {

        this.getStylesheets().add(GNDatePicker.class.getResource("/com/gn/css/simple.css").toExternalForm());

        setValue(localDate);
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.DATE_PICKER);
        setEditable(true);
    }

    private boolean validateDate(Chronology chrono, LocalDate date) {
        try {
            if (date != null) {
                chrono.date(date);
            }
            return true;
        } catch (DateTimeException ex) {
            System.err.println(ex);
            return false;
        }
    }

    private ObjectProperty<Callback<GNDatePicker, DateCell>> dayCellFactory;

    public final void setDayCellFactory(Callback<GNDatePicker, DateCell> value) {
        dayCellFactoryProperty().set(value);
    }

    public final Callback<GNDatePicker, DateCell> getDayCellFactory() {
        return (dayCellFactory != null) ? dayCellFactory.get() : null;
    }
    public final ObjectProperty<Callback<GNDatePicker, DateCell>> dayCellFactoryProperty() {
        if (dayCellFactory == null) {
            dayCellFactory = new SimpleObjectProperty<Callback<GNDatePicker, DateCell>>(this, "dayCellFactory");
        }
        return dayCellFactory;
    }

    public final ObjectProperty<Chronology> chronologyProperty() {
        return chronology;
    }
    private ObjectProperty<Chronology> chronology =
            new SimpleObjectProperty<Chronology>(this, "chronology", null);
    public final Chronology getChronology() {
        Chronology chrono = chronology.get();
        if (chrono == null) {
            try {
                chrono = Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT));
            } catch (Exception ex) {
                System.err.println(ex);
            }
            if (chrono == null) {
                chrono = IsoChronology.INSTANCE;
            }
            //System.err.println(chrono);
        }
        return chrono;
    }
    public final void setChronology(Chronology value) {
        chronology.setValue(value);
    }

    public final BooleanProperty showWeekNumbersProperty() {
        if (showWeekNumbers == null) {
            String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
            boolean localizedDefault =
                    (!country.isEmpty() &&
                            ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country));
            showWeekNumbers = new StyleableBooleanProperty(localizedDefault) {
                @Override public CssMetaData<GNDatePicker,Boolean> getCssMetaData() {
                    return GNDatePicker.StyleableProperties.SHOW_WEEK_NUMBERS;
                }

                @Override public Object getBean() {
                    return GNDatePicker.this;
                }

                @Override public String getName() {
                    return "showWeekNumbers";
                }
            };
        }
        return showWeekNumbers;
    }

    private BooleanProperty showWeekNumbers;
    public final void setShowWeekNumbers(boolean value) {
        showWeekNumbersProperty().setValue(value);
    }
    public final boolean isShowWeekNumbers() {
        return showWeekNumbersProperty().getValue();
    }

    public final ObjectProperty<StringConverter<LocalDate>> converterProperty() { return converter; }
    private ObjectProperty<StringConverter<LocalDate>> converter =
            new SimpleObjectProperty<StringConverter<LocalDate>>(this, "converter", null);
    public final void setConverter(StringConverter<LocalDate> value) { converterProperty().set(value); }
    public final StringConverter<LocalDate> getConverter() {
        StringConverter<LocalDate> converter = converterProperty().get();
        if (converter != null) {
            return converter;
        } else {
            return defaultConverter;
        }
    }

    // Create a symmetric (format/parse) converter with the default locale.
    private StringConverter<LocalDate> defaultConverter =
            new LocalDateStringConverter(FormatStyle.SHORT, null, getChronology());

    private ReadOnlyObjectWrapper<TextField> editor;

    public final TextField getEditor() {
        return editorProperty().get();
    }

    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (editor == null) {
            editor = new ReadOnlyObjectWrapper<TextField>(this, "editor");
            editor.set(new GNDatePickerSkin.DatePickerEditor());
        }
        return editor.getReadOnlyProperty();
    }

    private static class StyleableProperties {

        private static final String country =
                Locale.getDefault(Locale.Category.FORMAT).getCountry();

        private static final CssMetaData<GNDatePicker, Boolean> SHOW_WEEK_NUMBERS =
                new CssMetaData<GNDatePicker, Boolean>("-fx-show-week-numbers",
                        BooleanConverter.getInstance(),
                        (!country.isEmpty() &&
                                ControlResources.getNonTranslatableString("DatePicker.showWeekNumbers").contains(country))) {
                    @Override public boolean isSettable(GNDatePicker n) {
                        return n.showWeekNumbers == null || !n.showWeekNumbers.isBound();
                    }

                    @Override public StyleableProperty<Boolean> getStyleableProperty(GNDatePicker n) {
                        return (StyleableProperty<Boolean>)(WritableValue<Boolean>)n.showWeekNumbersProperty();
                    }
                };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
            Collections.addAll(styleables,
                    SHOW_WEEK_NUMBERS
            );
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }


    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return GNDatePicker.StyleableProperties.STYLEABLES;
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }


    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case DATE: return getValue();
            case TEXT: {
                String accText = getAccessibleText();
                if (accText != null && !accText.isEmpty()) return accText;

                LocalDate date = getValue();
                StringConverter<LocalDate> c = getConverter();
                if (date != null && c != null) {
                    return c.toString(date);
                }
                return "";
            }
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override protected Skin<?> createDefaultSkin() {
        return new GNDatePickerSkin(this);
    }

//    @Override
//    public String getUserAgentStylesheet() {
////        return GNDatePicker.class.getResource("/com/gn/css/datePicker.css").toExternalForm();
//        return GNDatePicker.class.getResource("/com/gn/css/simple.css").toExternalForm();
//    }
}
