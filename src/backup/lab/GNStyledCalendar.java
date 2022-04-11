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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.time.LocalDate;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  19/12/2018
 */
public class GNStyledCalendar extends Region {

    private CalendarPane calendar = new CalendarPane();
    private ToggleGroup group = new ToggleGroup();
    private HBox base = new HBox();
    private HBox yearControl = new HBox();
    private VBox side = new VBox();
    private Button left = new Button("<");
    private Button right = new Button(">");
    private SVGPath right_arrow = new SVGPath();
    private SVGPath left_arrow = new SVGPath();
    private Label year = new Label();
    private VBox panel = new VBox();
    private Label month = new Label("Week");


    public GNStyledCalendar() {

        this.getStyleClass().add("styled-calendar");
        side.getStyleClass().add("side");
        right.getStyleClass().add("right-button");
        left.getStyleClass().add("left-button");
        year.getStyleClass().add("year");
        right_arrow.getStyleClass().add("icon");
        left_arrow.getStyleClass().add("icon");
        panel.getStyleClass().add("panel");
        month.getStyleClass().add("month");

        right_arrow.setContent("M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z");
        left_arrow.setContent("M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z");
        right.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        left.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        right.setGraphic(right_arrow);
        left.setGraphic(left_arrow);

        getChildren().add(base);
        panel.setPadding(new Insets(20, 0 ,0,0));
        panel.getChildren().addAll(month, calendar);
        base.getChildren().addAll(side, panel);
        panel.setAlignment(Pos.CENTER);
        side.setPrefSize(100, 100);
//        year.setPrefWidth(80);
//        year.setMinWidth(80);
        year.setAlignment(Pos.CENTER);

        side.setPadding(new Insets(10,0,0,0));
        side.setAlignment(Pos.TOP_LEFT);
        yearControl.setAlignment(Pos.CENTER);
        VBox.setMargin(yearControl, new Insets(0,0,20D,0));

        String[] months = calendar.getSkeleton().getMonths();

        year.setText(String.valueOf(LocalDate.now().getYear()));

        yearControl.getChildren().addAll(left, year, right);
        side.getChildren().add(yearControl);

        left.setOnMouseReleased(event -> {
            int oldYear = Integer.valueOf(year.getText());
            year.setText( String.valueOf(oldYear - 1 ));
        });

        right.setOnMouseReleased(event -> {
            int newYear = Integer.valueOf(year.getText());
            year.setText( String.valueOf(newYear + 1 ));
        });

        year.textProperty().addListener((observable, oldValue, newValue) -> {
            int year = Integer.valueOf(newValue);

            calendar.refresh(LocalDate.of(year,
                    Integer.valueOf( ((ToggleButton) group.selectedToggleProperty().get()).getId()),
                    1));
        });

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                month.setText( ((ToggleButton)newValue).getText() );
            }
        });

        for(int i = 1; i < months.length; i++){

            int finalI = i - 1;

            ToggleButton btn = new ToggleButton(months[finalI]);
            btn.setToggleGroup(group);
            btn.setId(String.valueOf(i));

            if(i == LocalDate.now().getMonthValue()){
                group.selectToggle(btn);
            }

            btn.setOnMouseReleased(event -> {
                refresh(finalI + 1);
            });
            side.getChildren().add(btn);
        }


        VBox.setVgrow(calendar, Priority.ALWAYS);
        HBox.setHgrow(panel, Priority.ALWAYS);

        for(Node node : side.getChildren()){
            if(node instanceof ToggleButton){
                ((ToggleButton) node).setMinHeight(35D);
                ((ToggleButton) node).prefWidthProperty().bind(side.widthProperty());
                ((ToggleButton) node).setAlignment(Pos.BASELINE_LEFT);
            }
        }
    }

    private void refresh(int month){
        panel.getChildren().remove(calendar);
        calendar.refresh(LocalDate.of(Integer.valueOf(year.getText()), month, LocalDate.now().getDayOfMonth()));
        panel.getChildren().add(calendar);
//        HBox.setHgrow(calendar, Priority.ALWAYS);
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
        return GNDatePicker.class.getResource("/com/gn/styledCalendar.css").toExternalForm();
    }
}
