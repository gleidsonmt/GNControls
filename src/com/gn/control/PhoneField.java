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

import javafx.scene.control.TextField;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  09/04/2019
 */
public class PhoneField extends TextField {

    public PhoneField() {
        textProperty().addListener((observable, oldValue, newValue) -> {
            String value = getText();
            value = value.replaceAll("[^0-9]", "");
            int tam = value.length();
            value = value.replaceFirst("(\\d{2})(\\d)", "($1)$2");
            value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
            if (tam > 10) {
                value = value.replaceAll("-", "");
                value = value.replaceFirst("(\\d{5})(\\d)", "$1-$2");
            }
            setText(value);
        });
    }

    @Override
    public String getText(int start, int end) {
        if (start > end) {
            if(this.getCaretPosition() != 0) {
                if(this.getCaretPosition() < 1)
                    return getContent().get(start - 1, end);
                else
                    return getContent().get(start - 2, end);
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
