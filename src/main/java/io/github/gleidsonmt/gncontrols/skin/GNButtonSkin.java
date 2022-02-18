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

package io.github.gleidsonmt.gncontrols.skin;

import io.github.gleidsonmt.gncontrols.GNButton;
import io.github.gleidsonmt.gncontrols.options.GNButtonType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/02/2022
 */
public class GNButtonSkin extends ButtonSkin {


    public GNButtonSkin(GNButton control) {
        super(control);


        Timeline timeEntered = new Timeline();

        getSkinnable().setOnMouseEntered(null);
        getSkinnable().setOnMouseExited(null);


//        registerChangeListener(control.buttonTypeProperty(), c -> {
//            control.setButtonType((GNButtonType) c.getValue());
//        });

        getSkinnable().setOnMouseClicked(event -> {

            if (timeEntered.getStatus() == Animation.Status.RUNNING) {
                return;
            }

            double min = control.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius() ;

            Rectangle clip = new Rectangle();

            clip.setWidth(control.getWidth() );
            clip.setHeight(control.getHeight() );

            System.out.println("min = " + min);

            clip.setSmooth(true);
            clip.setStrokeWidth(0);
            control.setClip(clip);


            control.setButtonType(GNButtonType.RECT);

            switch (control.getButtonType()) {
                    case RECT -> {
                        clip.setArcHeight(0);
                        clip.setArcWidth( 0 );
                    }
                    case ROUNDED -> {
                        clip.setArcHeight(control.getWidth());
                        clip.setArcWidth(control.getWidth());
                    }
                    case SEMI_ROUNDED -> {
                        clip.setArcHeight(min * 2);
                        clip.setArcWidth(min * 2);
                    }
            }

//            setAnimated(true);
            Circle circle = new Circle();
            circle.setRadius(0);
            circle.setStrokeWidth(0);

            circle.setFill(control.getRippleFill());

            circle.setLayoutX(event.getX());
            circle.setLayoutY(event.getY());

            circle.setOpacity(0.5);
            circle.setMouseTransparent(true);
            getChildren().add(circle);

            double diameter = Math.max(control.getWidth(), control.getHeight());
            double radius = diameter / 2;

            timeEntered.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO, new KeyValue(circle.radiusProperty(), 0)),
                    new KeyFrame(Duration.millis(250), new KeyValue(circle.radiusProperty(), radius *2 ))
            );

            timeEntered.play();

            timeEntered.setOnFinished( e -> {
//                setAnimated(false);
                getChildren().removeAll(circle);
            });

        });
    }

}
