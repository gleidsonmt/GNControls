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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/02/2022
 */
public class BorderAnimation extends GNButtonBaseSkin {

    private final Rectangle rect = new Rectangle();
    private final GNButtonBase control;
    private Color oldColor;

    public BorderAnimation(GNButtonBase control) {
        super(control);
        this.control =  control;

        rect.setFill(Color.RED);
        getChildren().add(rect);

        rect.widthProperty().bind(control.widthProperty());
        rect.heightProperty().bind(control.heightProperty());
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLACK);

        rect.arcHeightProperty().bind(getClip().arcHeightProperty());
        rect.arcWidthProperty().bind(getClip().arcWidthProperty());

//        control.setStyle("-fx-background-color : derive(#4FC1E9, 50%)");
        rect.setStrokeDashOffset(150);
        rect.getStrokeDashArray().setAll(150D, 480D);

        rect.setStrokeWidth(3);

        Timeline timeLineOnEnter = new Timeline();
        Timeline timeLineOnExit = new Timeline();

        control.setOnMouseEntered(event -> {
            timeLineOnEnter.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO, new KeyValue(
                            rect.strokeDashOffsetProperty(), rect.getStrokeDashOffset()
                    )),
                    new KeyFrame(Duration.millis(1000), new KeyValue(
                            rect.strokeDashOffsetProperty(), -480
                    ))

                );
            if (timeLineOnExit.getStatus() == Animation.Status.RUNNING) {
                timeLineOnExit.stop();
            }
            timeLineOnEnter.play();
        });

        control.setOnMouseExited(event -> {
            timeLineOnExit.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO, new KeyValue(
                            rect.strokeDashOffsetProperty(), rect.getStrokeDashOffset()
                    )),
                    new KeyFrame(Duration.millis(1000), new KeyValue(
                            rect.strokeDashOffsetProperty(), 150
                    ))

            );
            if (timeLineOnEnter.getStatus() == Animation.Status.RUNNING) {
                timeLineOnEnter.stop();
            }
            timeLineOnExit.play();
        });

    }

    @Override
    public void dispose() {
        super.dispose();
        getChildren().removeAll(rect);
    }
}
