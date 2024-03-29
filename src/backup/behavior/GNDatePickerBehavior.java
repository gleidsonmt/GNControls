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
package io.gleidsonmt.controls.behavior;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import io.gleidsonmt.controls.control.GNDatePicker;
import io.gleidsonmt.controls.skin.GNDatePickerSkin;
import javafx.scene.input.KeyCode;

import java.time.LocalDate;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/03/2019
 */
public class GNDatePickerBehavior extends ComboBoxBaseBehavior<LocalDate> {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     *
     */
    public GNDatePickerBehavior(final GNDatePicker datePicker) {
        super(datePicker, DATE_PICKER_BINDINGS);
    }

    /***************************************************************************
     *                                                                         *
     * Key event handling                                                      *
     *                                                                         *
     **************************************************************************/

    protected static final List<KeyBinding> DATE_PICKER_BINDINGS = new ArrayList<KeyBinding>();
    static {

        DATE_PICKER_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "today"));

        DATE_PICKER_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
    }

    @Override public void onAutoHide() {
        // when we click on some non-interactive part of the
        // calendar - we do not want to hide.
        GNDatePicker datePicker = (GNDatePicker) getControl();
        GNDatePickerSkin cpSkin = (GNDatePickerSkin)datePicker.getSkin();
        cpSkin.syncWithAutoUpdate();
        // if the DatePicker is no longer showing, then invoke the super method
        // to keep its show/hide state in sync.
        if (!datePicker.isShowing()) super.onAutoHide();
    }
}
