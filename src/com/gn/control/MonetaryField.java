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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  25/02/2019
 */
public class MonetaryField extends TextField {

    private ObjectProperty<BigDecimal> value = new SimpleObjectProperty<BigDecimal>(this, "value");

    public MonetaryField(){

        this.getStyleClass().add("gn-monetary-field");

        this.getStylesheets().add(MonetaryField.class.getResource("/com/gn/css/simple.css").toExternalForm());

        this.setTextFormatter(new TextFormatter<Object>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                if(change.getText().matches("[^0-9.,]") ){
                    return null;
                } else
                    return change;
            }
        }));

        this.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() > 2) {
                    String z = newValue;

                    String value = z;
                    value = value.replaceAll("[^0-9]", "");

                    String cent = value.substring(value.length() - 2, value.length());
                    String middle = value.substring(0, value.length() - 2);
                    // reparttir o cent

                    StringBuilder build = new StringBuilder(middle);
                    for (int i = middle.length(); i > 3; i -= 3) {
                        build.insert(i - 3, ".");
                    }
                    String all;

                    if(build.toString().length() > 1) all = build.toString() + "," + cent;
                    else all = build.toString() + cent;

                    MonetaryField.super.setText(all);
                    setValue(new BigDecimal(build.toString().replaceAll("[^0-9]","") + "." + cent));

                } else {
                    if(!newValue.isEmpty())
                        setValue(new BigDecimal(newValue));
                }

            }
        });

        this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.BACK_SPACE){
                    if(MonetaryField.super.getCaretPosition() > 0) {

                        if (MonetaryField.super.getText().
                                substring(MonetaryField.super.getCaretPosition() - 1,
                                        MonetaryField.super.getCaretPosition()).equals(".")) {
                            MonetaryField.super.deleteText(MonetaryField.super.getCaretPosition() - 2, MonetaryField.super.getCaretPosition());
                        } else if (MonetaryField.super.getText().substring(MonetaryField.super.getCaretPosition() - 1,
                                MonetaryField.super.getCaretPosition()).equals(",")) {
                            MonetaryField.super.deleteText(MonetaryField.super.getCaretPosition() - 2, MonetaryField.super.getCaretPosition());
                        }
                    }
                }
            }
        });
    }

    @Override
    public String getText(int start, int end) {
        if (start > end) {
            if(this.getCaretPosition() != 0) {
                return getContent().get(start - 1, end);
            } else {
                return getContent().get();
            }
        }

        if (start < 0
                || end > getLength()) {
            throw new IndexOutOfBoundsException();
        }

        return getContent().get(start, end);
    }


    public final BigDecimal getValue() {
        return value.get();
    }

    public final void setValue(BigDecimal value) {
        this.value.set(value);
    }

    public final ObjectProperty<BigDecimal> valueProperty() {
        return value;
    }
}