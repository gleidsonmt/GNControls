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
package io.gleidsonmt.controls;

import com.gn.control.GNComboFilter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  10/02/2020
 */
public class TestFxmlController implements Initializable  {

    @FXML private GNComboFilter comboFilter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FilteredList<String> fullList = new FilteredList<>(FXCollections.<String>observableArrayList(
                "Jorge",
                "Luiza", "Paulo", "Jorge", "Andre"),
                p -> true
        );
//
//
        comboFilter.setItems(fullList);

        comboFilter.valueProperty().addListener((observable, oldValue, newValue) -> {

        });
    }
}
