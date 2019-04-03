/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.gn.skin;

import com.gn.control.MoneyField;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Doesn't support comma separated formatting, unfortunately. The parser gets all uptight about that
 * sort of stuff. There is a fair bit of hacking here too. Need a real i18n expert to do the formatting
 * and parsing work.
 */
public class MoneyFieldSkin implements Skin<MoneyField> {
    /**
     * The {@code Control} that is referencing this Skin. There is a
     * one-to-one relationship between a {@code Skin} and a {@code Control}.
     * When a {@code Skin} is set on a {@code Control}, this variable is
     * automatically updated.
     */
    private MoneyField control;

    /**
     * This textField is used to represent the MoneyField.
     */
    private TextField textField;

    /**
     * This is the formatter which will be used to format the money
     * when editing (it just gives us the amount)
     */
    private DecimalFormat formatter;

    /**
     * If true, the symbol leads the money amount, if false the symbol
     * trails it (such as with french symbols). This is all really just
     * a giant hack, to be honest.
     */
    private boolean leadingSymbol = true;
    
    /**
     * This flag is set whenever MoneyFieldSkin is going to set the value itself.
     * When it does that, it sets this flag so that the updated value is then
     * not used to update the text, causing a potential infinite loop
     */
    private boolean ignoreValueUpdate = false;

    private InvalidationListener moneyFieldFocusListener;
    private InvalidationListener moneyFieldValueListener;
    private InvalidationListener moneyFieldStyleClassListener;

    /**
     * Create a new MoneyFieldSkin.
     * @param control The MoneyField
     */
    public MoneyFieldSkin(final MoneyField control) {
        this.control = control;

        // Create the TextField that we are going to use to represent this MoneyFieldSkin.
        // The textField restricts input so that only valid digits that contribute to the
        // Money can be input.
        textField = new TextField() {
            @Override public void replaceText(int start, int end, String text) {
                // What I need to do is to first mock up what the result would be after
                // this modification has been made. I then need to check whether this
                // would end up being acceptable (either fully formatted with dollar symbols,
                // thousands and decimal separators, or just a number).
                String t = textField.getText() == null ? "" : textField.getText();
                t = t.substring(0, start) + text + t.substring(end);
                if (accept(t)) {
                    super.replaceText(start, end, text);
                }
            }

            @Override public void replaceSelection(String text) {
                // What I need to do is to first mock up what the result would be after
                // this modification has been made. I then need to check whether this
                // would end up being acceptable (either fully formatted with dollar symbols,
                // thousands and decimal separators, or just a number).
                String t = textField.getText() == null ? "" : textField.getText();
                int start = Math.min(textField.getAnchor(), textField.getCaretPosition());
                int end = Math.max(textField.getAnchor(), textField.getCaretPosition());
                t = t.substring(0, start) + text + t.substring(end);
                if (accept(t)) {
                    super.replaceSelection(text);
                }
            }
        };

        textField.setId("money-text-field");
        textField.getStyleClass().setAll(control.getStyleClass());
        control.getStyleClass().addListener(moneyFieldStyleClassListener = new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                textField.getStyleClass().setAll(control.getStyleClass());
            }
        });

        // Align the text to the right
        textField.setAlignment(Pos.BASELINE_RIGHT);
        textField.promptTextProperty().bind(control.promptTextProperty());
        textField.editableProperty().bind(control.editableProperty());
        textField.prefColumnCountProperty().bind(control.prefColumnCountProperty());
        
        // Whenever the value changes on the control, we need to update the text
        // in the TextField. The only time this is not the case is when the update
        // to the control happened as a result of an update in the text textField.
        control.valueProperty().addListener(moneyFieldValueListener = new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                if (!ignoreValueUpdate) {
                    updateText();
                }
            }
        });

        // Whenever the text of the textField changes, we may need to
        // update the value.
        textField.textProperty().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                updateValue();
            }
        });

        // Right now there is some funny business regarding focus in JavaFX. So
        // we will just make sure the TextField gets focus whenever somebody tries
        // to give it to the MoneyField. This isn't right, but we need to fix
        // this in JavaFX, I don't think I can hack around it
        textField.setFocusTraversable(false);
        control.focusedProperty().addListener(moneyFieldFocusListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (control.isFocused()) {
                    textField.requestFocus();
                }
            }
        });

        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                // Because TextFieldBehavior fires an action event on the parent of the TextField
                // (maybe a misfeature?) I don't need to do this. But I think this is
                // a bug, because having to add an empty event handler to get an
                // event on the control is odd to say the least!
//                control.fireEvent(new ActionEvent(textField, textField));
            }
        });

        // Make sure the text is updated to the initial state of the MoneyField value
        updateLocale();
        updateText();
    }

    @Override public MoneyField getSkinnable() {
        return control;
    }

    @Override public Node getNode() {
        return textField;
    }

    /**
     * Called by a Skinnable when the Skin is replaced on the Skinnable. This method
     * allows a Skin to implement any logic necessary to clean up itself after
     * the Skin is no longer needed. It may be used to release native resources.
     * The methods {@link #getSkinnable()} and {@link #getNode()}
     * should return null following a call to dispose. Calling dispose twice
     * has no effect.
     */
    @Override
    public void dispose() {
        control.getStyleClass().removeListener(moneyFieldStyleClassListener);
        control.valueProperty().removeListener(moneyFieldValueListener);
        control.focusedProperty().removeListener(moneyFieldFocusListener);
        textField = null;
    }

    boolean accept(String text) {
        text = text.trim();
        if (text.length() == 0) return true;
        
        // There must only be at most a single currency symbol, and it must lead.
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        final String currencySymbol = symbols.getCurrencySymbol();
        if (leadingSymbol && text.startsWith(currencySymbol)) {
            text = text.substring(currencySymbol.length()).trim();
        } else if (!leadingSymbol && text.endsWith(currencySymbol)) {
            text = text.substring(0, text.length() - currencySymbol.length()).trim();
        }

        // There must be no illegal characters.
        // If there is a thousands separator, then it must be used correctly
        // There may be only a single decimal separator
        final char thousandsSeparator = symbols.getGroupingSeparator();
        final char decimalSeparator = symbols.getMonetaryDecimalSeparator();
        boolean thousandsSeparatorInUse = false;
        int decimalSeparatorIndex = -1;
        for (int i=0; i<text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == thousandsSeparator) {
                thousandsSeparatorInUse = true;
            } else if (decimalSeparatorIndex == -1 && ch == decimalSeparator) {
                decimalSeparatorIndex = i;
            } else if (!Character.isDigit(ch)) {
                return false;
            }
        }

        // If there is a decimal separator, count the number of trailing digits and validate the length
        if (decimalSeparatorIndex != -1) {
            int trailingLength = text.substring(decimalSeparatorIndex+1).length();
            if (trailingLength > formatter.getCurrency().getDefaultFractionDigits()) return false;
        }
        
        // USD, GBP, etc can either lead or trail (TODO)
        
        // If there are thousands separators, then make sure they are correctly spaced
        if (thousandsSeparatorInUse) {
            // 43,234,234.00
            if (text.charAt(0) == thousandsSeparator) return false;
            int count = 1;
            for (int i=(decimalSeparatorIndex == -1 ? text.length()-1 : decimalSeparatorIndex-1); i>0; i--,count++) {
                if (count % 4 == 0) {
                    if (text.charAt(i) != ',') return false;
                } else {
                    if (text.charAt(i) == ',') return false;
                }
            }
        }
        return true;
    }
    
    private void updateLocale() {
        Locale locale = Locale.getDefault();
        formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        formatter.setParseBigDecimal(true);
        String s = formatter.format(BigDecimal.ZERO);
        leadingSymbol = s.charAt(0) != '0';
    }

    private void updateText() {
        BigDecimal value = control.getValue();
        textField.setText(value == null ? "" : formatter.format(value));
    }

    private void updateValue() {
        // Convert the text to a Money. Check the new Money with the one already there
        // on the MoneyField. If it has changed, then set it. Otherwise, just ignore this call.
        // Be sure to set the "ignoreValueChanged" flag so that we don't end up looping
        boolean updateText = false;
        try {
            ignoreValueUpdate = true;
            BigDecimal value = control.getValue();
            BigDecimal newValue;
            String text = textField.getText() == null ? "" : textField.getText().trim();
            // I have to clean some of this up because the formatter parsing isn't forgiving enough
            // I am probably incorrect in assuming the currency symbol goes at the front...
            Currency currency = formatter.getCurrency();
            if (text.equals("") || text.equals(currency.getSymbol())) {
                // This thing is just the symbol or empty string, so newValue is going to be 0
                newValue = value == null ? null : BigDecimal.ZERO;
            } else {
                if ((leadingSymbol && !text.startsWith(currency.getSymbol())) ||
                    (!leadingSymbol && !text.endsWith(currency.getSymbol()))) {
                    text = leadingSymbol ? currency.getSymbol() + text : text + " " + currency.getSymbol();
                    updateText = true;
                }
                Number n = formatter.parse(text);
                newValue = n instanceof BigDecimal ? (BigDecimal) n : new BigDecimal(n.doubleValue());
            }
            if (value != newValue) {
                if (value == null || newValue == null || !value.equals(newValue)) {
                    control.setValue(newValue);
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        } finally {
            ignoreValueUpdate = false;
            if (updateText) {
                // Weird bug where updating text while processing causes the caret
                // to end up in the wrong place.
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        int caretPosition = textField.getCaretPosition();
                        Currency currency = formatter.getCurrency();
                        if (leadingSymbol) {
                            textField.insertText(0, currency.getSymbol());
                            textField.positionCaret(caretPosition + currency.getSymbol().length());
                        } else {
                            textField.appendText(" " + currency.getSymbol());
                            textField.positionCaret(caretPosition);
                        }
                    }
                });
            }
        }
    }
}
