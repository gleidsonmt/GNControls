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

package io.github.gleidsonmt.gncontrols.test.controllers;

import io.github.gleidsonmt.gncontrols.controls.GNSearchList;
import io.github.gleidsonmt.gncontrols.controls.text_box.Editor;
import io.github.gleidsonmt.gncontrols.controls.text_box.FloatEditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  30/09/2022
 */
public class SimpleController implements Initializable {

    @FXML
    private GNSearchList<SearchItem> searchBox;

    @FXML
    private ComboBox<Label> comboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        SearchItem item1 = new SearchItem("Java");
        SearchItem item2 = new SearchItem("Kotlin");
        SearchItem item3 = new SearchItem("JavaScript");


        ObservableList<SearchItem> labels = FXCollections.observableArrayList(
                item1, item2, item3
        );

        FilteredList<SearchItem> filteredList = new FilteredList<>(labels, p -> true);

        searchBox.setItems(filteredList);

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(p -> p.toString().contains(newValue));
        });


    }

    @FXML
    private void getValue() {
        System.out.println("searchBox.getValue() = " + searchBox.getValue());
    }

}

record SearchItem (String name) {

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}