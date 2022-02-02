/*
 * Copyright (C) Gleidson Neves da Silveira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.gleidsonmt.controls;

import io.gleidsonmt.controls.skin.GNTextAreaSkin;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.SizeConverter;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  14/01/2022
 */
public class GNTextArea extends TextArea implements Component {

    public GNTextArea() {
        this(null);
    }

    public GNTextArea(String text) {
        setText(text);
        getStyleClass().add("gn-text-area");
        setWrapText(true);
    }

    @Override
    public String getUserAgentStylesheet() {
        return this.getControlStylesheet();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNTextAreaSkin(this);
    }

    @Override
    public void setCommonStylesheet() {
        getStylesheets().add(getCommonStylesheet());
    }

    private final StyleableBooleanProperty floatPrompt =
            new StyleableBooleanProperty() {
                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "floatPrompt";
                }

                @Override
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.FLOAT_PROMPT;
                }
            };

    private final StyleableObjectProperty<Number> maxLength =
            new StyleableObjectProperty<>(0) {
                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "maxLength";
                }

                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.MAX_LENGTH;
                }
            };

    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        if (this.STYLEABLES == null) {
            List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(getClassCssMetaData());
            styleables.addAll(getClassCssMetaData());
            styleables.addAll(Control.getClassCssMetaData());
            this.STYLEABLES = Collections.unmodifiableList(styleables);
        }
        return this.STYLEABLES;
    }

    private static class StyleableProperties {

        private static final List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        private static final CssMetaData<GNTextArea, Boolean>   FLOAT_PROMPT;
        private static final CssMetaData<GNTextArea, Number>    MAX_LENGTH;

        private StyleableProperties () {}

        static {

            FLOAT_PROMPT = new CssMetaData<>("-gn-float-prompt",
                    BooleanConverter.getInstance()) {
                @Override
                public boolean isSettable(GNTextArea styleable) {
                    return !styleable.floatPrompt.isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(GNTextArea styleable) {
                    return styleable.floatPromptProperty();
                }
            };

            MAX_LENGTH = new CssMetaData<>(
                    "-gn-max-length", SizeConverter.getInstance(), 0)
            {
                @Override
                public boolean isSettable(GNTextArea styleable) {
                    return !styleable.maxLength.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(GNTextArea styleable) {
                    return styleable.maxLengthProperty();
                }
            };

            List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(
                    styleables,
                    FLOAT_PROMPT, MAX_LENGTH
            );
            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);
        }

    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }

    public boolean isFloatPrompt() {
        return floatPrompt.get();
    }

    public StyleableBooleanProperty floatPromptProperty() {
        return floatPrompt;
    }

    public void setFloatPrompt(boolean floatPrompt) {
        this.floatPrompt.set(floatPrompt);
    }

    public Number getMaxLength() {
        return maxLength.get();
    }

    public StyleableObjectProperty<Number> maxLengthProperty() {
        return maxLength;
    }

    public void setMaxLength(Number maxLength) {
        this.maxLength.set(maxLength);
    }
}
