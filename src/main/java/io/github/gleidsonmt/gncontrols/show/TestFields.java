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
package io.github.gleidsonmt.gncontrols.show;

import io.github.gleidsonmt.gncontrols.*;
import io.github.gleidsonmt.gncontrols.controls.GNCheckBox;
import io.github.gleidsonmt.gncontrols.controls.GNIconButton;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.options.*;
import io.github.gleidsonmt.gncontrols.options.model.Model;
import io.github.gleidsonmt.gncontrols.options.model.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  01/01/2022
 */
public class TestFields extends Application  {

    @Override
    public void start( Stage stage)  {

        RootLayout root = new RootLayout();

        ScrollPane scrollPane = new ScrollPane();
//        scrollPane.setSkin(new ScrollPaneNoCaching(scrollPane));

        ObservableList<Item> items = FXCollections.observableArrayList(
                createTextFieldItem(),
                createButtonDefault(),
                new Item("Floating Button", new GNIconButton()),
                createHoverButton(),
                createButtonSnake(),
                new Item("Monetary", new GNMonetaryField()),
                createDatePickerItem(),
                createTextAreaItem()
        );

        root.setItems(items);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        scene.getStylesheets().addAll(
                Objects.requireNonNull(
                        getClass().getResource("/style.css")).toExternalForm()
        );

        stage.show();
//        CSSFX.start();
//        ScenicView.show(stage.getScene());

    }



    private @NotNull Item createTextFieldItem() {
        GNTextBoxB textField = new GNTextBoxB();

        textField.validProperty().bind(textField.textProperty().lessThan("2"));





//        textField.setAdditionalText("@gmail.com");
        textField.setPrefHeight(50);
        textField.setPromptText("Prompt dfdeld");
        textField.setHelperText("Password must contain an upper case letter, a numeric character, and a special character.");

//        textField.setText("The text");
//        textField.setFloatPrompt(true);
//        textField.setFieldType(FieldType.FILLED);

        Item item = new Item("Default TextField", textField);
        GNCheckBox floatPrompt = new GNCheckBox("Float Prompt");
        GNCheckBox filled = new GNCheckBox("Filled");
        GNCheckBox leadIcon = new GNCheckBox("Add Icon");
        GNCheckBox maxLength = new GNCheckBox("Max Length");
        GNCheckBox visibleHelperText = new GNCheckBox("Helper Text");
        GNCheckBox addAction = new GNCheckBox("Add Action");

        ObservableList<Model> suggestions = FXCollections.observableArrayList(
                new Person("Windows"),
                new Person("Android"),
                new Person("IOS"),
                new Person("Watch"),
                new Person("Apple")
        );

//        textField.setSuggestionList(suggestions);


        item.getOptions().addAll(floatPrompt,
                maxLength, leadIcon, filled, addAction, visibleHelperText);

        floatPrompt.selectedProperty().addListener((observable, oldValue, newValue) -> {
             textField.setFloatPrompt(newValue);
        });

        maxLength.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                textField.setCount(10);
            else textField.setCount(0);
        });

        filled.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (textField.getFieldType().equals(FieldType.FILLED)) {
                textField.setFieldType(FieldType.OUTLINED);
            } else textField.setFieldType(FieldType.FILLED);
        });

        leadIcon.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) textField.setLeadIcon(Icons.FAVORITE);
            else textField.setLeadIcon(null);
        });

        visibleHelperText.selectedProperty().addListener(
                (observable, oldValue, newValue) ->
                        textField.setVisibleHelperText(newValue)
        );

        addAction.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setTrayAction(TrayAction.CLEAR);
            } else textField.setTrayAction(TrayAction.NONE);
        });


        return item;
    }

    private Item createDatePickerItem() {
        GNDatePicker textArea = new GNDatePicker();

        textArea.setPromptText("Date Picker");
//        textArea.setFloatPrompt(true);
        textArea.setPrefHeight(50);

        textArea.setTrayAction(TrayAction.DATE_PICKER);

        GNCheckBox floatPrompt = new GNCheckBox("Float Prompt");
        GNCheckBox maxLength = new GNCheckBox("Max Lenght");
//
        Item item = new Item("Text Area", textArea);
        item.getOptions().addAll(floatPrompt, maxLength);

//        floatPrompt.selectedProperty().addListener(
//                (observable, oldValue, newValue) -> textArea.setFloatPrompt(newValue));
//
//        maxLength.selectedProperty().addListener((observable, oldValue, newValue) ->
//                textArea.setMaxLength(10));

        return item;
    }

    private Item createTextAreaItem() {
        GNTextArea textArea = new GNTextArea();

        textArea.setPromptText("Prompt text Field");
//        textArea.setFloatPrompt(true);


        GNCheckBox floatPrompt = new GNCheckBox("Float Prompt");
        GNCheckBox maxLength = new GNCheckBox("Max Lenght");
//
        Item item = new Item("Text Area", textArea);
        item.getOptions().addAll(floatPrompt, maxLength);

        floatPrompt.selectedProperty().addListener(
                (observable, oldValue, newValue) -> textArea.setFloatPrompt(newValue));

        maxLength.selectedProperty().addListener((observable, oldValue, newValue) ->
                textArea.setMaxLength(10));

        return item;
    }

    private @NotNull Item createButtonDefault() {
        GNButtonB button = new GNButtonB();
//
        GNRadioButton semi_rounded = new GNRadioButton("Semi Round");
        GNRadioButton rect = new GNRadioButton("Rect");
        GNRadioButton rounded = new GNRadioButton("Rounded");

        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(rect, rounded, semi_rounded);

        semi_rounded.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
//                button.setButtonType(GNButtonType.SEMI_ROUNDED);
            }
        });

        rect.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
//                button.setButtonType(GNButtonType.RECT);
            }
        });

        rounded.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
//                button.setButtonType(GNButtonType.ROUNDED);
            }
        });


        Item item = new Item("Default Button", button);
        item.getOptions().addAll(rect, rounded, semi_rounded);

        return item;
    }

    private Item createButtonSnake() {
        return new Item("Button Snake Animation", new GNButtonSnake());
    }

    private @NotNull Item createHoverButton() {
        GNButtonHover button = new GNButtonHover("Hover Button");
//
        GNRadioButton semi_rounded = new GNRadioButton("Semi Round");
        GNRadioButton rect = new GNRadioButton("Rect");
        GNRadioButton rounded = new GNRadioButton("Rounded");

        GNRadioButton swipe = new GNRadioButton("Swipe");
        GNRadioButton swipeDiagonal = new GNRadioButton("Swipe Diagonal");
        GNRadioButton border = new GNRadioButton("Border Animation");
        GNRadioButton smoosh = new GNRadioButton("Smoosh");
        GNRadioButton corners = new GNRadioButton("Corners");
        GNRadioButton centralize = new GNRadioButton("Centralize");
        GNRadioButton alternate = new GNRadioButton("Alternate");

        ToggleGroup type = new ToggleGroup();
        ToggleGroup hover = new ToggleGroup();
        type.getToggles().addAll(rect, rounded, semi_rounded);
        hover.getToggles().addAll(swipe,swipeDiagonal, border, smoosh, corners, centralize, alternate);

        semi_rounded.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setButtonType(GNButtonType.SEMI_ROUNDED);
            }
        });

        rect.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setButtonType(GNButtonType.RECT);
            }
        });

        rounded.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setButtonType(GNButtonType.ROUNDED);
            }
        });

        swipe.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setHoverType(GNHoverType.SWIPE);
            }
        });


        swipeDiagonal.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                button.setHoverType(GNHoverType.SWIPE_DIAGONAL);
            }
        });

        border.selectedProperty().addListener((observable, oldValue, newValue) -> {
            button.setHoverType(GNHoverType.HOVER_BORDER);
        });

        smoosh.selectedProperty().addListener((observable, oldValue, newValue) -> {
            button.setHoverType(GNHoverType.SMOOSH);
        });

        corners.selectedProperty().addListener((observable, oldValue, newValue) -> {
            button.setHoverType(GNHoverType.CORNERS);
        });

        centralize.selectedProperty().addListener((observable, oldValue, newValue) -> {
            button.setHoverType(GNHoverType.CENTRALIZE);
        });

        alternate.selectedProperty().addListener((observable, oldValue, newValue) -> {
            button.setHoverType(GNHoverType.ALTERNATE);
        });

        Item item = new Item("Button Hover Effects", button);
        item.getOptions().addAll(rect, rounded, semi_rounded, swipe, swipeDiagonal, smoosh, centralize, border, corners, alternate);

        return item;
    }
}
