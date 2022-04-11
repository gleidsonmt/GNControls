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
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.util.Duration;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  18/02/2022
 */
public class SwipeDiagonalSkin extends GNButtonBaseSkin {

    private final Pane rect = new Pane();
    private final Timeline timeLineOnEnter = new Timeline();
    private final Timeline timeLineOnExit = new Timeline();
    private GNButtonHover control;
    private Color oldColor;

    public SwipeDiagonalSkin(GNButtonHover control) {
        super(control);
        this.control =  control;

        getChildren().add(rect);
        rect.toBack();

        SVGPath shape = new SVGPath();
        shape.setContent("M 250 200 L 250 300 L 500 300 L 450 200 L 250 200 ");
        rect.setShape(shape);

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

    public void updateType(Color color) {
        switch (control.getButtonType()) {
            case RECT -> {
                rect.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
//                this.setBorder(new Border(new BorderStroke(getFillTransition(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
            case ROUNDED -> {
                rect.setBackground(new Background(new BackgroundFill(color, new CornerRadii(100), Insets.EMPTY)));
//                this.setBorder(new Border(new BorderStroke(getFillTransition(), BorderStrokeStyle.SOLID, new CornerRadii(100), BorderWidths.DEFAULT)));
            }
            case SEMI_ROUNDED -> {
                rect.setBackground(new Background(new BackgroundFill(color, new CornerRadii(3.5), Insets.EMPTY)));
//                this.setBorder(
//                        new Border(
//                                new BorderStroke(
//                                        getFillTransition(),
//                                        new BorderStrokeStyle(StrokeType.CENTERED, StrokeLineJoin.ROUND, StrokeLineCap.SQUARE, 1, 0, null),
//                                        new CornerRadii(3.5), BorderWidths.DEFAULT)));
            }
        }
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);
        layoutInArea(rect, (w * -1) - (w / 4), y, w + (w / 4) , h, 2,
                HPos.LEFT,  VPos.CENTER);
    }

    private final EventHandler<MouseEvent> onEnter = event -> {

        rect.setMinHeight(control.getHeight());
        rect.toBack();

//        rect.setBackground(new Background(new BackgroundFill(control.getFillTransition())));
//        rect.setFill(control.getFillTransition());
//        rect.setStyle("-fx-background-color : " + control.getFillTransition());

        updateType((Color) control.getBackgroundTransition());

        timeLineOnEnter.getKeyFrames().setAll(
                      new KeyFrame(Duration.ZERO, new KeyValue(
                              rect.translateXProperty(),  rect.getTranslateX()
                      )),
                      new KeyFrame(Duration.millis(200), new KeyValue(
                              rect.translateXProperty(), rect.getWidth()
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

        timeLineOnExit.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        rect.translateXProperty(),  rect.getTranslateX()
                )),
                new KeyFrame(Duration.millis(200), new KeyValue(
                        rect.translateXProperty(), 0
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
        getChildren().remove(rect);
        control.removeEventHandler(MouseEvent.MOUSE_ENTERED, onEnter);
        control.removeEventHandler(MouseEvent.MOUSE_EXITED, onExit);
    }
}
