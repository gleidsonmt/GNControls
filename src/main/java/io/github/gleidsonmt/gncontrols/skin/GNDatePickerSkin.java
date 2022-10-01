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

package io.github.gleidsonmt.gncontrols.skin;

import io.github.gleidsonmt.gncontrols.GNDatePicker;
import io.github.gleidsonmt.gncontrols.material.icon.IconContainer;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  24/07/2022
 */
public class GNDatePickerSkin extends GNTextBoxSkinB {

    private GNDatePicker control;
    private GNDatePickerContent datePickerContent = null;
    private String initialTextFieldValue = null;
    private final PopOver popup = new PopOver();


    public GNDatePickerSkin(GNDatePicker control) {
        super(control);
        this.control = control;

        if (!getChildren().contains(getButtonTrailIcon()))
            getChildren().add(getButtonTrailIcon());

        getButtonTrailIcon().setMouseTransparent(false);
        getButtonTrailIcon().setGraphic(new IconContainer(Icons.DATE_RANGE));

        if (this.control != null) {
            datePickerContent = new GNDatePickerContent(this.control, this) ;
            configDatePicker();
        }

        new ContextMenu().getItems().add(new MenuItem());

        registerChangeListener(control.valueProperty(), e -> {
            updateDisplayNode();
            if (datePickerContent != null) {
                LocalDate date = control.getValue();
                datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
                datePickerContent.updateValues();
            }
            control.fireEvent(new ActionEvent());
        });
        
//        registerChangeListener(control.showingProperty(), e -> {
//            if (control.isShowing()) {
//                if (datePickerContent != null) {
//                    LocalDate date = control.getValue();
//                    datePickerContent.displayedYearMonthProperty().set((date != null) ? YearMonth.from(date) : YearMonth.now());
//                    datePickerContent.updateValues();
//                }
//                show();
//            } else {
//                hide();
//            }
//        });

        initialTextFieldValue = control.getText();

    }

    void updateDisplayNode() {
        if (control.getEditor() != null) {
            LocalDate value = control.getValue();
            StringConverter<LocalDate> c = control.getConverter();

            if (initialTextFieldValue != null && ! initialTextFieldValue.isEmpty()) {
                // Remainder of fix for RT-21406: ComboBox do not show initial text value
                control.getEditor().setText(initialTextFieldValue);
                initialTextFieldValue = null;
                // end of fix
            } else {
                String stringValue = c.toString(value);
                if (value == null || stringValue == null) {
                    control.getEditor().setText("");
                } else if (! stringValue.equals(control.getEditor().getText())) {
                    control.getEditor().setText(stringValue);
                }
            }
        }
    }

    private void configDatePicker() {
        resetTrayActions();
            getButtonTrailIcon().setOnMouseClicked(event -> {


            popup.setContentNode(datePickerContent);
            Bounds bounds = getButtonTrailIcon().localToScreen(getButtonTrailIcon().getBoundsInLocal());

    //            this.show(node, bounds.getMinX() - 25, bounds.getMinY() -  46);

    //            popup.show(this.control, bounds.getMinX(), bounds.getMinY());
            popup.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);

            popup.setHeaderAlwaysVisible(false);
            popup.setArrowIndent(0);
            popup.setDetached(false);
            popup.setDetachable(false);
            popup.setArrowSize(0);

            datePickerContent.setFillWidth(true);
//            datePickerContent.setPrefWidth(control.getWidth() );

    //            popup.show(this.buttonTrailIcon, -20);

            popup.show(getButtonTrailIcon(), (bounds.getMinX() + bounds.getWidth() / 2.0D) , bounds.getMinY() - 20);

        });
    }

    public void hide() {
        popup.hide();
    }


}
