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

import io.github.gleidsonmt.gncontrols.Material;
import io.github.gleidsonmt.gncontrols.Theme;
import io.github.gleidsonmt.gncontrols.controls.*;
import io.github.gleidsonmt.gncontrols.material.icon.IconContainer;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  13/09/2022
 */
public class LoginSampleTestCustom extends Application {

    @Override
    public void start(@NotNull Stage stage) {

        VBox root = new VBox();
        root.setPadding(new Insets(20));

        ListView<Label> listContent = new ListView<>();

        Label labelF = new Label("My First Label");
        labelF.setGraphic(new IconContainer(Icons.BADGE));

        Label labelS = new Label("My Second Label");
        labelS.setGraphic(new IconContainer(Icons.PERSON_ADD));

        ObservableList<Label> labels = FXCollections.observableArrayList(
                labelF, labelS
        );

        listContent.setFixedCellSize(35);
        listContent.setItems(labels);

        GNSearchList<String> searchBox = new GNSearchList<>();

        root.setSpacing(50);

        GNAvatarStatus avatar = new GNAvatarStatus();
        avatar.setImage(new Image("avatar_purple.png", 40, 40, true, true));

        root.getChildren().addAll(searchBox, avatar);

//        stage.setResizable(false);

        Scene scene = new Scene(root, 480, 600);
        new Material(scene, Theme.SIMPLE_INFO);

        stage.setScene(scene);
        stage.show();

//        CSSFX.start(scene);
//        ScenicView.show(scene);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
