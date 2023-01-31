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

package io.github.gleidsonmt.gncontrols.controls;

import io.github.gleidsonmt.gncontrols.GNButtonSkin;
import javafx.beans.DefaultProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  29/09/2022
 */
@DefaultProperty("control")
public class GNButton extends Button {

    public GNButton() {
        this(null);
    }

    public GNButton(String text) {
        setText(text == null ? "Button" : text);
        setPrefSize(100, 40);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNButtonSkin(this);
    }


}