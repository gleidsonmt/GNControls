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

package io.github.gleidsonmt.gncontrols.controls.skin;

import io.github.gleidsonmt.gncontrols.controls.GNPasswordBox;
import io.github.gleidsonmt.gncontrols.controls.GNIconButton;
import io.github.gleidsonmt.gncontrols.controls.text_box.Editor;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import javafx.scene.input.MouseEvent;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  24/09/2022
 */
public class GNPasswordBoxSkin extends GNTextBoxSkin {

    public GNPasswordBoxSkin(GNPasswordBox _control) {
        super(_control);

        getBase().textProperty().unbind();

        GNIconButton actionButton = createAction();
        setActionButton(actionButton);
        getBase().setRightNode(actionButton);

    }


    public GNIconButton createAction() {
        GNIconButton actionButton = new GNIconButton(Icons.VIEWER);
        actionButton.getStyleClass().add("action-button");

        actionButton.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

            Editor editor = getBase().getEditor();
            editor.setMaskText(!editor.isMaskText());

            actionButton.setIcon(
                    !editor.isMaskText() ?
                            Icons.VIEWER_OFF : Icons.VIEWER
            );
//
            editor.setText(editor.getText());
            editor.end();
//
        });

        return actionButton;
    }
}
