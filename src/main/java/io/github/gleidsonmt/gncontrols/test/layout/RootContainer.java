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

package io.github.gleidsonmt.gncontrols.test.layout;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  13/09/2022
 */
public class RootContainer extends SplitPane {

    private final VBox        body;
    private final StackPane   boxContainer;
    private final VBox        wrapper;
    private final AnchorPane  screenSelector;
    private final AnchorPane  boxSelector;
    private final StackPane   nodeArea;

    private final VBox details ;

    private final ObservableList<Node> corners = FXCollections.observableArrayList();
    private final Region control;

    public RootContainer(Region control) {
        this.control = control;
        this.setPadding(new Insets(20));
        details = createDeatails();

        body = createBody();
        boxContainer = createBoxContainer();
        wrapper = createWrapper();
        screenSelector = createScreenSelector();
        boxSelector = createBoxSelector();
        nodeArea = createNodeArea();

        corners.setAll(createCorners());

        screenSelector.getChildren().add(boxSelector);
        boxSelector.getChildren().addAll(nodeArea);
        boxSelector.getChildren().addAll(corners);
        boxContainer.getChildren().addAll(wrapper, screenSelector);
        body.getChildren().add(boxContainer);
        body.setPadding(new Insets(10));


        this.getItems().add(0, body);
        this.getItems().add(1, details);

        this.setDividerPosition(0, 0.8);

        nodeArea.setPadding(new Insets(10));
        nodeArea.setStyle("-fx-border-color : -mint; -fx-border-width : 2px;");
        nodeArea.getChildren().add(control);

//        boxSelector.setPrefSize(300, 200);

        boxSelector.setMinSize(control.getMinWidth(), control.getMinHeight());


    }

    private boolean exitedFromRoot = false;

    private VBox createDeatails() {
        VBox _container = new VBox();
        _container.setPadding( new Insets(5));
        Label title = new Label("Item View");
        title.getStyleClass().addAll("title");
        _container.getChildren().addAll(title, new Button("Button Test"));
        return _container;
    }

    @Contract(" -> new")
    private @NotNull StackPane createBoxContainer() {
        StackPane _container = new StackPane();
        _container.setAlignment(Pos.CENTER);
        _container.setId("box-container");

        _container.setOnMouseExited(event -> {
            exitedFromRoot = true;
        });
        _container.setOnMouseEntered(event -> exitedFromRoot = false);
        VBox.setVgrow(_container, Priority.ALWAYS);

        _container.setStyle("-fx-border-width : 2px; -fx-border-color : -light-gray;");
        return _container;
    }

    @Contract("-> new")
    private @NotNull VBox createBody() {
        VBox body = new VBox();
        Label title = new Label("View Box");
        title.getStyleClass().add("title");
        body.getChildren().addAll(title);

        return body;
    }

    @Contract(" -> new")
    private @NotNull VBox createWrapper() {
        VBox _container = new VBox();
        _container.setId("wrapper");
        return _container;
    }

    @Contract(" -> new")
    private @NotNull AnchorPane createScreenSelector() {
        AnchorPane _container = new AnchorPane();
        _container.setId("screen-selector");
        return _container;
    }

    @Contract(" -> new")
    private @NotNull AnchorPane createBoxSelector() {
        AnchorPane _container = new AnchorPane();
        _container.setId("box-selector");

        Platform.runLater(() -> {
            AnchorPane.setTopAnchor(_container, getHorizontalLine());
            AnchorPane.setLeftAnchor(_container, getVerticalLine());
        });

        return _container;
    }

    @Contract(" -> new")
    private @NotNull StackPane createNodeArea() {
        StackPane _container = new StackPane();
        _container.setId("node-area");

        AnchorPane.setTopAnchor(_container, 0D);
        AnchorPane.setRightAnchor(_container, 0D);
        AnchorPane.setBottomAnchor(_container, 0D);
        AnchorPane.setLeftAnchor(_container, 0D);

        return _container;
    }

    private @NotNull ObservableList<Node> createCorners() {
        ObservableList<Node> corners = FXCollections.observableArrayList();

        Circle topLeft = createCircle();
        posInTopLeft(topLeft);

        Circle topRight = createCircle();
        posInTopRight(topRight);

        Circle bottomRight = createCircle();
        posInBottomRight(bottomRight);

        Circle bottomLeft = createCircle();
        posInBottomLeft(bottomLeft);

        corners.addAll(topLeft, topRight, bottomRight, bottomLeft);
        corners.addAll(createInfoHeight(), createInfoWidth());

        return corners;
    }

    private Label createInfoHeight() {
        Label _info = new Label();
//        lblChange.textProperty().bind(Bindings.format(MoneyUtil.getSymbol() + " %.2f ",
//                Bindings.select(transactionTable.getSelectionModel().selectedItemProperty(), "change")
//        ));
//
        _info.textProperty().bind(
                Bindings.format("H : %.2f", control.heightProperty())
        );

//        Bindings.fo

        Platform.runLater(() -> {
            AnchorPane.setTopAnchor(_info, 0D);
            AnchorPane.setRightAnchor(_info, -_info.getWidth() -15 );
            AnchorPane.setBottomAnchor(_info, 0D);
        });

        return _info;
    }

    private Label createInfoWidth() {
        Label _info = new Label();
        _info.setAlignment(Pos.CENTER_RIGHT);
//
        _info.textProperty().bind(
                Bindings.format("W : %.2f", control.widthProperty())
        );

//        Bindings.fo

        Platform.runLater(() -> {
            AnchorPane.setLeftAnchor(_info, 0D);
            AnchorPane.setRightAnchor(_info, 0D);
            AnchorPane.setBottomAnchor(_info, -_info.getHeight() -15 );
        });

        return _info;
    }

    private @NotNull Circle createCircle() {
        Circle circle = new Circle();
        circle.setRadius(10D);

        circle.setStyle("-fx-stroke : -mint;" +
                "-fx-fill : white;");

        return circle;
    }

    private VBox createPadding() {

        VBox _container = new VBox();
        return _container;
    }

    private void posInTopLeft(Node node) {
        AnchorPane.setTopAnchor(node, -10D);
        AnchorPane.setLeftAnchor(node, -10D);
        node.setCursor(Cursor.SE_RESIZE);
        node.setId("top-left");
        node.setOnMousePressed(this::fixedOnTopLeft);
        node.setOnMouseDragged(this::resizeFromTopLeft);
    }


    private void posInTopRight(Node node) {
        AnchorPane.setTopAnchor(node, -10D);
        AnchorPane.setRightAnchor(node, -10D);
        node.setCursor(Cursor.SW_RESIZE);
        node.setId("top-right");
        node.setOnMousePressed(this::fixedOnTopRight);
        node.setOnMouseDragged(this::resizeFromTopRight);
    }

    private void posInBottomRight(Node node) {
        AnchorPane.setBottomAnchor(node, -10D);
        AnchorPane.setRightAnchor(node, -10D);
        node.setCursor(Cursor.SE_RESIZE);
        node.setId("bottom-right");

        node.setOnMousePressed(this::fixedOnBottomRight);
        node.setOnMouseDragged(this::resizeFromBottomRight);
    }

    private void posInBottomLeft(Node node) {
        AnchorPane.setBottomAnchor(node, -10D);
        AnchorPane.setLeftAnchor(node, -10D);
        node.setCursor(Cursor.SW_RESIZE);
        node.setId("bottom-left");

        node.setOnMousePressed(this::fixedOnBottomLeft);
        node.setOnMouseDragged(this::resizeFromBottomLeft);
    }

    /*********************************************************************************
     *
     *                      Resize Methods
     *
     ********************************************************************************/

    private double initX;
    private double initY;

    private double newX;
    private double newY;

    private double maxW;
    private double maxH;

    private double deltaX;
    private double deltaY;

    private void fixedOnTopLeft(MouseEvent event) {

        // Gets the initial cordinates and clamp with the size of circle
        initY = clampPointerTopY(event);
        initX = clampPointerForLeftX(event);

        // Remove old anchors
        clearConstraints(boxSelector);

        // Get the min x and y postion (Relative to screenSelector)
        double _minX =  (boxSelector.getLocalToParentTransform().getTx() ) ;
        double _minY =  (boxSelector.getLocalToParentTransform().getTy()  ) ;

        // Get the maxX and maxY position in the parent (Relative to screenSelector)
        double _maxX = boxContainer.getWidth() - (_minX + boxSelector.getWidth());
        double _maxY = screenSelector.getHeight() - (_minY + boxSelector.getHeight());

        // Anchor positions (Relative to screenSelector)
        AnchorPane.setRightAnchor(boxSelector, _maxX );
        AnchorPane.setBottomAnchor(boxSelector, _maxY );


    }

    private void resizeFromTopLeft(@NotNull MouseEvent event) {

        if (!event.isPrimaryButtonDown()
                || (initX == -1 && initY == -1)) return; // if out of bounds and not the primary button(left)

        if (event.isStillSincePress()) return; // if the mouse doesn't continuos pressed

        // get the new x positions
        newX = event.getSceneX();
        newY = event.getSceneY();

        // get the difference from initial clicked to dragg
        deltaX = newX - initX;
        deltaY = newY - initY;

        retainPrefSize();

        // get the bounds for the box
        double maxX = (screenSelector.getLocalToSceneTransform().getTx() -2);
        double maxY = (screenSelector.getLocalToSceneTransform().getTy() -2);

        // don't leave from the limit
        if (newY > maxY)
            setNodeHeight(boxSelector.getPrefHeight() - deltaY);
        if (newX > maxX)
            setNodeWidth(boxSelector.getPrefWidth()   - deltaX);
    }

    private void fixedOnTopRight(MouseEvent event) {

        // Gets the initial cordinates and clamp with the size of circle
        initY = clampPointerTopY(event);
        initX = clampPointerForRightX(event);

        // Remove old anchors
        clearConstraints(boxSelector);

        // Get the min x and y postion (Relative to screenSelector)
        double _minX =  (boxSelector.getLocalToParentTransform().getTx() ) ;
        double _minY =  (boxSelector.getLocalToParentTransform().getTy()  ) ;


        // Get the maxX and maxY position in the parent (Relative to screenSelector)
        double _maxX = boxContainer.getWidth() - (_minX + boxSelector.getWidth());
        double _maxY = screenSelector.getHeight() - (_minY + boxSelector.getHeight());

        // Anchor positions (Relative to screenSelector)
        AnchorPane.setLeftAnchor(boxSelector, _minX );
        AnchorPane.setBottomAnchor(boxSelector, _maxY );
    }

    private void resizeFromTopRight(@NotNull MouseEvent event) {

        if (!event.isPrimaryButtonDown() || (initX == -1 && initY == -1)) return; // if out of bounds and not the primary button(left)
        if (event.isStillSincePress()) return; // if the mouse doesn't continuos pressed

        if (exitedFromRoot) return;

        // get the new x positions
        newX = event.getSceneX();
        newY = event.getSceneY();

        // get the difference from initial clicked to dragg
        deltaX = newX - initX;
        deltaY = newY - initY;

        retainPrefSize();

        // get the bounds for the box
        double maxX = (screenSelector.getLocalToSceneTransform().getTx() + screenSelector.getWidth()) -2;
        double maxY = (screenSelector.getLocalToSceneTransform().getTy() -2);

        if (newX < maxX) setNodeWidth(boxSelector.getPrefWidth() + deltaX);
        if (newY > maxY) setNodeHeight(boxSelector.getPrefHeight() - deltaY);

    }

    private void fixedOnBottomLeft(MouseEvent event) {

//        btnLockInTop.setVisible(true);

        initY = clampPointerBottomY(event);
        initX = clampPointerForLeftX(event);

        clearConstraints(boxSelector);

        double _minX =  (boxSelector.getLocalToParentTransform().getTx()  ) ;
        double _minY =  (boxSelector.getLocalToParentTransform().getTy()  ) ;

        double _maxX = boxContainer.getWidth() - (_minX + boxSelector.getWidth());

        AnchorPane.setRightAnchor( boxSelector, _maxX );
        AnchorPane.setTopAnchor( boxSelector, _minY );

    }

    private void resizeFromBottomLeft(@NotNull MouseEvent event) {

        if (!event.isPrimaryButtonDown()
                || (initX == -1 && initY == -1)) return; // if out of bounds and not the primary button(left)

        if (event.isStillSincePress()) return; // if the mouse doesn't continuos pressed

        if (exitedFromRoot) {
            event.consume();
            return;
        }

        // get the new x positions
        newX = event.getSceneX();
        newY = event.getSceneY();

        // get the difference from initial clicked to dragg
        deltaX = newX - initX;
        deltaY = newY - initY;

        retainPrefSize();

        // get the bounds for the box limit
        double maxY = (screenSelector.getLocalToSceneTransform().getTy() + screenSelector.getHeight()) -2;
        double maxX = (screenSelector.getLocalToSceneTransform().getTx() -2);

        // don't leave from the limit
        if ( newY < maxY )  setNodeHeight(boxSelector.getPrefHeight() + deltaY);
        if ( newX > maxX )  setNodeWidth (boxSelector.getPrefWidth()  - deltaX);

    }

    private void fixedOnBottomRight(MouseEvent event) {

//        btnLockInTop.setVisible(true);

        initY = clampPointerBottomY(event);
        initX = clampPointerForRightX(event);

        clearConstraints(boxSelector);
//
        double _minX =  (boxSelector.getLocalToParentTransform().getTx()  ) ;
        double _minY =  (boxSelector.getLocalToParentTransform().getTy()  ) ;

        AnchorPane.setTopAnchor( boxSelector, _minY );
        AnchorPane.setLeftAnchor( boxSelector, _minX );

    }

    private void resizeFromBottomRight(@NotNull MouseEvent event) {

        if (!event.isPrimaryButtonDown()
                || (initX == -1 && initY == -1)) return; // if out of bounds and not the primary button(left)

        if (event.isStillSincePress()) return; // if the mouse doesn't continuos pressed

        // get the new x positions
        newX = event.getSceneX();
        newY = event.getSceneY();

        // get the difference from initial clicked to dragg
        deltaX = newX - initX;
        deltaY = newY - initY;

        retainPrefSize();

        double maxX = (screenSelector.getLocalToSceneTransform().getTx() + screenSelector.getWidth())  -2; // 2 is the border width
        double maxY = (screenSelector.getLocalToSceneTransform().getTy() + screenSelector.getHeight()) -2;

        // don't out the limits
        if (newX < maxX) setNodeWidth(boxSelector.getPrefWidth() + deltaX);
        if (newY < maxY) setNodeHeight(boxSelector.getPrefHeight() + deltaY);

    }


    /*********************************************************************************
     *
     *                      UTIL
     *
     ********************************************************************************/

    private double getHorizontalLine() {
        return (screenSelector.getWidth() / 2) - (nodeArea.getWidth() /2);
    }

    private double getVerticalLine() {
        return (screenSelector.getHeight() / 2) - (nodeArea.getHeight() /2);
    }

    private void retainPrefSize() {
        if (boxSelector.getPrefWidth() <= 0) {
            boxSelector.setPrefWidth(control.getWidth() + 24); // 10 padding both sides + border size both sides
        }

        if (boxSelector.getPrefHeight() <= 0) {
            boxSelector.setPrefHeight(control.getHeight() + 24); // 10 padding both sides + border size both sides
        }
    }

    private void clearConstraints(Node node) {
        AnchorPane.clearConstraints(node);
    }

    private double clampPointerForLeftX(@NotNull MouseEvent event) {
        double min = boxSelector.getLocalToSceneTransform().getTx();
        double _count = event.getSceneX() - min;
        double _sum = event.getSceneX() - _count;
        return  _sum - 1;
    }

    private double clampPointerForRightX(@NotNull MouseEvent event) {
        double min = (boxSelector.getLocalToSceneTransform().getTx() + boxSelector.getWidth()) -2;
        double _count = event.getSceneX() - min;
        double _sum = event.getSceneX() - _count;
        return  _sum - 1;
    }

    private double clampPointerTopY(@NotNull MouseEvent event) {
        double min = boxSelector.getLocalToSceneTransform().getTy();
        double _count = event.getSceneY() - min;
        double _sum = event.getSceneY() - _count;
        return _sum -1;
    }

    private double clampPointerBottomY(@NotNull MouseEvent event) {
        double min = (boxSelector.getLocalToSceneTransform().getTy() + boxSelector.getHeight()) -2; // two border width
        double _count = event.getSceneY() - min;
        double _sum = event.getSceneY() - _count;
        return _sum -1;
    }

    private void setNodeWidth(double width) {
        if (width >= boxSelector.getMinWidth()) {
            boxSelector.setPrefWidth(width);
            initX = newX;
        }
    }

    private void setNodeHeight(double height) {
        if (height >= boxSelector.getMinHeight()) {
            boxSelector.setPrefHeight(height);
            initY = newY;
        }
    }

}
