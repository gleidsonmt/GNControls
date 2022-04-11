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

import io.github.gleidsonmt.gncontrols.material.icon.IconContainer;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.options.GNButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  24/02/2022
 */
public class GNFloatingButton extends GNButton {

    public GNFloatingButton() {
        super(null);
        setText(null);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setButtonType(GNButtonType.ROUNDED);
        getStyleClass().add("gn-floating-button");
        setRippleFill(Color.WHITE);
        setGraphic(new IconContainer(Icons.FAVORITE));
        setPrefSize(50, 50);

    }

}
