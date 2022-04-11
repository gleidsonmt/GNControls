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
import io.github.gleidsonmt.gncontrols.GNButtonHover;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  19/02/2022
 */
public class AlternateSkin extends GNButtonBaseSkin {

    private final Rectangle rectTopLeft = new Rectangle();
    private final Rectangle rectBottomLeft = new Rectangle();
    private final Rectangle rectTopRight = new Rectangle();
    private final Rectangle rectBottomRight = new Rectangle();
    private final Timeline timeLineOnEnter = new Timeline();
    private final Timeline timeLineOnExit = new Timeline();
    private GNButtonHover control;
    private Color oldColor;

    public AlternateSkin(GNButtonHover control) {
        super(control);
        this.control = control;

        getChildren().addAll(rectTopLeft, rectBottomLeft, rectTopRight, rectBottomRight);
        rectTopLeft.toBack();
        rectBottomLeft.toBack();
        rectTopRight.toBack();
        rectBottomRight.toBack();

        control.addEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.addEventHandler(MouseEvent.MOUSE_EXITED, onExit);

        oldColor = (Color) control.getTextFill();



        registerChangeListener(control.backgroundTransitionProperty(), c -> {
            control.setBackgroundTransition((Paint) c.getValue());
        });


        registerChangeListener(control.textTransitionProperty(), c -> {
            control.setTextTransition((Paint) c.getValue());
        });




        update(control.getButtonType());
    }


    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        layoutInArea(rectTopLeft, x, y, w, h, 0, HPos.LEFT, VPos.TOP);
        layoutInArea(rectBottomLeft, x + widthRect(), y, w, h, 0, HPos.LEFT, VPos.BOTTOM);
        layoutInArea(rectTopRight, x + ( widthRect() * 2), y, w, h, 0, HPos.LEFT, VPos.TOP);
        layoutInArea(rectBottomRight, x + ( widthRect() * 3), y, w, h, 0, HPos.LEFT, VPos.BOTTOM);
    }

    @Override
    public void dispose() {
        super.dispose();
        getChildren().removeAll(rectTopLeft, rectBottomLeft, rectTopRight, rectBottomRight);
        control.removeEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.removeEventHandler(MouseEvent.MOUSE_EXITED, onExit);
    }

    private final EventHandler<MouseEvent> onEnter = event -> {

        rectTopLeft.setFill(control.getBackgroundTransition());
        rectBottomLeft.setFill(control.getBackgroundTransition());
        rectTopRight.setFill(control.getBackgroundTransition());
        rectBottomRight.setFill(control.getBackgroundTransition());

        rectTopLeft.setWidth(widthRect());
        rectBottomLeft.setWidth(widthRect());
        rectTopRight.setWidth(widthRect());

        rectBottomRight.setWidth(widthRect());

        timeLineOnEnter.getKeyFrames().setAll(
                // Top Left
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectTopLeft.heightProperty(), rectTopLeft.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectTopLeft.heightProperty(), control.getHeight()
                )),

                // Bottom Left
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectBottomLeft.heightProperty(), rectBottomLeft.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectBottomLeft.heightProperty(), control.getHeight()
                )),

                // Top Right
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectTopRight.heightProperty(), rectTopRight.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectTopRight.heightProperty(), control.getHeight()
                )),

                // Bottom Right
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectBottomRight.heightProperty(), rectBottomRight.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectBottomRight.heightProperty(), control.getHeight()
                )),

                // Text Fill
                new KeyFrame(Duration.ZERO, new KeyValue(
                        control.textFillProperty(), control.getTextFill()
                )),
                new KeyFrame(Duration.millis(150), new KeyValue(
                        control.textFillProperty(), Color.WHITE
                ))
        );

        if (timeLineOnExit.getStatus() == Animation.Status.RUNNING) {
            timeLineOnExit.stop();
        }

        timeLineOnEnter.play();

    };

    private double widthRect() {
        return control.getWidth() / 4;
    }

    private final EventHandler<MouseEvent> onExit = event -> {
//        updateClip();
        timeLineOnExit.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectTopLeft.heightProperty(), rectTopLeft.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectTopLeft.heightProperty(), 0
                )),

                // Bottom Left

                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectBottomLeft.heightProperty(), rectBottomLeft.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectBottomLeft.heightProperty(), 0
                )),

                // Top Right

                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectTopRight.heightProperty(), rectTopRight.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectTopRight.heightProperty(), 0
                )),

                // Bottom Right

                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectBottomRight.heightProperty(), rectBottomRight.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectBottomRight.heightProperty(), 0
                )),

                // Text Fill
                new KeyFrame(Duration.ZERO, new KeyValue(
                        control.textFillProperty(), oldColor
                )),
                new KeyFrame(Duration.millis(150), new KeyValue(
                        control.textFillProperty(), control.getTextTransition()
                ))
        );

        if (timeLineOnEnter.getStatus() == Animation.Status.RUNNING) {
            timeLineOnEnter.stop();
        }
        timeLineOnExit.play();
    };
}
