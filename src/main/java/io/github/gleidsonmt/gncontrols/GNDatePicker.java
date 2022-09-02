/*
 *
 *  * Copyright (C) Gleidson Neves da Silveira
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.options.FieldType;
import io.github.gleidsonmt.gncontrols.skin.GNDatePickerSkin;
import io.github.gleidsonmt.gncontrols.skin.TextBox;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  24/07/2022
 */
@DefaultProperty("control")
public class GNDatePicker extends TextBox implements GNComponent {

    private LocalDate lastValidDate = null;
    private Chronology lastValidChronology = IsoChronology.INSTANCE;

    public GNDatePicker() {
        this(FieldType.OUTLINED, null);
    }

    public GNDatePicker(FieldType fieldType, String text) {

//        setLeadIcon(Icons.CONTACT);
        getStyleClass().add("gn-text-box");
        getStyleClass().add("gn-date-picker");

        setEditor(new TextField(){
            @Override
            public void paste() {
                createPasteAction(this);
            }
        });

        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> setFocused(newValue));
        super.setFieldType(fieldType);

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

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNDatePickerSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getControlStylesheet();
    }

    /* *************************************************************************
     *                                                                         *
     * Properties                                                                 *
     *                                                                         *
     **************************************************************************/


    /**
     * A custom cell factory can be provided to customize individual
     * day cells in the DatePicker popup. Refer to {@link DateCell}
     * and {@link Cell} for more information on cell factories.
     * Example:
     *
     * <pre><code>
     * final Callback&lt;DatePicker, DateCell&gt; dayCellFactory = new Callback&lt;DatePicker, DateCell&gt;() {
     *     public DateCell call(final DatePicker datePicker) {
     *         return new DateCell() {
     *             &#064;Override public void updateItem(LocalDate item, boolean empty) {
     *                 super.updateItem(item, empty);
     *
     *                 if (MonthDay.from(item).equals(MonthDay.of(9, 25))) {
     *                     setTooltip(new Tooltip("Happy Birthday!"));
     *                     setStyle("-fx-background-color: #ff4444;");
     *                 }
     *                 if (item.equals(LocalDate.now().plusDays(1))) {
     *                     // Tomorrow is too soon.
     *                     setDisable(true);
     *                 }
     *             }
     *         };
     *     }
     * };
     * datePicker.setDayCellFactory(dayCellFactory);
     * </code></pre>
     *
     * @defaultValue null
     */
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

    // --- value
    /**
     * The value of this ComboBox is defined as the selected item if the input
     * is not editable, or if it is editable, the most recent user action:
     * either the value input by the user, or the last selected item.
     * @return the value property
     */
    public ObjectProperty<LocalDate> valueProperty() { return value; }
    private ObjectProperty<LocalDate> value = new SimpleObjectProperty<>(this, "value");

    public final void setValue(LocalDate value) { valueProperty().set(value); }
    public final LocalDate getValue() { return valueProperty().get(); }

    /**
     * The calendar system used for parsing, displaying, and choosing
     * dates in the DatePicker control.
     *
     * <p>The default value is returned from a call to
     * {@code Chronology.ofLocale(Locale.getDefault(Locale.Category.FORMAT))}.
     * The default is usually {@link java.time.chrono.IsoChronology} unless
     * provided explicitly in the {@link java.util.Locale} by use of a
     * Locale calendar extension.
     *
     * Setting the value to <code>null</code> will restore the default
     * chronology.
     * @return a property representing the Chronology being used
     */
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

    // --- string converter
    /**
     * Converts the input text to an object of type LocalDate and vice
     * versa.
     *
     * <p>If not set by the application, the DatePicker skin class will
     * set a converter based on a {@link java.time.format.DateTimeFormatter}
     * for the current {@link java.util.Locale} and
     * {@link #chronologyProperty() chronology}. This formatter is
     * then used to parse and display the current date value.
     *
     * Setting the value to <code>null</code> will restore the default
     * converter.
     *
     * <p>Example using an explicit formatter:
     * <pre><code>
     * datePicker.setConverter(new StringConverter&lt;LocalDate&gt;() {
     *     String pattern = "yyyy-MM-dd";
     *     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
     *
     *     {
     *         datePicker.setPromptText(pattern.toLowerCase());
     *     }
     *
     *     {@literal @Override public String toString(LocalDate date) {
     *         if (date != null) {
     *             return dateFormatter.format(date);
     *         } else {
     *             return "";
     *         }
     *     }}
     *
     *     {@literal @Override public LocalDate fromString(String string) {
     *         if (string != null && !string.isEmpty()) {
     *             return LocalDate.parse(string, dateFormatter);
     *         } else {
     *             return null;
     *         }
     *     }}
     * });
     * </code></pre>
     * <p>Example that wraps the default formatter and catches parse exceptions:
     * <pre><code>
     *   final StringConverter&lt;LocalDate&gt; defaultConverter = datePicker.getConverter();
     *   datePicker.setConverter(new StringConverter&lt;LocalDate&gt;() {
     *       &#064;Override public String toString(LocalDate value) {
     *           return defaultConverter.toString(value);
     *       }
     *
     *       &#064;Override public LocalDate fromString(String text) {
     *           try {
     *               return defaultConverter.fromString(text);
     *           } catch (DateTimeParseException ex) {
     *               System.err.println("HelloDatePicker: "+ex.getMessage());
     *               throw ex;
     *           }
     *       }
     *   });
     * </code></pre>
     *
     * <p>The default base year for parsing input containing only two digits for
     * the year is 2000 (see {@link java.time.format.DateTimeFormatter}).  This
     * default is not useful for allowing a person's date of birth to be typed.
     * The following example modifies the converter's fromString() method to
     * allow a two digit year for birth dates up to 99 years in the past.
     * <pre><code>
     *   {@literal @Override public LocalDate fromString(String text) {
     *       if (text != null && !text.isEmpty()) {
     *           Locale locale = Locale.getDefault(Locale.Category.FORMAT);
     *           Chronology chrono = datePicker.getChronology();
     *           String pattern =
     *               DateTimeFormatterBuilder.getLocalizedDateTimePattern(FormatStyle.SHORT,
     *                                                                    null, chrono, locale);
     *           String prePattern = pattern.substring(0, pattern.indexOf("y"));
     *           String postPattern = pattern.substring(pattern.lastIndexOf("y")+1);
     *           int baseYear = LocalDate.now().getYear() - 99;
     *           DateTimeFormatter df = new DateTimeFormatterBuilder()
     *                       .parseLenient()
     *                       .appendPattern(prePattern)
     *                       .appendValueReduced(ChronoField.YEAR, 2, 2, baseYear)
     *                       .appendPattern(postPattern)
     *                       .toFormatter();
     *           return LocalDate.from(chrono.date(df.parse(text)));
     *       } else {
     *           return null;
     *       }
     *   }}
     * </code></pre>
     *
     * @return the property representing the current LocalDate string converter
     * @see javafx.scene.control.ComboBox#converterProperty
     */
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




}
