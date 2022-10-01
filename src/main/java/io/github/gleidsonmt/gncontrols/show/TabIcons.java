/*
 *    Copyright (C) Gleidson Neves da Silveira
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.gleidsonmt.gncontrols.show;

import io.github.gleidsonmt.gncontrols.material.icon.IconContainer;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  01/01/2022
 */
public class TabIcons extends Tab {

    public TabIcons(String title) {
        setText(title);

        ScrollPane body = new ScrollPane();
        StackPane root = new StackPane(body);

        setContent(root);

        GridPane layout = new GridPane();
//        layout.setGridLinesVisible(true);

        layout.setAlignment(Pos.CENTER);

        layout.setPrefSize(300, 300);

        body.setContent(layout);
        body.setFitToWidth(true);
        body.setFitToHeight(true);

        layout.setVgap(10);
        layout.setHgap(10);

        int col = 0; // hide none option
        int row = 0;
        // 5x5

        for (Icons value : Icons.values()) {
            col++;

            VBox box = new VBox();
            box.setAlignment(Pos.CENTER);
            box.setSpacing(5);
            box.setPrefHeight(30);

            IconContainer iconContainer = new IconContainer(value);
            iconContainer.setFill(Color.web("gray"));
//            layout.getChildren().add(box);

            box.getChildren().addAll(iconContainer, new Label(value.toString()));

            layout.getChildren().addAll(box);

            GridPane.setConstraints(box, col, row, 1,1);

            if (col >= 5) {
                col = 0;
                row++;
            }
        }
    }


}
