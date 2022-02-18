/*
 *
 *  * Copyright (C) Gleidson Neves da Silveira
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.gleidsonmt.gncontrols;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/02/2022
 */
public class TestButton extends Application implements Theme {
    @Override
    public void start(Stage stage) throws Exception {
        GNButton button = new GNButton("Button");
        StackPane root = new StackPane();
        root.getChildren().add(button);
        stage.setScene(new Scene(root, 400, 400));
//        setTheme(stage.getScene());
//        stage.show();
//        ScenicView.show(stage.getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
