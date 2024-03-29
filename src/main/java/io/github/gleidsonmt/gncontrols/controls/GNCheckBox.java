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

import io.github.gleidsonmt.gncontrols.skin.GNCheckBoxSkin;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Skin;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  26/09/2022
 */
public class GNCheckBox extends CheckBox {

    public GNCheckBox() {
        this(null);
    }

    public GNCheckBox(String text) {
        if (text == null || text.isEmpty()) setText("CheckBox");
        getStyleClass().add("gn-check-box");

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNCheckBoxSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(getClass().getResource("/agents/checkbox.css")).toExternalForm();
    }
}
