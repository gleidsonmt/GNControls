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

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.gleidsonmt.gncontrols.Material;
import io.github.gleidsonmt.gncontrols.Theme;
import io.github.gleidsonmt.gncontrols.controls.GNSearchList;
import io.github.gleidsonmt.gncontrols.material.icon.IconContainer;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.test.layout.RootContainer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  25/09/2022
 */
public class SizeControlView extends Application {

    @Override
    public void start(Stage stage) {

        ComboBox<Label> iconButton = new ComboBox<>();

        Label labelF = new Label("My First Label");
        labelF.setGraphic(new IconContainer(Icons.BADGE));

        Label labelS = new Label("My Second Label");
        labelS.setGraphic(new IconContainer(Icons.PERSON_ADD));

        ObservableList<Label> labels = FXCollections.observableArrayList(
                labelF, labelS
        );
        iconButton.setPromptText("Item value");

        iconButton.setItems(labels);

        VBox body = new VBox(iconButton);
        body.setAlignment(Pos.CENTER);

        RootContainer container = new RootContainer(new GNSearchList<>());

        Scene scene = new Scene(container);
        new Material(scene, Theme.SIMPLE_INFO);

        stage.setScene(scene);
        stage.show();

        CSSFX.start(scene);
//        ScenicView.show(scene);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
