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
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  25/02/2019
 */
public class GNMonetaryField extends TextField {

    private final ObjectProperty<BigDecimal> value = new SimpleObjectProperty<>(this, "value");

    public GNMonetaryField() {



        this.getStyleClass().add("gn-monetary-field");
        this.getStylesheets().add(GNMonetaryField.class.getResource("/com/gn/css/simple.css").toExternalForm());

        this.setTextFormatter(new TextFormatter<>(change -> {

            if(change.getText().matches("[^0-9.,]") ) {
                return null;
            } else
                return change;

        }));


        DecimalFormat ds = (DecimalFormat) DecimalFormat.getInstance();
        this.setText("0" + ds.getDecimalFormatSymbols().getMonetaryDecimalSeparator() + "00");

        this.setContextMenu(new ContextMenu());

        this.textProperty().addListener((observable, oldValue, newValue) -> {

//                System.out.println("newValue = " + newValue.length());
//                System.out.println("oldValue = " + oldValue.length());
//                System.out.println("-------------------------------");

            DecimalFormat s = (DecimalFormat) DecimalFormat.getInstance();

            char group = s.getDecimalFormatSymbols().getGroupingSeparator();
            char decimal = s.getDecimalFormatSymbols().getMonetaryDecimalSeparator();

            if(newValue.length() > 4 && newValue.charAt(0) == '0' ) {
                String cent = newValue.substring(newValue.length() - 1);
                String middle = newValue.substring(0, newValue.length() - 2);

                if(oldValue.equals("0" + decimal + "00")) {
                    setText(middle + cent);
                } else if(!cent.equals("0") && newValue.charAt(2) == '0') {
                    String x = newValue.substring(0,2);
                    String y = newValue.substring(3,5);
                    setText(x + y);
                } else {
                    String x = newValue.substring(2,3);
                    String y = newValue.substring(3,5);
                    setText(x + decimal + y);
                }

            } else {

                if (newValue.length() > 2) {

                    String value = newValue;
                    value = value.replaceAll("[^0-9]", "");

                    String cent = value.substring(value.length() - 2);
                    String middle = value.substring(0, value.length() - 2);

                    StringBuilder build = new StringBuilder(middle);
                    for (int i = middle.length(); i > 3; i -= 3) {
                        build.insert(i - 3, group);
                    }

                    String all;

                    if (build.toString().length() > 0) all = build.toString() + decimal + cent;
                    else all = build.toString() + cent;

                    setText(all);

                    if (getLength() > 2) {
                        setValue(new BigDecimal(build.toString().replaceAll("[^0-9]", "") + "." + cent));
                    } else {
                        setValue(new BigDecimal(value.replaceAll("[^0-9]", "")));
                    }

                } else {

                    String value = newValue.replaceAll("[^0-9]", "");
                    setText(value);

                    if (newValue.isEmpty()) {
                        setValue(new BigDecimal(0));
                    } else {
                        setValue(new BigDecimal(value));
                    }
                }
            }
        });

        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {


            if(event.getCode().equals(KeyCode.LEFT)) event.consume();

            if(getCaretPosition() != getText().length()) {
                System.out.println("test");
//                positionCaret(getText().length());
            }

//            if(event.getCode() == KeyCode.BACK_SPACE){
//                if(getCaretPosition() > 0) {
//
//                    if (getText().charAt(getCaretPosition() - 1) == '.') {
//                        deleteText(getCaretPosition() - 2, getCaretPosition());
//                    } else if (getText().charAt(getCaretPosition() - 1) == ',') {
//                        deleteText(getCaretPosition() - 2, getCaretPosition());
//                    }
//
//                }
//            }
        });

    }

    public String formatValue(String text){
        String all = null;

        if(!text.equals("")){
                DecimalFormat s = (DecimalFormat) DecimalFormat.getInstance();

                char group = s.getDecimalFormatSymbols().getGroupingSeparator();
                char decimal = s.getDecimalFormatSymbols().getMonetaryDecimalSeparator();

                if(this.getLength() > 3) {
                    String value = this.getText();
                    value = value.replaceAll("[^0-9]", "");

                    String cent = value.substring(value.length() - 2);
                    String middle = value.substring(0, value.length() - 2);

                    StringBuilder build = new StringBuilder(middle);
                    for (int i = middle.length(); i > 3; i -= 3) {
                        build.insert(i - 3, group);
                    }

                    if(build.toString().length() > 0) all = build.toString() + decimal + cent;
                    else all = build.toString() + cent;

                    GNMonetaryField.super.setText(all);

                }
        }
        return all;
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