/*
 *
 *  * Copyright (C) Gleidson Neves da Silveira
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.gleidsonmt.gncontrols.converters;

import io.github.gleidsonmt.gncontrols.options.GNButtonType;
import io.github.gleidsonmt.gncontrols.options.TrayAction;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

import java.util.logging.Logger;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  04/02/2022
 */
public class GNButtonTypeConverter extends StyleConverter<String, GNButtonType> {

    private GNButtonTypeConverter() {

    }

    public static StyleConverter<String, GNButtonType> getInstance() {
        return Holder.INSTANCE;
    }

    public GNButtonType convert(ParsedValue<String, GNButtonType> value, Font notUsedFont) {
        String string = value.getValue();

        System.out.println(string);
        try {
            return GNButtonType.valueOf(string.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException var5) {
            Logger.getLogger(LeadIconTypeConverter.class.getName()).info(String.format("Icon '%s' not found", string));
            return GNButtonType.RECT;
        }
    }


    @Override
    public String toString() {
        return "GNButtonTypeConverter";
    }

    public static class Holder {
        static final GNButtonTypeConverter INSTANCE = new GNButtonTypeConverter();

        private Holder() {
            throw new IllegalAccessError("Holder class");
        }
    }
}
