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

import io.github.gleidsonmt.gncontrols.options.FieldType;
import io.github.gleidsonmt.gncontrols.skin.GNTextBoxSkinB;
import io.github.gleidsonmt.gncontrols.skin.TextBoxB;
import javafx.beans.DefaultProperty;
import javafx.scene.control.*;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  24/07/2022
 */
@DefaultProperty("control")
public class GNTextBoxB extends TextBoxB implements GNComponent {

    public GNTextBoxB() {
        this(FieldType.OUTLINED, null);
    }

    public GNTextBoxB(FieldType fieldType, String text) {

//        setLeadIcon(Icons.CONTACT);
        getStyleClass().add("gn-text-box");
        setEditor(new FakeFocusTextField(){
            @Override
            public void paste() {
                createPasteAction(this);
            }
        });

        setEditor(new TextField());

        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> setFocused(newValue));

        super.setFieldType(fieldType);

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNTextBoxSkinB(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getControlStylesheet();
    }

}