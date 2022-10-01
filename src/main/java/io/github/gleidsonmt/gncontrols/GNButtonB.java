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

package io.github.gleidsonmt.gncontrols;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.PaintConverter;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.*;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/02/2022
 */
public class GNButtonB extends Button  {

    private Rectangle clip = new Rectangle();

    public GNButtonB() {

        setPrefSize(200, 200);

        this.clip.widthProperty().bind(this.widthProperty());
        this.clip.heightProperty().bind(this.heightProperty());

        setText("Button");

        setOnMouseClicked(onPressed);


        setBorder(new Border(
                new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(2))
        ));

        getBorder().getStrokes().forEach(c -> System.out.println(c.getRightStyle()));

        double arc = this.getWidth() / 2;

        clip.setArcHeight(100 );
        clip.setArcWidth(100);

//        heightProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                System.out.println(newValue);
//                clip.setArcWidth(newValue.doubleValue() / 2);
//                clip.setArcHeight(newValue.doubleValue() / 2);
//
//
//            }
//        });

        // 10

        new Button();


        this.setClip(clip);
        setEffect(new DropShadow(5, Color.RED));

    }

    private final Timeline timeline = new Timeline();
    private final Circle circle = new Circle();


    private final EventHandler<MouseEvent> onPressed = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {

            if (timeline.getStatus() == Animation.Status.RUNNING) {
                return;
            }

            circle.setRadius(0);
            circle.setStrokeWidth(0);

            circle.setFill(Color.RED);

            circle.setLayoutX(event.getX());
            circle.setLayoutY(event.getY());

//            clip.setX(-(getWidth() /2));
//            clip.setY(-(getHeight() /2));
//            clip.setWidth((-getWidth() / 2) + (event.getX()));
//            clip.setHeight((-getHeight() / 2) + (event.getY()));
//
            circle.setOpacity(0.5);

            circle.setMouseTransparent(true);

            getChildren().add(circle);

            circle.setStyle("-fx-fill: radial-gradient(focus-distance 0% , center 50% 50% , radius 55% , #ffffff80, #fff);");
            setClip(clip);

            double diameter = Math.max(getWidth(), getHeight());
            double radius = diameter / 2;

            System.out.println(radius);

            timeline.getKeyFrames().setAll(
                    new KeyFrame(Duration.ZERO, new KeyValue(circle.radiusProperty(), 0)),
                    new KeyFrame(Duration.millis(250), new KeyValue(circle.radiusProperty(), radius *2 ))
            );


            timeline.play();


            timeline.setOnFinished( e -> {
                getChildren().remove(circle);
                setClip(null);
            });
        }
    };



//    @Override
//    protected Skin<?> createDefaultSkin() {
//        return new RippleButtonSkin(this);
//    }

    private final StyleableObjectProperty<Paint> rippleFill = new StyleableObjectProperty<>(Color.RED) {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "rippleFill";
        }

        @Override
        public CssMetaData<? extends Styleable, Paint> getCssMetaData() {
            return StyleableProperties.RIPPLE_FILL;
        }
    };

    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    private static class StyleableProperties {

        private final static List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        private static final CssMetaData<GNButtonB, Paint>               RIPPLE_FILL;

        private StyleableProperties() {}

        static {

            RIPPLE_FILL = new CssMetaData<>("-gn-ripple-fill", PaintConverter.getInstance(), Color.WHITE) {
                @Override
                public boolean isSettable(GNButtonB styleable) {
                    return !styleable.rippleFill.isBound();
                }

                @Override
                public StyleableProperty<Paint> getStyleableProperty(GNButtonB styleable) {
                    return styleable.rippleFillProperty();
                }
            };

            List<CssMetaData<? extends Styleable, ?>> styleables
                    = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, RIPPLE_FILL);
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        if (this.STYLEABLES == null) {
            List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(Button.getClassCssMetaData());
            styleables.addAll(Control.getClassCssMetaData());
            styleables.addAll(getClassCssMetaData());
            this.STYLEABLES = Collections.unmodifiableList(styleables);

        }
        return this.STYLEABLES;
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }

    public Paint getRippleFill() {
        return rippleFill.get();
    }

    public StyleableObjectProperty<Paint> rippleFillProperty() {
        return rippleFill;
    }

    public void setRippleFill(Paint rippleFill) {
        this.rippleFill.set(rippleFill);
    }
}