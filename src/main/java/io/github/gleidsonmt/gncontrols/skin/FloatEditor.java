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

import io.github.gleidsonmt.gncontrols.enums.FloatAligment;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  08/09/2022
 */
@DefaultProperty(value = "Controls")
public class FloatEditor extends TextField {

    private ObjectProperty<FloatAligment> floatAligment = new SimpleObjectProperty<>(FloatAligment.TOP);

    public FloatEditor() {

        getStyleClass().add("gn-float-editor");
        setAlignment(Pos.CENTER_LEFT);

        if (getPromptText().isEmpty()) {
            setPromptText("Prompt Text");
        }

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FloatEditorSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(FloatEditor.class.getResource("/text_box_editor.css")).toExternalForm();
    }

    public FloatAligment getFloatAligment() {
        return floatAligment.get();
    }

    public ObjectProperty<FloatAligment> floatAligmentProperty() {
        return floatAligment;
    }

    public void setFloatAligment(FloatAligment floatAligment) {
        this.floatAligment.set(floatAligment);
    }
}
