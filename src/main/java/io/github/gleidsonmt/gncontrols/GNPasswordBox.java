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

package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.skin.GNPasswordBoxSkin;
import javafx.beans.DefaultProperty;
import javafx.scene.control.*;

@DefaultProperty("control")
@SuppressWarnings("unused")
public class GNPasswordBox extends GNTextBox {

    public GNPasswordBox() {
        getStyleClass().add("gn-password-box");
        PasswordField passwordField = new PasswordField() {
            @Override
            public void paste() {
                createPasteAction(this);
            }
        };

        setEditor(passwordField);
        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> setFocused(newValue));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNPasswordBoxSkin(this);
    }


}