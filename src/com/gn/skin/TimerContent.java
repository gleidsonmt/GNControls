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

import com.gn.control.GNTimePicker;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  19/04/2019
 */
public class TimerContent extends VBox {

    private HourSpinner hourSpinner = new HourSpinner();
    private MinutesSpinner minutesSpinner = new MinutesSpinner();
    private SecondSpinner secondSpinner = new SecondSpinner();
    private HBox content = new HBox();
    private Button confirm = new Button("Confirm");


    public TimerContent(final GNTimePicker timePicker){

        ResourceBundle resourceBundle;

        if (Locale.getDefault().getISO3Country().equals("BRA")){
            resourceBundle = ResourceBundle.getBundle("com.gn.i18n.Lang_" + Locale.getDefault());
        } else {
            resourceBundle = ResourceBundle.getBundle("com.gn.i18n.Lang_" + Locale.ENGLISH);
        }

        hourSpinner.getTitle().setText(resourceBundle.getString("time.hour"));
        minutesSpinner.getTitle().setText(resourceBundle.getString("time.minutes"));
        secondSpinner.getTitle().setText(resourceBundle.getString("time.second"));

        getStyleClass().add("time-picker-popup");
        confirm.getStyleClass().add("confirm");
        this.getStylesheets().add(TimerContent.class.getResource("/com/gn/css/timePopup.css").toExternalForm());

        this.content.getChildren().add(hourSpinner);
        this.content.getChildren().add(minutesSpinner);
        this.content.getChildren().add(secondSpinner);
        this.content.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);

        this.getChildren().add(content);

        confirm.setPrefWidth(200);
        confirm.setPrefHeight(40);

        this.getChildren().add(confirm);

        this.content.setSpacing(10);

        this.setPrefWidth(200);

        setValue(LocalTime.of(0,0,0));

        this.secondSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            setValue(getValue().withSecond(Integer.parseInt(secondSpinner.getEditor().getText())));
        });

        this.hourSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            setValue(getValue().withHour(Integer.parseInt(hourSpinner.getEditor().getText())));
        });

        this.minutesSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            setValue(getValue().withMinute(Integer.parseInt(minutesSpinner.getEditor().getText())));
        });

        confirm.setOnMouseClicked(event -> {
            timePicker.setValue(String.valueOf(LocalTime.now()));
            timePicker.hide();
        });
    }

    public ObjectProperty<LocalTime> valueProperty() {
        return value;
    }

    private ObjectProperty<LocalTime> value = new SimpleObjectProperty<LocalTime>(this, "value");

    public final void setValue(LocalTime value) {
        valueProperty().set(value);
    }

    public final LocalTime getValue() {
        return valueProperty().get();
    }
}
