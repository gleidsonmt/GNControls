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
package io.gleidsonmt.controls.test;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  01/01/2022
 */
public class TabControls extends Tab {

    private ScrollPane scrollPane = new ScrollPane();
    private GridControls gridControls = new GridControls();

    public TabControls(String title) {
        setText(title);
        setContent(scrollPane);
        scrollPane.setContent(gridControls);

        gridControls.setStyle("-fx-background-color : white;");

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

    }

    public GridControls getGridControls() {
        return gridControls;
    }


}
