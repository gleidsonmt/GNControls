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
package com.gn.control;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/04/2019
 */
public class GNNumericField extends TextField {

    private ObjectProperty<Number> value = new SimpleObjectProperty<>(0);

    public GNNumericField() {
            this.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    Number num = null;
                    try {
                        num = NumberFormat.getInstance().parse(newValue);
                        setValue(num);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(num);
                }
            });
    }

    public Number getValue() {
        return value.get();
    }

    public ObjectProperty<Number> valueProperty() {
        return value;
    }

    public void setValue(Number value) {
        this.value.set(value);
    }
}
