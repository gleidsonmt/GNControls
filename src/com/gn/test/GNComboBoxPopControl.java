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
package com.gn.test;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  16/03/2019
 */
public class GNComboBoxPopControl extends ComboBoxPopupControl<LocalDate> {

    private final ComboBoxBase comboBox;
    
    public GNComboBoxPopControl(ComboBoxBase comboBoxBase, ComboBoxBaseBehavior behavior) {
        super(comboBoxBase, behavior);

        System.out.println(comboBoxBase.focusedProperty());

        this.comboBox = comboBoxBase;
    }

    @Override
    protected Node getPopupContent() {
        return getSkinnable().isEditable() ? ((ComboBox)getSkinnable()).getEditor() : null;
    }

    @Override
    protected TextField getEditor() {
        return getSkinnable().isEditable() ? ((ComboBox)getSkinnable()).getEditor() : null;
    }

    @Override
    protected StringConverter getConverter() {
        return ((ComboBox)getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        Node displayNode = null;
        if (comboBox.isEditable()) {
            displayNode = getEditableInputNode();
//        } else {
//            displayNode = super.buttonCell;
        }

        updateDisplayNode();

        return displayNode;
    }
}
