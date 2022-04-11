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
public class SwipeSkin extends GNButtonBaseSkin {

    private final Rectangle rectangle = new Rectangle();
    private final Timeline timeLineOnEnter = new Timeline();
    private final Timeline timeLineOnExit = new Timeline();
    private GNButtonHover control;
    private Color oldColor;

    public SwipeSkin(GNButtonHover control) {
        super(control);
        this.control =  control;

        getChildren().add(rectangle);
        rectangle.toBack();

        control.addEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.addEventHandler(MouseEvent.MOUSE_EXITED, onExit);

        oldColor = (Color) control.getTextFill();
//        oldColor = Color.RED;

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
        layoutInArea(rectangle, x - snappedLeftInset(), y, w, h, 0, HPos.LEFT, VPos.CENTER);
    }

    private final EventHandler<MouseEvent> onEnter = event -> {

        rectangle.setHeight(control.getHeight());
        rectangle.setFill(control.getBackgroundTransition());
        rectangle.toBack();

        timeLineOnEnter.getKeyFrames().setAll(
                      new KeyFrame(Duration.ZERO, new KeyValue(
                              rectangle.widthProperty(), rectangle.getWidth()
                      )),
                      new KeyFrame(Duration.millis(200), new KeyValue(
                              rectangle.widthProperty(), control.getWidth()
                      )),
                      new KeyFrame(Duration.ZERO, new KeyValue(
                              control.textFillProperty(), control.getTextFill()
                      )),
                      new KeyFrame(Duration.millis(150), new KeyValue(
                              control.textFillProperty(), control.getTextTransition()
                      ))
              );

        if (timeLineOnExit.getStatus() == Animation.Status.RUNNING) {
            timeLineOnExit.stop();
        }

        timeLineOnEnter.play();
    };


    private final EventHandler<MouseEvent> onExit = event -> {

        System.out.println(control.getTextTransition());
//        updateClip();
        timeLineOnExit.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rectangle.widthProperty(), rectangle.getWidth()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rectangle.widthProperty(), 0
                )),
                new KeyFrame(Duration.ZERO, new KeyValue(
                        control.textFillProperty(), control.getTextTransition()
                )),
                new KeyFrame(Duration.millis(150), new KeyValue(
                        control.textFillProperty(), oldColor
                ))
        );

        if (timeLineOnEnter.getStatus() == Animation.Status.RUNNING) {
            timeLineOnEnter.stop();
        }
        timeLineOnExit.play();
    };

    @Override
    public void dispose() {
        super.dispose();
        getChildren().remove(rectangle);
        control.removeEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.removeEventHandler(MouseEvent.MOUSE_EXITED, onExit);
    }
}
