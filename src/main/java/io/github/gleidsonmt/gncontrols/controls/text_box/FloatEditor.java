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

package io.github.gleidsonmt.gncontrols.controls.text_box;

import io.github.gleidsonmt.gncontrols.controls.skin.FloatEditorSkin;
import io.github.gleidsonmt.gncontrols.enums.FloatAligment;
import javafx.beans.DefaultProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  08/09/2022
 */
@DefaultProperty("children")
public class FloatEditor extends Editor {

    private final ObjectProperty<FloatAligment>   floatAligment   = new SimpleObjectProperty<>(); // top of aligment
    private final DoubleProperty                  distanceX       = new SimpleDoubleProperty(); // distance x for the prompt label

    public FloatEditor() {
        this(null, FloatAligment.BASELINE);
    }

    public FloatEditor(String floatPrompt, FloatAligment aligment) {

        floatAligment.set(aligment);

        getStyleClass().addAll("gn-float-editor");
        setPromptText(floatPrompt);
        setFocusTraversable(false);

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FloatEditorSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(FloatEditor.class.getResource("/agents/float_editor.css")).toExternalForm();
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

    public double getDistanceX() {
        return distanceX.get();
    }

    public DoubleProperty distanceXProperty() {
        return distanceX;
    }

    public void setDistanceX(double distanceX) {
        this.distanceX.set(distanceX);
    }


}
