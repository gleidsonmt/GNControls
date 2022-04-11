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
package io.github.gleidsonmt.gncontrols;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Classe de valor monetario, custom editor.
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  25/02/2019
 */
public class GNMonetaryField extends TextField {


    public GNMonetaryField() {
        this(2);
    }

    public GNMonetaryField(int numberOfDecimals) {

        getStyleClass().add("gn-monetary-field");
//        this.getStylesheets().add(GNMonetaryField.class.getResource("/com/gn/css/simple.css").toExternalForm());

        setTextFormatter(new TextFormatter<>(change -> {

            if(change.getText().matches("[^0-9.,]") ) {
                return null;
            } else
                return change;
        }));


        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();

        char group = decimalFormat.getDecimalFormatSymbols().getGroupingSeparator();
        char decimal = decimalFormat.getDecimalFormatSymbols().getMonetaryDecimalSeparator();

//        this.setContextMenu(new ContextMenu());

        this.textProperty().addListener((observable, oldValue, newValue) -> {

//                System.out.println("newValue = " + newValue.length());
//                System.out.println("oldValue = " + oldValue.length());
//                System.out.println("-------------------------------");

            if (newValue.isEmpty()) {
                setText("0" + decimal + createZeros(numberOfDecimals));
                return;
            }

            if(newValue.length() > (2 + numberOfDecimals) && newValue.charAt(0) == '0' ) {
                String cent = newValue.substring(newValue.length() - numberOfDecimals);
                String middle = newValue.substring(0, newValue.indexOf(decimal) );

                if(oldValue.equals("0" + decimal + createZeros(numberOfDecimals))) {
                    setText(middle + cent);
                } else if(!cent.equals("0") && newValue.charAt(2) == '0') {
                    String x = newValue.substring(0,2);
                    String y = newValue.substring(3,3 + numberOfDecimals);
                    setText(x + y);
                } else {
                    String x = newValue.substring(2,3);
                    String y = newValue.substring(3,3 + numberOfDecimals);
                    setText(x + decimal + y);
                }

            } else {
                if (newValue.length() > 2) {
                    String value = newValue;
                    value = value.replaceAll("[^0-9]", "");

                    String cent = value.substring(value.length() - numberOfDecimals);
                    String middle = value.substring(0, value.length() - numberOfDecimals);
                    if (middle.equals("")) middle = "0";

                    StringBuilder build = new StringBuilder(middle);
                    for (int i = middle.length(); i > 3; i -= 3) {
                        build.insert(i - 3, group);
                    }

                    String all;

                    if (build.toString().length() > 0) all = build.toString() + decimal + cent;
                    else all = build + cent;

                    setText(all);

                } else {
                    if (newValue.charAt(0) != '0' ) {
                        char val = newValue.charAt(0);
                        setText("0" + decimal + val + "0");
//                    }
                    } else if (newValue.charAt(1) != '0') {
                        char val = newValue.charAt(1);
                        System.out.println("0"  + decimal + "0" + val );
//
                        setText("0" + decimal + "0" + val );
                    } else {
                        System.out.println("passed");
                        setText("0" + decimal + "00");
                    }
                }
            }
        });

//        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
////
////
////            System.out.println(event);
//////            if(event.getCode().equals(KeyCode.LEFT)) event.consume();
//////
//////            if(getCaretPosition() != getText().length()) {
//////                System.out.println("test");
////////                positionCaret(getText().length());
//////            }
////
//            if(event.getCode() == KeyCode.BACK_SPACE){
////                if(getCaretPosition() > 0) {
////                    System.out.println("whats happened = " + getText().charAt(getCaretPosition() ));
//                System.out.println("getTex" + getText());
////
////                    if (getText().charAt(getCaretPosition() - 1) == '.') {
////                        deleteText(getCaretPosition() - 2, getCaretPosition());
////                    } else if (getText().charAt(getCaretPosition() - 1) == ',') {
////                        deleteText(getCaretPosition() - 2, getCaretPosition());
////                    }
//
//                System.out.println(getText());
//                if (getText().charAt(0) != '0') {
//                    char val = getText().charAt(0);
//                    setText("0," + val + "0");
//                    positionCaret(getText().length());
//                    System.out.println("val = " + val);
////
//                }
//            }
//        });

        setText("0" + decimal + createZeros(numberOfDecimals));
//        setText("1,111");

    }

    private String createZeros(int number) {
        return "0".repeat(Math.max(0, number));
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
//                return getContent().get(start - 1, end);
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

}