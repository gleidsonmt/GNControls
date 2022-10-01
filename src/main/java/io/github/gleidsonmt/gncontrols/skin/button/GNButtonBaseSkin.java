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
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/02/2022
 */
public class GNButtonBaseSkin extends ButtonSkin {

    private final GNButtonBase control;

    private final Rectangle region = new Rectangle();

    private final Rectangle clip = new Rectangle();

    public GNButtonBaseSkin(GNButtonBase control) {
        super(control);
        this.control =  control;

        region.widthProperty().bind(control.widthProperty());
        region.heightProperty().bind(control.heightProperty());

        registerChangeListener(control.buttonTypeProperty(), c -> {
            control.setButtonType((GNButtonType) c.getValue());
            update (control.getButtonType());

        });
//
        control.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            update (control.getButtonType());
        });
////
        update(control.getButtonType());


        clip.widthProperty().bind(control.widthProperty());
        clip.heightProperty().bind(control.heightProperty());
//
//        region.setStyle("-fx-background-color : blue;");


//        getChildren().add(region);

//        clip.setFill(Color.RED);
//

    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
    }


    protected void update(GNButtonType type) {

        System.out.println(type);

        double min = control.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius();

        switch (type) {
            case RECT -> {
                clip.setArcHeight(0);
                clip.setArcWidth( 0 );
            }
            case ROUNDED -> {

//                clip.setArcWidth(control.getWidth() == 0 ?
//                        control.getPrefHeight() : Math.min(control.getHeight(), control.getWidth()));
//
//                clip.setArcHeight(control.getWidth() == 0 ?
//                        control.getPrefHeight() : Math.min(control.getHeight(), control.getWidth()));

            }
            case SEMI_ROUNDED -> {
                clip.setArcHeight(min * 2);
                clip.setArcWidth(min * 2);
            }
        }

    }
    protected Rectangle getClip() {
//        return new Rectangle();
        return clip;
    }
}
