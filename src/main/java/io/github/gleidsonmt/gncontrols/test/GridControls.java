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
package io.github.gleidsonmt.gncontrols.test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  01/01/2022
 */
public class GridControls extends GridPane {

    private final ObjectProperty<ObservableList<Item>> items
            = new SimpleObjectProperty<>(this, "items", FXCollections.observableArrayList());

    public GridControls() {

        setPadding(new Insets(20));
        setHgap(30);
        setVgap(30);

//        setGridLinesVisible(true);

        items.addListener((observable, oldValue, newValue) -> {

            int row = 0;
            int col = 0;

            for (Item item : newValue) {

                if (col == 3) {
                    col = 0;
                    row += 1;
                }

                getChildren().add(item);
                GridPane.setConstraints(item, col,row,1,1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);

                col++;
            }
        });


//        widthProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                System.out.println("newValue = " + newValue);
//            }
//        });
    }

    public ObjectProperty<ObservableList<Item>> itemsProperty() {
        return items;
    }
}
