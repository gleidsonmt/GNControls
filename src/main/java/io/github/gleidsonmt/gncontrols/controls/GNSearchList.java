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

package io.github.gleidsonmt.gncontrols.controls;

import io.github.gleidsonmt.gncontrols.controls.skin.GNSearchListSkin;
import io.github.gleidsonmt.gncontrols.controls.skin.GNTextBoxBase;
import io.github.gleidsonmt.gncontrols.controls.text_box.FloatEditor;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  27/09/2022
 */
public class GNSearchList<T> extends GNTextBoxBase {

    private final ObjectProperty<T> value = new SimpleObjectProperty<>(this, "value");

    public GNSearchList() {
        setPrefSize(200, 50);

        setPromptText("Custom Prompt Text");
        getStyleClass().add("gn-search-box");
        setEditor(new FloatEditor());

    }


    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(getClass().getResource("/agents/text_box_base.css")).toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNSearchListSkin<T>(this);
    }

    private final ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>(this, "items", FXCollections.observableArrayList());

    public final void setItems(ObservableList<T> value) {
        itemsProperty().set(value);
    }

    public final ObservableList<T> getItems() {
        return items.get();
    }

    public ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }

    public T getValue() {
        return value.get();
    }

    public ObjectProperty<T> valueProperty() {
        return value;
    }

    public void setValue(T value) {
        this.value.set(value);
    }
    

}
