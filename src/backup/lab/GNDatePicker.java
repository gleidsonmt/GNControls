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
package io.gleidsonmt.controls.lab;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  12/12/2018
 */
public class GNDatePicker extends Region {

    private CalendarPane tile = new CalendarPane();
    private VBox base = new VBox();

    private VBox content = new VBox();
    private HBox header = new HBox();
    private HBox footer = new HBox();

    public GNDatePicker(){
        this(Locale.getDefault());
    }

    public GNDatePicker(Locale locale){
       this(LocalDate.now(), locale);
    }

    public GNDatePicker(LocalDate date, Locale locale){




//        Label title = new Label(
//                date.getMonth().getDisplayName(TextStyle.FULL, locale) + ", " +
//                        LocalDate.now().getYear()
//
//        );



        Label title = new Label(
//                date.getMonth().getDisplayName(TextStyle.FULL, locale) + ", " +
//                        LocalDate.now().getYear()
                    "teste"
        );

//        Label footer = new Label(
//                String.valueOf(date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, locale))
//                String.valueOf(LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())) // use for specific location
//        );

//        footer.setMinHeight(40D);
//        title.setMinHeight(40D);
//        month.setMinWidth(40D);
//
//        title.getStyleClass().add("title");
//        year.getStyleClass().add("year");
//        footer.getStyleClass().add("footer");

        VBox info = new VBox();
        info.setPadding(new Insets(10));

        Label year = new Label(String.valueOf(date.getYear()));
        Label month = new Label(String.valueOf(date.getMonth()));

//        Label week = new Label(String.valueOf(date.getDayOfWeek()));

        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");

        footer.setAlignment(Pos.CENTER_RIGHT);


        footer.getChildren().addAll(ok, cancel);
        footer.getStyleClass().add("actions");

        footer.setPrefHeight(46);

        info.getChildren().addAll(year, month);

        header.getChildren().addAll(info);

        content.getChildren().addAll(header, tile);
        base.getChildren().add(content);
        base.getChildren().add(tile);
        base.getChildren().add(footer);

        setPrefSize(200, 200);
        getChildren().add(base);

        base.getStyleClass().add("calendar-tile");
        year.getStyleClass().add("year");
        month.getStyleClass().add("month");
        footer.getStyleClass().add("footer");

        base.setAlignment(Pos.CENTER);

        VBox.setVgrow(title, Priority.ALWAYS);
        VBox.setVgrow(year, Priority.ALWAYS);
        VBox.setVgrow(month, Priority.ALWAYS);
        VBox.setVgrow(tile, Priority.ALWAYS);
//        VBox.setVgrow(footer, Priority.ALWAYS);
    }

    public void refresh(LocalDate date){
        tile.refresh(date);
    }

    @Override
    public double computeMinWidth(double height) {
        return base.minWidth(height);
    }

    @Override
    public double computeMinHeight(double width) {
        return base.minHeight(width);
    }

    @Override
    public double computePrefWidth(double height) {
        return base.prefWidth(height) + snappedLeftInset() + snappedRightInset();
    }

    @Override
    public double computePrefHeight(double width) {
        return base.prefHeight(width) + snappedTopInset() + snappedBottomInset();
    }

    @Override
    public void layoutChildren() {
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = getWidth() - (snappedLeftInset() + snappedRightInset());
        final double h = getHeight() - (snappedTopInset() + snappedBottomInset());
        base.resizeRelocate(x, y, w, h);
    }

    @Override
    public String getUserAgentStylesheet() {
        return GNDatePicker.class.getResource("/com/gn/lab/datePicker.css").toExternalForm();
    }
}
