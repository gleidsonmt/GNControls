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
package io.github.gleidsonmt.gncontrols.test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  01/01/2022
 */
public class RootLayout extends StackPane {

    private final ObjectProperty<ObservableList<Item>> items
            = new SimpleObjectProperty<>(this, "items", FXCollections.observableArrayList());

    public RootLayout() {
        setPrefWidth(1336);
        setPrefHeight(720);

        TabLayout tabLayout = new TabLayout();
        getChildren().add(tabLayout);

        items.bindBidirectional(tabLayout.getTabControls().getGridControls().itemsProperty());
    }

    public final void setItems(ObservableList<Item> value) {
        itemsProperty().set(value);
    }

    public ObjectProperty<ObservableList<Item>> itemsProperty() {
        return items;
    }

}
