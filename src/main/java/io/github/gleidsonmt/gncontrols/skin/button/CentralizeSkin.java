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
public class CentralizeSkin extends GNButtonBaseSkin {

    private final Rectangle rect = new Rectangle();
    private final Timeline timeLineOnEnter = new Timeline();
    private final Timeline timeLineOnExit = new Timeline();
    private GNButtonHover control;
    private Color oldColor;


    public CentralizeSkin(GNButtonHover control) {
        super(control);

        this.control =  control;

        getChildren().add(rect);
        rect.toBack();

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
        layoutInArea(rect, x, y, w, h, 0,
                HPos.CENTER, VPos.CENTER);
    }

    @Override
    public void dispose() {
        super.dispose();
        getChildren().removeAll(rect);
        control.removeEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.removeEventHandler(MouseEvent.MOUSE_EXITED, onExit);
    }

    private final EventHandler<MouseEvent> onEnter = event -> {

        rect.setHeight(control.getHeight());
        rect.setFill(control.getBackgroundTransition());

        timeLineOnEnter.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rect.widthProperty(), rect.getWidth()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rect.widthProperty(), control.getWidth()
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

//        updateClip();
        timeLineOnExit.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rect.widthProperty(), rect.getWidth()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rect.widthProperty(), 0
                )),
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
