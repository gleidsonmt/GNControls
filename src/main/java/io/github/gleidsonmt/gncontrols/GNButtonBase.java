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

import io.github.gleidsonmt.gncontrols.converters.GNButtonTypeConverter;
import io.github.gleidsonmt.gncontrols.options.GNButtonType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  18/02/2022
 */
public class GNButtonBase extends Button {

    public GNButtonBase() {
        this(null);
    }

    public GNButtonBase(String text) {
        this(text, GNButtonType.SEMI_ROUNDED);
    }

    public GNButtonBase(String text, GNButtonType type) {
        super(text);

        if (text == null) setText("Button");

        setAlignment(Pos.CENTER);
        getStyleClass().add("gn-button");
        setPrefSize(100, 40);

        setButtonType(type);

        buttonTypeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateType(newValue);
            }
        });

        updateType(type);

    }

    private void updateType(GNButtonType type) {
        switch (type) {
            case RECT -> {
                this.setBackground(new Background(new BackgroundFill(Color.gray(0.8), CornerRadii.EMPTY, Insets.EMPTY)));
            }
            case ROUNDED -> {
                this.setBackground(new Background(new BackgroundFill(Color.gray(0.8), new CornerRadii(100), Insets.EMPTY)));
            }
            case SEMI_ROUNDED -> {
                this.setBackground(new Background(new BackgroundFill(Color.gray(0.8), new CornerRadii(3.5), Insets.EMPTY)));
            }
        }
    }


    private final StyleableObjectProperty<GNButtonType> buttonType = new StyleableObjectProperty<>(GNButtonType.RECT) {
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


    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    private static class StyleableProperties {

        private final static List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        private static final CssMetaData<GNButtonBase, GNButtonType>        BUTTON_TYPE;

        private StyleableProperties() {}

        static {

            BUTTON_TYPE = new CssMetaData<>(
                    "-gn-button-type", GNButtonTypeConverter.getInstance(), GNButtonType.ROUNDED) {
                @Override
                public boolean isSettable(GNButtonBase styleable) {
                    return !styleable.buttonType.isBound();
                }

                @Override
                public StyleableProperty<GNButtonType> getStyleableProperty(GNButtonBase styleable) {
                    return styleable.buttonTypeProperty();
                }
            };

            List<CssMetaData<? extends Styleable, ?>> styleables
                    = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables, BUTTON_TYPE);
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


}
