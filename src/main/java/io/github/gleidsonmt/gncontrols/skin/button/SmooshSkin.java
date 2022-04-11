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

import io.github.gleidsonmt.gncontrols.GNButtonHover;
import io.github.gleidsonmt.gncontrols.options.GNButtonType;
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
 * Create on  18/02/2022
 */
public class SmooshSkin extends GNButtonBaseSkin {

    private final Rectangle rectTop = new Rectangle();
    private final Rectangle rectBottom = new Rectangle();
    private final Timeline timeLineOnEnter = new Timeline();
    private final Timeline timeLineOnExit = new Timeline();

    private GNButtonHover control;
    private Color oldColor;

    public SmooshSkin(GNButtonHover control) {
        super(control);
        this.control =  control;

        getChildren().addAll(rectTop, rectBottom);
        rectTop.toBack();
        rectBottom.toBack();

        control.addEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.addEventHandler(MouseEvent.MOUSE_EXITED, onExit);

        oldColor = (Color) control.getTextFill();

        registerChangeListener(control.buttonTypeProperty(), c -> {
            control.setButtonType((GNButtonType) c.getValue());
            update (control.getButtonType());

        });

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
        layoutInArea(rectTop, x - snappedLeftInset() , y - snappedTopInset(), w, h , 0, HPos.LEFT, VPos.TOP);
        layoutInArea(rectBottom, x - snappedLeftInset(), y - snappedBottomInset(), w, h + snappedBottomInset(), 0, HPos.LEFT, VPos.BOTTOM);

    }

    private final EventHandler<MouseEvent> onEnter = event -> {

        rectTop.setHeight(0);
        rectBottom.setHeight(0);

        rectTop.setWidth(control.getWidth());
        rectBottom.setWidth(control.getWidth());

        rectTop.setFill(control.getBackgroundTransition());
        rectBottom.setFill(control.getBackgroundTransition());

        timeLineOnEnter.getKeyFrames().setAll(
            new KeyFrame(Duration.ZERO, new KeyValue(
                  rectTop.heightProperty(), 0
            )),
            new KeyFrame(Duration.millis(200), new KeyValue(
                  rectTop.heightProperty(), control.getHeight() / 2
            )),
            new KeyFrame(Duration.ZERO, new KeyValue(
                    rectBottom.heightProperty(), 0
            )),
            new KeyFrame(Duration.millis(200), new KeyValue(
                    rectBottom.heightProperty(), control.getHeight() / 2
            )),

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


    private final EventHandler<MouseEvent> onExit = event -> {
        timeLineOnExit.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectTop.heightProperty(), rectTop.getHeight()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectTop.heightProperty(), 0
                )),
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectBottom.heightProperty(), control.getHeight() / 2
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectBottom.heightProperty(), 0
                )),
                new KeyFrame(Duration.ZERO, new KeyValue(
                        control.textFillProperty(), oldColor
                )),
                new KeyFrame(Duration.millis(150), new KeyValue(
                        control.textFillProperty(), control.getTextTransition()
                ))
        );
//
        if (timeLineOnEnter.getStatus() == Animation.Status.RUNNING) {
            timeLineOnEnter.stop();
        }
        timeLineOnExit.play();
    };

    @Override
    public void dispose() {
        super.dispose();
        getChildren().removeAll(rectTop, rectBottom);
        control.removeEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.removeEventHandler(MouseEvent.MOUSE_EXITED, onExit);
    }
}
