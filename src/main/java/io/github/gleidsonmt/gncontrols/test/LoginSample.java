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

package io.github.gleidsonmt.gncontrols.test;

import io.github.gleidsonmt.gncontrols.*;
import io.github.gleidsonmt.gncontrols.controls.*;
import io.github.gleidsonmt.gncontrols.controls.GNIconButton;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.options.FieldType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  13/09/2022
 */
public class LoginSample extends Application {

    @Override
    public void start(@NotNull Stage stage) {

        VBox root = new VBox();
        root.setPadding(new Insets(20));

        root.getChildren().addAll(createHeader(), createContent(), createFooter());

//        stage.setResizable(false);

        Scene scene = new Scene(root, 480, 600);
        new Material(scene, Theme.DARK_INFO);

        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/login.css")).toExternalForm()
        );

        stage.setScene(scene);
        stage.show();

//        CSSFX.start(scene);
//        ScenicView.show(scene);

    }



    private @NotNull StackPane createHeader() {

        StackPane container = new StackPane();
        container.setAlignment(Pos.TOP_RIGHT);

        GNIconButton closeButton = new GNIconButton();
        closeButton.setCursor(Cursor.HAND);
        closeButton.setIcon(Icons.CLEAR);

        closeButton.setOnMouseClicked(event -> {
            Platform.exit();
        });

        closeButton.getStyleClass().add("close-button");

        GNIconButton keyButton = new GNIconButton();
        keyButton.setIcon(Icons.VPN_KEY_FILLED);
        keyButton.getStyleClass().add("key-button");

        Label title = new Label("Log in");
        title.getStyleClass().addAll("h2", "title");

        title.setGraphic(keyButton);
        title.setGraphicTextGap(10);

        Text text = new Text("Become a member - you'll enjoy exclusive delas, offers, invites and rewards");
        text.getStyleClass().addAll("text-18", "custom-text");
        TextFlow textFlow = new TextFlow(text);

        VBox header = new VBox();
        header.setAlignment(Pos.TOP_LEFT);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        VBox.setMargin(separator, new Insets(10,0,10,0));

        header.getChildren().addAll(title, textFlow, separator);

        container.getChildren().addAll(header, closeButton);
        return container;
    }

    private StackPane createContent () {

        StackPane container = new StackPane();
        container.setAlignment(Pos.CENTER);

        GNTextBox emailField = new GNTextBox();
        emailField.setText("gleidsonm@gmail.com");
        emailField.setAnimated(true);
        emailField.setFieldType(FieldType.FILLED);
        emailField.setPromptText("Input your email");
        emailField.setIcon(Icons.MAIL);
        emailField.setAction(true);
        emailField.getStyleClass().add("input-form");
        emailField.setHelperText("This field needs more than three words.");
        emailField.setCount(50);
//        emailField.setPrefHeight(70);

//        emailField.validProperty().bind(
//                IntegerBinding.integerExpression(Bindings.createIntegerBinding(emailField.textProperty().length()) > 2)
//        );


        emailField.validProperty().bind(
                Bindings.createBooleanBinding(
                        () -> emailField.getText() != null && emailField.getText().length() > 5, emailField.textProperty()
                )
        );

//        System.out.println("emailField = " + intBintdin);

        GNPasswordBox passField = new GNPasswordBox();
        passField.setText("My tesxt");
        passField.getStyleClass().add("input-form");
        passField.setAnimated(true);
        passField.setPromptText("Input your pass");
        passField.setIcon(Icons.LOCK);
        passField.setAction(true);
        passField.setHelperText("The minimal size is 8");

        passField.validProperty().bind(
                Bindings.createBooleanBinding(
                        () -> passField.getText() != null && passField.getText().length() > 8, passField.textProperty()
                )
        );

//        passField.setPrefHeight(70);
        passField.setCount(50);

        VBox content = new VBox();
        content.setSpacing(5);
        content.setAlignment(Pos.CENTER);

        GNButton loginButton = new GNButton();
        loginButton.setText("Log in");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(50);
        loginButton.getStyleClass().addAll("text-14");

        GridPane remenberBox = new GridPane();

        GNCheckBox remenberCheck = new GNCheckBox();
        remenberCheck.setText("Remember me");

        GNHyperLink forgotPass = new GNHyperLink();
        forgotPass.setText("Forgot password?");

        remenberBox.getChildren().addAll(remenberCheck, forgotPass);

        remenberBox.setPadding(new Insets(10));
        GridPane.setConstraints(remenberCheck, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER, Priority.SOMETIMES, Priority.ALWAYS);
        GridPane.setConstraints(forgotPass, 1, 0, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.SOMETIMES, Priority.ALWAYS);

        content.getChildren().addAll(emailField, passField, remenberBox, loginButton);
        VBox.setVgrow(container, Priority.ALWAYS);

        loginButton.setOnAction(event -> {
            emailField.validate();
            passField.validate();
//            emailField.validate();
        });

        container.getChildren().addAll(content);
        return container;
    }

    private Node createFooter() {

        VBox content = new VBox();
        Separator separator = new Separator();
        content.setAlignment(Pos.CENTER);
        content.setPrefHeight(50);

        GNHyperLink hyperLink = new GNHyperLink();
        hyperLink.setText("Sign up");
        hyperLink.getStyleClass().addAll("text-14");

        Text information = new Text("Don't have an account?");
        information.getStyleClass().addAll("text-14");

        HBox infoBox = new HBox();
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getChildren().setAll(information, hyperLink);
        VBox.setVgrow(infoBox, Priority.ALWAYS);

        content.getChildren().addAll(separator, infoBox);

        return content;
    }

    public static void main(String[] args) {

        launch(args);

    }
}
