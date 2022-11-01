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

package io.github.gleidsonmt.gncontrols.controls.skin;

import io.github.gleidsonmt.gncontrols.controls.GNIconButton;
import io.github.gleidsonmt.gncontrols.controls.GNIconLabel;
import io.github.gleidsonmt.gncontrols.controls.GNSearchList;
import io.github.gleidsonmt.gncontrols.controls.text_box.FloatEditor;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.PopupWindow;
import org.controlsfx.control.PopOver;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  28/09/2022
 */
public class GNSearchListSkin<T> extends GNTextBoxBaseSkin implements ComponentSkin<GNSearchList<T>> {

    private final GNSearchList<T> control;
    private final HBox actionsContainer = new HBox();
    private final ListView<T> listContent = new ListView<>();

    private final GNIconButton arrowButton = new GNIconButton(Icons.ARROW_DROP_DOWN);
    private final GNIconButton clearButton = new GNIconButton(Icons.CLEAR);

    private final PopOver popup = new PopOver();

    public GNSearchListSkin(GNSearchList<T> _control) {
        super(_control);
        this.control = _control;

        createLeadIcon();
        createRightActions();

        bind(_control);
        setInitialState(_control);


        listContent.setFixedCellSize(50);
        listContent.setPrefHeight(_control.getItems().size() * listContent.getFixedCellSize() + 4);

        if (_control.getItems().size() > 0) {
            listContent.setMaxHeight((_control.getItems().size() * listContent.getFixedCellSize()) + 4);
        }

        _control.getItems().addListener(listUpdateSize);

        _control.itemsProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.addListener(listUpdateSize);
            }
        });

        _control.textProperty().addListener(whenTextChanged);


        listContent.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            updateDisplayText(_control);
        });

        listContent.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                updateDisplayText(_control);
                hide();
            }
        });

        arrowButton.setOnMouseClicked(event -> show());

        clearButton.setOnMouseClicked(event -> {
            _control.getEditor().clear();
            _control.getEditor().end();
            updateContainerWithArrow();
        });
    }

    private void updateDisplayText(@NotNull GNSearchList<T> _control) {
        T seleted = listContent.getSelectionModel().getSelectedItem();
        _control.setText(seleted.toString());
        _control.getEditor().end();
    }

    @Override
    public void bind(@NotNull GNSearchList<T> _control) {
        listContent.itemsProperty().bind(_control.itemsProperty());
        _control.valueProperty().bind(listContent.getSelectionModel().selectedItemProperty());
        listContent.prefWidthProperty().bind(_control.widthProperty());
    }

    @Override
    public void setInitialState(@NotNull GNSearchList<T> _control) {
        // Text state
        if (_control.getText() != null && !_control.getText().isEmpty()) {
            updateContainerFull();
        } else {
            updateContainerWithArrow();
        }

        // Settings for existance lead icon
        if (_control.getEditor() instanceof FloatEditor editor) {
            editor.setDistanceX(-30);
        }

        // About configs
        _control.getEditor().setPadding(new Insets(4, 0, 4, 0));

    }


    private void updateContainerFull() {
        control.setRightNode(actionsContainer);
        actionsContainer.setMaxWidth(60);
        actionsContainer.getChildren().setAll(clearButton, arrowButton);
    }

    private void updateContainerWithArrow() {
        control.setRightNode(actionsContainer);
        actionsContainer.setMaxWidth(30);
        actionsContainer.getChildren().setAll(arrowButton);
    }

    private void updateContainerWithClear() {
        control.setRightNode(actionsContainer);
        actionsContainer.setMaxWidth(30);
        actionsContainer.getChildren().setAll(clearButton);
    }


    public void hide() {
        popup.hide();
    }

    public void show() {
        Bounds bounds = control.localToScreen(control.getBoundsInLocal());

        popup.setContentNode(listContent);
        popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);

        popup.getRoot().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/core/theme/poplight.css")).toExternalForm()
        );

        popup.show(control.getScene().getWindow(), bounds.getMinX(), bounds.getMaxY());
    }

    private final ListChangeListener<T> listUpdateSize = new ListChangeListener<T>() {
        @Override
        public void onChanged(Change<? extends T> c) {
            if (c.next()) {
                listContent.setPrefHeight(
                        (control.getItems().size() * listContent.getFixedCellSize() + 10)
                );
            }
        }
    };

    private final ChangeListener<String> whenTextChanged = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (newValue != null && !newValue.isEmpty()) {

                if (!popup.isShowing()) show();

                if (control.getItems().size() < 1) popup.hide();

                updateContainerWithClear();
            } else {
                updateContainerWithArrow();
            }
        }
    };

    private void createRightActions() {
        arrowButton.getStyleClass().add("action-button");

        clearButton.getStyleClass().add("action-button");

        actionsContainer.getStyleClass().add("actions");
        actionsContainer.setAlignment(Pos.CENTER_RIGHT);

        actionsContainer.setFocusTraversable(false);
        arrowButton.setFocusTraversable(false);
        clearButton.setFocusTraversable(false);

        actionsContainer.setManaged(false);
        actionsContainer.setMaxWidth(60);

    }

    private void createLeadIcon() {
        GNIconLabel leadIcon = new GNIconLabel(Icons.SEARCH);
        leadIcon.getStyleClass().add("lead-icon");
        control.setLeftNode(leadIcon);
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);

        positionInArea(actionsContainer, x, y, w, h, -1, HPos.RIGHT, VPos.CENTER);
    }



}
