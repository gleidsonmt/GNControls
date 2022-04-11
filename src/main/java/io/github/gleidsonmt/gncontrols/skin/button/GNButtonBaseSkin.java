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

package io.github.gleidsonmt.gncontrols.skin.button;

import io.github.gleidsonmt.gncontrols.GNButtonBase;
import io.github.gleidsonmt.gncontrols.options.GNButtonType;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/02/2022
 */
public class GNButtonBaseSkin extends ButtonSkin {

    private final Rectangle clip = new Rectangle();
    private final GNButtonBase control;

    public GNButtonBaseSkin(GNButtonBase control) {
        super(control);
        this.control =  control;

        registerChangeListener(control.buttonTypeProperty(), c -> {
            control.setButtonType((GNButtonType) c.getValue());
            update (control.getButtonType());

        });

        control.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            update (control.getButtonType());
        });

        update(control.getButtonType());
        clip.widthProperty().bind(control.widthProperty());
        clip.heightProperty().bind(control.heightProperty());
        control.setClip(clip);

    }

    protected void update(GNButtonType type) {

        double min = control.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius() ;

        switch (type) {
            case RECT -> {
                clip.setArcHeight(0);
                clip.setArcWidth( 0 );
            }
            case ROUNDED -> {
                clip.setArcWidth(control.getWidth() == 0 ? control.getPrefHeight() : Math.min(control.getHeight(), control.getWidth()));
                clip.setArcHeight(control.getWidth() == 0 ? control.getPrefHeight() : Math.min(control.getHeight(), control.getWidth()));
            }
            case SEMI_ROUNDED -> {
                clip.setArcHeight(min * 2);
                clip.setArcWidth(min * 2);
            }
        }
    }
    protected Rectangle getClip() {
        return clip;
    }
}
