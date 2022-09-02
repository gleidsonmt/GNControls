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
package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.skin.GNDatePickerSkinB;
import javafx.scene.AccessibleRole;
import javafx.scene.control.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/03/2019
 */
public class GNDatePickerB extends DatePicker {

    private LocalDate lastValidDate = null;
    private Chronology lastValidChronology = IsoChronology.INSTANCE;

    private static final String DEFAULT_STYLE_CLASS = "gn-date-picker";

    public GNDatePickerB() {
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
//                defaultConverter = new LocalDateStringConverter(FormatStyle.SHORT, null, chrono);
            } else {
                System.err.println("Restoring value to " + lastValidChronology);
                setChronology(lastValidChronology);
            }
        });
    }

    public GNDatePickerB(LocalDate localDate) {

//        this.getStylesheets().add(GNDatePicker.class.getResource("/com/gn/css/simple.css").toExternalForm());

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



    @Override protected Skin<?> createDefaultSkin() {
        return new GNDatePickerSkinB(this);
    }

//    @Override
//    public String getUserAgentStylesheet() {
////        return GNDatePicker.class.getResource("/com/gn/css/datePicker.css").toExternalForm();
//        return GNDatePicker.class.getResource("/com/gn/css/simple.css").toExternalForm();
//    }
}
