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

import io.github.gleidsonmt.gncontrols.GNButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  18/02/2022
 */
public class RippleButtonSkin extends GNButtonBaseSkin {

    private final Circle circle = new Circle();
    private final Timeline timeline = new Timeline();
    private final GNButton control;

    public RippleButtonSkin(GNButton control) {
        super(control);
        this.control = control;

        registerChangeListener( control.rippleFillProperty(), c -> {
             control.setRippleFill((Paint) c.getValue());
        });

        control.addEventHandler(MouseEvent.MOUSE_PRESSED, onPressed);

    }

    private final EventHandler<MouseEvent> onPressed = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                return;
            }

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

            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO, new KeyValue(circle.radiusProperty(), 0)),
                    new KeyFrame(Duration.millis(250), new KeyValue(circle.radiusProperty(), radius *2 ))
            );

            timeline.play();

            timeline.setOnFinished( e -> {
                getChildren().removeAll(circle);
            });
        }
    };

    @Override
    public void dispose() {
        super.dispose();
        control.removeEventHandler(MouseEvent.MOUSE_PRESSED, onPressed);
    }
}
