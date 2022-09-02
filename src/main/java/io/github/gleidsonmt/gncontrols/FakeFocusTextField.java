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

package io.github.gleidsonmt.gncontrols;

import javafx.beans.DefaultProperty;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.TextField;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  24/07/2022
 */
@DefaultProperty("control")
public class FakeFocusTextField extends TextField {

    @Override public void requestFocus() {
        if (getParent() != null) {
            getParent().requestFocus();
        }
    }

    public void setFakeFocus(boolean b) {
        setFocused(b);
    }

    /** {@inheritDoc} */
    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case FOCUS_ITEM:
                /* Internally comboBox reassign its focus the text field.
                 * For the accessibility perspective it is more meaningful
                 * if the focus stays with the comboBox control.
                 */
                return getParent();
            default: return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
