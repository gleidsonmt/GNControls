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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.time.LocalTime;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  19/04/2019
 */
public class MinutesSpinner extends VBox {

    private Label title = new Label("Min");
    private Button upButton = new Button();
    private Button bottomButton = new Button();
    private TextField editor = new TextField();

    public MinutesSpinner(){
        this.getChildren().add(title);
        this.getChildren().add(upButton);
        this.getChildren().add(editor);
        this.getChildren().add(bottomButton);

        this.title.getStyleClass().add("hour-label");

        SVGPath uArrow = new SVGPath();
        uArrow.getStyleClass().add("up-arrow");
        uArrow.setContent("M12 8l-6 6 1.41 1.41L12 10.83l4.59 4.58L18 14z");
        upButton.setGraphic(uArrow);

        SVGPath btArrow = new SVGPath();
        btArrow.getStyleClass().add("bottom-arrow");
        btArrow.setContent("M16.59 8.59L12 13.17 7.41 8.59 6 10l6 6 6-6z");
        bottomButton.setGraphic(btArrow);

        this.editor.setAlignment(Pos.CENTER);

        this.editor.setEditable(false);
        this.editor.setText("0");

        upButton.setOnMouseClicked(event -> {
            int value = Integer.valueOf(this.editor.getText());
            if(value < 59){
                this.editor.setText(String.valueOf(value + 1));
            } else {
                this.editor.setText("0");
            }
        });

        bottomButton.setOnMouseClicked(event -> {
            int value = Integer.valueOf(this.editor.getText());
            if(value > 0){
                this.editor.setText(String.valueOf(value -1));
            } else {
                this.editor.setText("59");
            }
        });

        this.setAlignment(Pos.CENTER);
    }

    TextField getEditor(){
        return this.editor;
    }

}

