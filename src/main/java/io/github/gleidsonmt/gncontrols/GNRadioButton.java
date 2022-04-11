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

import io.github.gleidsonmt.gncontrols.skin.GNRadioButtonSkin;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.PaintConverter;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  19/02/2022
 */
public class GNRadioButton extends RadioButton implements GNComponent {

    public GNRadioButton() {
        this(null);
    }

    public GNRadioButton(String text) {
        super(text);
        getStyleClass().add("gn-radio-button");
        setCursor(Cursor.HAND);
        setRippleFill(Color.WHITE);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNRadioButtonSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getControlStylesheet();
    }


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

        private static final CssMetaData<GNRadioButton, Paint>               RIPPLE_FILL;

        private StyleableProperties() {}

        static {

            RIPPLE_FILL = new CssMetaData<>("-gn-ripple-fill", PaintConverter.getInstance(), Color.WHITE) {
                @Override
                public boolean isSettable(GNRadioButton styleable) {
                    return !styleable.rippleFill.isBound();
                }

                @Override
                public StyleableProperty<Paint> getStyleableProperty(GNRadioButton styleable) {
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
                    new ArrayList<>(RadioButton.getClassCssMetaData());
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
