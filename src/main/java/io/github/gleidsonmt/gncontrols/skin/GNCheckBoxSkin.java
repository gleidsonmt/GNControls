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

package io.github.gleidsonmt.gncontrols.skin;

import io.github.gleidsonmt.gncontrols.controls.GNCheckBox;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.skin.CheckBoxSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  20/02/2022
 */
public class GNCheckBoxSkin extends CheckBoxSkin {

    private final Circle circle = new Circle();
    private final Timeline timeline = new Timeline();
    private StackPane dot;
    private GNCheckBox control;

    public GNCheckBoxSkin(GNCheckBox control) {
        super(control);
        this.control = control;
        this.control.setCursor(Cursor.HAND);

        dot = (StackPane) control.lookup(".box");

        control.addEventHandler(MouseEvent.MOUSE_PRESSED, onPressed);

    }

    private final EventHandler<MouseEvent> onPressed = event -> {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        circle.setRadius(0);
        circle.setStrokeWidth(0);

//            circle.setFill(control.getRippleFill());
        circle.setStyle("-fx-fill : -fx-accent");
//        circle.setFill(Color.gray(0.6));

        circle.setLayoutX(dot.getWidth() / 2);
        circle.setLayoutY(dot.getHeight() / 2);

        circle.setOpacity(0.5);
        circle.setMouseTransparent(true);
        getChildren().add(circle);

        double diameter = Math.max(dot.getWidth(), dot.getHeight());

        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(circle.radiusProperty(), 0)),
                new KeyFrame(Duration.millis(250), new KeyValue(circle.radiusProperty(), diameter + 5  ))
        );

        timeline.play();

        timeline.setOnFinished( e -> getChildren().removeAll(circle));
    };

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        layoutInArea(circle, x - (control.getWidth() / 2 -10), y , w, h, 0, HPos.CENTER, VPos.CENTER);
    }
}
