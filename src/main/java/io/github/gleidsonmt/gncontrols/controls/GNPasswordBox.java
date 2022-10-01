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

import io.github.gleidsonmt.gncontrols.controls.skin.GNPasswordBoxSkin;
import javafx.beans.DefaultProperty;
import javafx.scene.control.Skin;

@DefaultProperty("control")
public class GNPasswordBox extends GNTextBox {

//    public GNPasswordBox() {
//        this(null, null, false);
//    }
//
//    public GNPasswordBox(String text) {
//        this(text, null, false);
//    }
//
//    public GNPasswordBox(Icons _icon) {
//        this(null, _icon, false);
//    }
//
//    public GNPasswordBox(boolean animated) {
//        this(null, null, animated);
//    }
//
//    public GNPasswordBox(String text, boolean animated) {
//        this(text, null, animated);
//    }
//
//    public GNPasswordBox(String text, Icons _icon) {
//        this(text, _icon, false);
//    }
//
//    public GNPasswordBox(Icons _icon, boolean animated) {
//        this(null, _icon, animated);
//    }

//    public GNPasswordBox(String text, Icons _icon, boolean animated) {
    public GNPasswordBox() {
        setMaskText(true);
//    public GNTextBox(String text, Icons _icon, boolean animated, boolean mask) {
//        super(text, _icon, animated);

//        getStyleClass().addAll("gn-password-box");
//        setMaskText(true);
    }


    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNPasswordBoxSkin(this);
    }


}
