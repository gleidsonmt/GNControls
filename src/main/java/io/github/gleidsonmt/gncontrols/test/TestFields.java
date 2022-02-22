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
package io.github.gleidsonmt.gncontrols.test;

import io.github.gleidsonmt.gncontrols.*;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.options.model.Model;
import io.github.gleidsonmt.gncontrols.options.model.Person;
import io.github.gleidsonmt.gncontrols.options.FieldType;
import io.github.gleidsonmt.gncontrols.options.TrayAction;
import io.github.gleidsonmt.gncontrols.skin.MaskedTextField;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  01/01/2022
 */
public class TestFields extends Application implements Theme {

    @Override
    public void start( Stage stage)  {

        RootLayout root = new RootLayout();

        ObservableList<Item> items = FXCollections.observableArrayList(
                createTextFieldItemFilled(),
                createTextFieldItem(),
//                createTextFieldItemAvatarType(),
//                createComboBox(),
//                createListView(),
                createPasswordField(),
                createButtonDefault(),
                createTextAreaItem()
        );

        root.setItems(items);

        Scene scene = new Scene(root);
        setTheme(scene);
        stage.setScene(scene);

//        scene.getStylesheets().addAll(
//                Objects.requireNonNull(
//                        getClass().getResource("/style.css")).toExternalForm()
//        );


        stage.show();
//        CSSFX.start();
//        ScenicView.show(stage.getScene());
    }


    private Item createPasswordField() {
        GNPasswordBox textField = new GNPasswordBox();

        textField.setAdditionalText("@gmail.com");

        textField.setPrefHeight(50);
        textField.setPromptText("Password Field");

        textField.setHelperText("Lorem ipsum dolor color");

        Item item = new Item("TextField", textField);
        CheckBox floatPrompt = new CheckBox("Float Prompt");
        CheckBox filled = new CheckBox("Filled");
        CheckBox leadIcon = new CheckBox("Add Icon");
        CheckBox action = new CheckBox("Add Action");

        leadIcon.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) textField.setLeadIcon(Icons.FAVORITE);
            else textField.setLeadIcon(null);
        });
        floatPrompt.selectedProperty().addListener((observable, oldValue, newValue) -> textField.setFloatPrompt(newValue));

        filled.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) textField.setFieldType(FieldType.FILLED);
            else textField.setFieldType(FieldType.OUTLINED);
        });

        action.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setTrayAction(TrayAction.VIEWER);
            } else textField.setTrayAction(TrayAction.NONE);
        });

        item.getOptions().addAll(floatPrompt,
                filled, leadIcon, action);

        textField.setTrayAction(TrayAction.ICON);
        textField.setStyle("-gn-tray-action : icon;");
        System.out.println(textField.getEditor());

        return item;
    }

    private @NotNull Item createTextFieldItem() {
        GNTextBox textField = new GNTextBox();


//        textField.setAdditionalText("@gmail.com");
        textField.setPrefHeight(50);
        textField.setPromptText("Prompt dfdeld");
        textField.setHelperText("Password must contain an upper case letter, a numeric character, and a special character.");

//        textField.setText("The text");
//        textField.setFloatPrompt(true);
//        textField.setFieldType(FieldType.FILLED);

        Item item = new Item("TextField Teste", textField);
        CheckBox floatPrompt = new CheckBox("Float Prompt");
        CheckBox filled = new CheckBox("Filled");
        CheckBox leadIcon = new CheckBox("Add Icon");
        CheckBox maxLength = new CheckBox("Max Length");
        CheckBox visibleHelperText = new CheckBox("Helper Text");
        CheckBox addAction = new CheckBox("Add Action");

        ObservableList<Model> suggestions = FXCollections.observableArrayList(
                new Person("Windows"),
                new Person("Android"),
                new Person("IOS"),
                new Person("Watch"),
                new Person("Apple")
        );

//        textField.setSuggestionList(suggestions);

        textField.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Model> call(ListView<Model> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Model item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                            setItem(item);
                            setGraphic(null);
                            setCursor(Cursor.HAND);
                        } else {
                            setText(null);
                            setItem(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });

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

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("newValue = " + newValue);
        });

        VBox box = new VBox();

        return item;
    }

    private @NotNull Item createTextFieldItemFilled() {
        GNTextBox textField = new GNTextBox();

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("fuck");
            }
        });

//        textField.setAdditionalText("@gmail.com");
        textField.setPrefHeight(50);
//        textField.setPromptText("Prompt Text Field");
        textField.setHelperText("Password must contain an upper case letter, a numeric character, and a special character.");

        textField.setFieldType(FieldType.FILLED);

        Item item = new Item("TextField 1", textField);
        CheckBox floatPrompt = new CheckBox("Float Prompt");
        CheckBox leadIcon = new CheckBox("Add Icon");
        CheckBox maxLength = new CheckBox("Max Length");
        CheckBox visibleHelperText = new CheckBox("Helper Text");

        item.getOptions().addAll(floatPrompt,
                maxLength, leadIcon, visibleHelperText);

        floatPrompt.selectedProperty().addListener((observable, oldValue, newValue) -> {
            textField.setFloatPrompt(newValue);
        });

        maxLength.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                textField.setCount(10);
            else textField.setCount(0);
        });


        leadIcon.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) textField.setLeadIcon(Icons.FAVORITE);
            else textField.setLeadIcon(null);
        });

        visibleHelperText.selectedProperty().addListener(
                (observable, oldValue, newValue) ->
                        textField.setVisibleHelperText(newValue)
        );

        return item;
    }

//    private @NotNull Item createListView() {
//        ListView<String> listView = new ListView<>();
//
//        listView.getItems().addAll("Item 01", "Item 02", "Item 03");
//
//        return new Item("ListView", listView);
//    }

    private Item createTextAreaItem() {
        GNTextArea textArea = new GNTextArea();

        textArea.setPromptText("Prompt text Field");
//        textArea.setFloatPrompt(true);


        CheckBox floatPrompt = new CheckBox("Float Prompt");
        CheckBox maxLength = new CheckBox("Max Lenght");
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
        GNButton button = new GNButton();
//
        Item item = new Item("Button", button);

        return item;
    }
}