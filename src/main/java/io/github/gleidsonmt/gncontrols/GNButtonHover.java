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

import io.github.gleidsonmt.gncontrols.converters.GNHoverTypeConverter;
import io.github.gleidsonmt.gncontrols.options.GNButtonType;
import io.github.gleidsonmt.gncontrols.options.GNHoverType;
import io.github.gleidsonmt.gncontrols.skin.button.*;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.PaintConverter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  18/02/2022
 */
public class GNButtonHover extends GNButtonBase implements GNComponent {

    public GNButtonHover() {
        this(null);
    }

    public GNButtonHover(String text) {
        super(text);
        getStyleClass().removeAll("gn-button", "button");
        getStyleClass().addAll("gn-button-hover");

//        setTextFill(getFillTransition());

        buttonTypeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateType(newValue);
            }
        });

        fillTransitionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setTextFill(newValue);
                updateType(getButtonType());
            }
        });

        hoverTypeProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("newValue = " + newValue);
            switch (newValue) {
                case SWIPE -> setSkin(new SwipeSkin(this));
                case SWIPE_DIAGONAL -> setSkin(new SwipeDiagonalSkin(this));
                case HOVER_BORDER -> setSkin(new BorderAnimation(this));
                case SMOOSH -> setSkin(new SmooshSkin(this));
                case CORNERS -> setSkin(new CornersSkin(this));
                case CENTRALIZE -> setSkin(new CentralizeSkin(this));
                case ALTERNATE -> setSkin(new AlternateSkin(this));
            }
        });

        updateType(getButtonType());
    }

    public void updateType(GNButtonType type) {
        switch (type) {
            case RECT -> {
                this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                this.setBorder(new Border(new BorderStroke(getFillTransition(), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            }
            case ROUNDED -> {
                this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(100), Insets.EMPTY)));
                this.setBorder(new Border(new BorderStroke(getFillTransition(), BorderStrokeStyle.SOLID, new CornerRadii(100), BorderWidths.DEFAULT)));
            }
            case SEMI_ROUNDED -> {
                this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(3.5), Insets.EMPTY)));
                this.setBorder(
                        new Border(
                                new BorderStroke(
                                        getFillTransition(),
                                        new BorderStrokeStyle(StrokeType.CENTERED, StrokeLineJoin.ROUND, StrokeLineCap.SQUARE, 1, 0, null),
                                        new CornerRadii(3.5), BorderWidths.DEFAULT)));
            }
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new SwipeSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getControlStylesheet();
    }

    private final StyleableObjectProperty<Paint> fillTransition = new StyleableObjectProperty<>(Color.WHITE) {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "fillTransition";
        }

        @Override
        public CssMetaData<? extends Styleable, Paint> getCssMetaData() {
            return StyleableProperties.FILL_TRANSITION;
        }
    };

    private final StyleableObjectProperty<GNHoverType> hoverType = new StyleableObjectProperty<>(GNHoverType.SWIPE) {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "hoverType";
        }

        @Override
        public CssMetaData<? extends Styleable, GNHoverType> getCssMetaData() {
            return StyleableProperties.HOVER_TYPE;
        }
    };


    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    private static class StyleableProperties {

        private final static List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        private static final CssMetaData<GNButtonHover, Paint>          FILL_TRANSITION;
        private static final CssMetaData<GNButtonHover, GNHoverType>    HOVER_TYPE;

        private StyleableProperties() {}

        static {

            FILL_TRANSITION = new CssMetaData<>(
                    "-gn-fill-transition", PaintConverter.getInstance()) {
                @Override
                public boolean isSettable(GNButtonHover styleable) {
                    return !styleable.fillTransition.isBound();
                }

                @Override
                public StyleableProperty<Paint> getStyleableProperty(GNButtonHover styleable) {
                    return styleable.fillTransitionProperty();
                }
            };

            HOVER_TYPE = new CssMetaData<>(
                    "-gn-hover-type", GNHoverTypeConverter.getInstance()) {
                @Override
                public boolean isSettable(GNButtonHover styleable) {
                    return !styleable.hoverType.isBound();
                }

                @Override
                public StyleableProperty<GNHoverType> getStyleableProperty(GNButtonHover styleable) {
                    return styleable.hoverTypeProperty();
                }
            };

            List<CssMetaData<? extends Styleable, ?>> styleables
                    = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, FILL_TRANSITION, HOVER_TYPE);
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

    public Paint getFillTransition() {
        return fillTransition.get();
    }

    public StyleableObjectProperty<Paint> fillTransitionProperty() {
        return fillTransition;
    }

    public void setFillTransition(Paint fillTransition) {
        this.fillTransition.set(fillTransition);
    }

    public GNHoverType getHoverType() {
        return hoverType.get();
    }

    public StyleableObjectProperty<GNHoverType> hoverTypeProperty() {
        return hoverType;
    }

    public void setHoverType(GNHoverType hoverType) {
        this.hoverType.set(hoverType);
    }
}
