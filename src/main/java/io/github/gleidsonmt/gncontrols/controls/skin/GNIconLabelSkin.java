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

import io.github.gleidsonmt.gncontrols.controls.GNIconLabel;
import io.github.gleidsonmt.gncontrols.material.icon.IconContainer;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.Color;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  29/09/2022
 */
public class GNIconLabelSkin extends SkinBase<GNIconLabel> {

    private final IconContainer iconContainer = new IconContainer();

    public GNIconLabelSkin(GNIconLabel _control) {
        super(_control);


        if (_control.getIcon() == null || _control.getIcon() == Icons.NONE) {
            iconContainer.setContent(Icons.FAVORITE);
        } else
            iconContainer.setContent(_control.getIcon());

        _control.iconProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != Icons.NONE) {
                iconContainer.setContent(newValue.getContent());
            }
        });

        iconContainer.setFill(Color.WHITE);
        getChildren().add(iconContainer);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        layoutInArea(iconContainer, contentX, contentY, contentWidth, contentHeight, -1, HPos.CENTER, VPos.CENTER);
    }
}
