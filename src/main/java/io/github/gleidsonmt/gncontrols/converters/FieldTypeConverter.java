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

package io.github.gleidsonmt.gncontrols.converters;

import io.github.gleidsonmt.gncontrols.options.FieldType;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  24/01/2022
 */
public class FieldTypeConverter extends StyleConverter<String, FieldType> {

    private FieldTypeConverter() {

    }

    public static StyleConverter<String, FieldType> getInstance() {
        return Holder.INSTANCE;
    }

    public FieldType convert(@NotNull ParsedValue<String, FieldType> value) {
        String string = (String) value.getValue();
        try {
            return FieldType.valueOf(string.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException exception) {
            Logger.getLogger(FieldTypeConverter.class.getName())
                    .info(String.format("Invalid field type value '%s'", string));
            return FieldType.OUTLINED;
        }
    }

    private static class Holder {
            static final FieldTypeConverter INSTANCE = new FieldTypeConverter();
            private Holder() {
                throw new IllegalAccessError("Holder class");
            }
    }
}
