/*
 *
 *  * Copyright (C) Gleidson Neves da Silveira
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.converters.GNButtonTypeConverter;
import io.github.gleidsonmt.gncontrols.material.color.Colors;
import io.github.gleidsonmt.gncontrols.options.GNButtonType;
import io.github.gleidsonmt.gncontrols.skin.GNButtonSkin;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.PaintConverter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/02/2022
 */
public class GNButton extends Button implements GNComponent {

    public GNButton() {
        this(null);
    }

    public GNButton(String text) {
        this(text, GNButtonType.RECT);
    }

    public GNButton(String text, GNButtonType type) {
        super(text);
        if (text == null) {
            setText("Button");
        }
        setAlignment(Pos.CENTER);
        getStyleClass().add("gn-button");
        setPrefSize(100, 40);

        setRippleFill(Color.WHITE);

        buttonType.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (type) {
                    case RECT -> setBackground(new Background(new BackgroundFill(Colors.get("aqua"), CornerRadii.EMPTY, Insets.EMPTY)));
                    case ROUNDED -> setBackground(new Background(new BackgroundFill(Colors.get("aqua"), new CornerRadii(100), Insets.EMPTY)));
                    case SEMI_ROUNDED -> setBackground(new Background(new BackgroundFill(Colors.get("aqua"), new CornerRadii(10), Insets.EMPTY)));
                }
            }
        });
        setButtonType(type);

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNButtonSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getControlStylesheet();
    }

    private final StyleableObjectProperty<GNButtonType> buttonType = new StyleableObjectProperty<>() {
        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "buttonType";
        }

        @Override
        public CssMetaData<? extends Styleable, GNButtonType> getCssMetaData() {
            return StyleableProperties.BUTTON_TYPE;
        }
    };

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

        private static final CssMetaData<GNButton, GNButtonType>        BUTTON_TYPE;
        private static final CssMetaData<GNButton, Paint>               RIPPLE_FILL;

        private StyleableProperties() {}

        static {


        BUTTON_TYPE = new CssMetaData<>(
                "-gn-button-type", GNButtonTypeConverter.getInstance()) {
            @Override
            public boolean isSettable(GNButton styleable) {
                return !styleable.buttonType.isBound();
            }

            @Override
            public StyleableProperty<GNButtonType> getStyleableProperty(GNButton styleable) {
                return styleable.buttonTypeProperty();
            }
        };

        RIPPLE_FILL = new CssMetaData<>("-gn-ripple-fill", PaintConverter.getInstance(), Color.WHITE) {
            @Override
            public boolean isSettable(GNButton styleable) {
                return !styleable.rippleFill.isBound();
            }

            @Override
            public StyleableProperty<Paint> getStyleableProperty(GNButton styleable) {
                return styleable.rippleFillProperty();
            }
        };

            List<CssMetaData<? extends Styleable, ?>> styleables
                    = new ArrayList<>(Control.getClassCssMetaData());
                Collections.addAll(styleables, BUTTON_TYPE, RIPPLE_FILL);
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


    public GNButtonType getButtonType() {
        return buttonType.get();
    }

    public StyleableObjectProperty<GNButtonType> buttonTypeProperty() {
        return buttonType;
    }

    public void setButtonType(GNButtonType buttonType) {
        this.buttonType.set(buttonType);
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
