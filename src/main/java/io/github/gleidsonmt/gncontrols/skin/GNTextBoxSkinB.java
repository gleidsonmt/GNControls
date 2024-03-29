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
package io.github.gleidsonmt.gncontrols.skin;

import io.github.gleidsonmt.gncontrols.GNListView;
import io.github.gleidsonmt.gncontrols.options.model.Model;
import io.github.gleidsonmt.gncontrols.options.FieldType;
import io.github.gleidsonmt.gncontrols.material.icon.IconContainer;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.options.TrayAction;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GNTextBoxSkinB
        extends SkinBase<TextBoxB> {

    private final Timeline          animation       = new Timeline();
    private final Button            buttonLeadIcon  = new Button();
    private final Button            buttonTrailIcon = new Button();
    private final IconContainer     leadIcon        = new IconContainer();

    private TextField editor;

    private final Timeline          filledAnimation = new Timeline();
    private final Rectangle         filledBorder    = new Rectangle();

    private final double iconWidth          = 40;
    private final double iconHeight         = 30;
    private final double paddingSize        = 5;
    private final double spacing            = 2;
    private final double animationDuration  = 5000;

    private boolean      up                 = false;
    private boolean      opened             = false;
    private boolean      keep               = false;
    private boolean      shouldMaskText     = true;

    private FilteredList<Model> filteredList;
    private ListView<Model>     listView;
    private TextBoxB control;

    private final Text  addText     = new Text();
    private final Label helperText  = new Label();
    private final Label promptText  = new Label();
    private final Label countText   = new Label();

    private final ChangeListener<Boolean> clearFocus = this::setClearFocus;
    private final ChangeListener<String>  addButtonOnText = this::setAddButtonOnText;

    private static final PseudoClass FLOAT_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("float");

    private static final PseudoClass FILLED_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("filled");

    public Button getButtonTrailIcon() {
        return buttonTrailIcon;
    }

    public GNTextBoxSkinB(TextBoxB control) {
        super(control);

        this.control = control;
        this.editor = control.getEditor();



        addText.textProperty().bind(control.additionalTextProperty());
        helperText.textProperty().bind(control.helperTextProperty());
        helperText.getStyleClass().add("helper-text");

        promptText.textProperty().bind(control.promptTextProperty());

        buttonLeadIcon.setGraphic(new IconContainer(control.getLeadIcon()));
        buttonLeadIcon.setAlignment(Pos.CENTER);
        buttonLeadIcon.getStyleClass().add("lead-button");
        buttonLeadIcon.setMouseTransparent(true);

        buttonTrailIcon.getStyleClass().add("trail-button");
        buttonTrailIcon.setAlignment(Pos.CENTER);
        buttonTrailIcon.setCursor(Cursor.HAND);

        filledBorder.getStyleClass().add("filled-border");

        filledBorder.setStrokeWidth(2);

        filledBorder.setHeight(2D);
        filledBorder.setStrokeType(StrokeType.OUTSIDE);
        filledBorder.setStrokeLineCap(StrokeLineCap.ROUND);
        filledBorder.setStrokeLineJoin(StrokeLineJoin.ROUND);
        filledBorder.setSmooth(false);
        filledBorder.setStrokeMiterLimit(10);

//        promptText.textProperty().bind(control.promptTextProperty());

        configIcons(buttonLeadIcon);
        configIcons(buttonTrailIcon);

        if (!getChildren().contains(editor))
            getChildren().add(editor);

        editor.getStyleClass().add("editor");

        promptText.getStyleClass().add("prompt-text");
        promptText.setMouseTransparent(true);
        promptText.toBack();

        getChildren().add(addText);
        getChildren().add(promptText);
        getChildren().add(filledBorder);


        if (control.getLeadIcon() != null) {
            updateLeadIcon(control.getLeadIcon());
        } else getChildren().remove(buttonLeadIcon);

        if (helperText.getText() != null && control.getVisibleHelperText()) {
            getChildren().add(helperText);
        }

        helperText.setWrapText(true);
        addText.getStyleClass().add("add-text");

        editor.focusedProperty().addListener(focusedListener);

        editor.lengthProperty().addListener((observable, oldValue, newValue) -> {
            updateMaxLength();
        });

        editor.selectedTextProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) keep = true;
        });

        if (control.isFloatPrompt()) {
            if (editor.getText() != null) {
                if (!editor.getText().isEmpty()) {
                    Platform.runLater(this::upPrompt);
                }
            }
        } else {
            if (editor.getText() != null) {
                if (!editor.getText().isEmpty()) {
                    getChildren().removeAll(promptText);
                }
            }
        }

        control.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                editor.toFront();
                editor.requestFocus();
            }
        });

        configType();
        configSuggestionList();

        registerChangeListener(control.floatPromptProperty(), c -> {
            if (((boolean) c.getValue()))  editor.setStyle("-fx-prompt-text-fill : transparent;");
            else  editor.setStyle(null);
        });

        control.fieldTypeProperty().addListener((observable, oldValue, newValue) ->
                pseudoClassStateChanged(FILLED_PSEUDO_CLASS, newValue.equals(FieldType.FILLED)));

        if (control.getCount().intValue() > 0) {
            if (!getChildren().contains(countText)) {
                getChildren().add(countText);
            }
            control.addEventFilter(KeyEvent.KEY_TYPED, maxEvent);
            updateMaxLength();
        }

        registerChangeListener(control.countProperty(), c -> {
            if ( ((int) c.getValue()) < 1) {
                getChildren().remove(countText);
                control.removeEventFilter(KeyEvent.KEY_TYPED, maxEvent);
            } else {
                if (!getChildren().contains(countText)) {
                    getChildren().add(countText);
                }
                control.addEventFilter(KeyEvent.KEY_TYPED, maxEvent);
            }
            control.setVisibleCount(control.getVisibleCount());
            updateMaxLength((int) c.getValue());
        });

        registerChangeListener(control.visibleCountProperty(), c -> {
            control.setVisibleCount((Boolean) c.getValue());
            countText.setVisible(((Boolean) c.getValue()));
        });

        registerChangeListener(control.leadIconProperty(), c -> {
            if (c.getValue() != null) {
                updateLeadIcon(control.getLeadIcon());
            } else getChildren().remove(buttonLeadIcon);
//            floating(control.isFloatPrompt());
        });

        registerChangeListener(control.trayIconProperty(), c -> {
            if (c.getValue() != null) {
                updateTrayIcon(control.getTrayIcon());
            }
//            floating(control.isFloatPrompt());
        });

        registerChangeListener(control.visibleHelperTextProperty(), c -> {
            if ((boolean) c.getValue()) {
                if (!getChildren().contains(helperText)) getChildren().add(helperText);
            } else getChildren().remove(helperText);
        });

        registerChangeListener(control.trayActionProperty(), c -> {
            try {
                selectAction((TrayAction) c.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            selectAction(control.getTrayAction());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectAction(@NotNull TrayAction action) throws Exception {
        switch (action) {
            case NONE -> {
                getChildren().removeAll(buttonTrailIcon);
                buttonTrailIcon.setMouseTransparent(true);
            }
            case CLEAR -> {
                configClearAction();
                buttonTrailIcon.setMouseTransparent(false);
                buttonTrailIcon.setGraphic(new IconContainer(Icons.CLEAR));
            }
//            case VIEWER -> {
//                if (control instanceof GNPasswordBoxB) {
//                    configViewerAction();
//                    buttonTrailIcon.setGraphic(new IconContainer(Icons.VIEWER));
//                } else throw new Exception("This action is not applicable for this control.");
//            }
            case ICON -> {
//                Icons.get("viewer");
                if (!getChildren().contains(buttonTrailIcon))
                    getChildren().add(buttonTrailIcon);

//                buttonTrailIcon.setMouseTransparent(true);
                buttonTrailIcon.setGraphic(new IconContainer(control.getTrayIcon()));

            }
            case DATE_PICKER -> {

            }
        }
    }


    private void configViewerAction() {
        resetTrayActions();
        buttonTrailIcon.setOnMouseClicked(event -> {

            shouldMaskText = !shouldMaskText;

            if (shouldMaskText) {
                buttonTrailIcon.setGraphic(new IconContainer(Icons.VIEWER));
            } else {
                buttonTrailIcon.setGraphic(new IconContainer(Icons.VIEWER_OFF));
            }

            editor.setText(editor.getText());
            editor.end();

        });

        editor.textProperty().addListener(addButtonOnText);
        editor.focusedProperty().addListener(this::setClearFocus);
    }

    private void configClearAction() {
        resetTrayActions();
        buttonTrailIcon.setOnMouseClicked(event -> {
           editor.clear();
           getChildren().remove(buttonTrailIcon);
        });

        editor.focusedProperty().addListener(clearFocus);
        editor.textProperty().addListener(addButtonOnText);

//        buttonTrailIcon.toFront();

    }



    public void resetTrayActions() {
        buttonTrailIcon.setOnAction(null);
        editor.focusedProperty().removeListener(clearFocus);
        editor.textProperty().removeListener(addButtonOnText);
    }


    private final EventHandler<KeyEvent> maxEvent = new EventHandler<>() {
        @Override
        public void handle(KeyEvent event) {
            if (editor.getLength() >= control.getCount().intValue() && !keep) {
                editor.positionCaret(editor.getLength());
                event.consume();
            }
        }
    };


    private void updateMaxLength() {
        updateMaxLength(editor.getLength(), control.getCount().intValue());
    }

    private void updateMaxLength(int max) {
        updateMaxLength(editor.getLength(), max);
    }

    private void updateMaxLength(int act, int max) {
        countText.setText(act + "/" + max);
    }

    private void updateLeadIcon(Icons icon) {
        if (control.getLeadIcon() != Icons.NONE) {
            if (!getChildren().contains(buttonLeadIcon)) getChildren().add(buttonLeadIcon);
            buttonLeadIcon.setGraphic(new IconContainer(icon));
        }
        else getChildren().remove(buttonLeadIcon);
    }

    private void updateTrayIcon(Icons icon) {
        if (control.getTrayIcon() != Icons.NONE) {
            if (!getChildren().contains(buttonTrailIcon)) getChildren().add(buttonTrailIcon);
            buttonTrailIcon.setGraphic(new IconContainer(icon));
        }
//        else getChildren().remove(buttonTrailIcon);
    }


    private void configType() {
        pseudoClassStateChanged(FILLED_PSEUDO_CLASS, control.getFieldType().equals(FieldType.FILLED));
    }

    private void configIcons(Button btn) {
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btn.setFocusTraversable(false);
        btn.setPadding(Insets.EMPTY);
        btn.setPrefSize(iconWidth, iconHeight);
        btn.setMinSize(iconWidth, iconHeight);
        btn.setMaxSize(iconWidth, iconHeight);
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return editor.minWidth(height) +
                (addText.getText() != null ? addText.minWidth(height) : 0 ) +
                buttonTrailIcon.minWidth(height) +
                buttonLeadIcon.minWidth(height) +
//                helperText.minWidth(height) +
                promptText.getWidth() ;
//                01;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return editor.prefWidth(height) ;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return editor.maxWidth(height);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return iconHeight;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return control.getPrefHeight();
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return editor.prefHeight(width) ;
    }

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);

        double suffixWidth = addText.getBoundsInLocal().getWidth();

        double xWithIcons = !getChildren().contains(buttonLeadIcon) ?
                x + paddingSize * 2 : x + (iconWidth + spacing);

        double widthWithIcon = 0;

        if ( getChildren().contains(buttonLeadIcon) && getChildren().contains(buttonTrailIcon))
            widthWithIcon = w - (iconWidth * 2) - (suffixWidth + spacing);
        else if (getChildren().contains(buttonTrailIcon)) widthWithIcon = w - (iconWidth) - (suffixWidth + spacing);
        else if (getChildren().contains(buttonLeadIcon)) widthWithIcon = w - (iconWidth + suffixWidth + spacing + (paddingSize * 2));

        else widthWithIcon = w - (suffixWidth + spacing + (paddingSize * 4));


        layoutInArea(editor, xWithIcons , y, widthWithIcon , h, 0, HPos.LEFT, VPos.CENTER);
        layoutInArea(addText, (x - xWithIcons) + 2 , y, w,h, 0, HPos.RIGHT, VPos.CENTER);

        editor.setPrefHeight(control.getHeight());

        layoutInArea(buttonLeadIcon, x, y, w, h, 0, HPos.LEFT, VPos.CENTER);
        layoutInArea(buttonTrailIcon, x, y, w, h, 0, HPos.RIGHT, VPos.CENTER);

        if (up) {
            layoutInArea(promptText, xWithIcons - 4 , y, w, h, 0, HPos.LEFT, VPos.TOP);
        } else {
            layoutInArea(promptText, xWithIcons - 4 , y, w, h, 0, HPos.LEFT, VPos.CENTER);
        }


//        layoutInArea(countText, x, y + editor.getHeight() + paddingSize , w, h, 0, HPos.RIGHT, VPos.BOTTOM);

        layoutInArea(countText, x,  countText.getHeight() + paddingSize , w, h, 0, HPos.RIGHT, VPos.BOTTOM);

        double countWith = !getChildren().contains(countText) ? 0 : countText.getWidth();

        layoutInArea(helperText, x, h + paddingSize + 10,
                w - (countWith + spacing), h + helperText.getHeight(), 0, HPos.LEFT, VPos.BASELINE);

        layoutInArea(filledBorder, x, y, w, h,0, HPos.CENTER, VPos.BOTTOM);

    }

//    ChangeListener<Boolean> appearButtonTray = (observable, oldValue, newValue) -> {
//        if (newValue) {
//            if (!getChildren().contains(buttonTrailIcon)) {
//                getChildren().add(buttonTrailIcon);
//                buttonTrailIcon.setMouseTransparent(false);
//            }
//        } else getChildren().remove(buttonTrailIcon)
//    };

    ChangeListener<Boolean> focusedListener = (observable, oldValue, newValue) -> {

//        System.out.println("newValue = " + newValue);

        if (newValue) { // focused
            if (control.isFloatPrompt()) {
                if (!up) {
                    upPrompt();
                }
            } else {
                getChildren().removeAll(promptText);
            }

            if (control.getFieldType().equals(FieldType.FILLED)) {
                if (!getChildren().contains(filledBorder))
                    getChildren().add(filledBorder);
                openFilled();
            }

        } else { // no focus

            if (control.isFloatPrompt()) {
                if (editor.getText() != null) {
                    if (editor.getText().isEmpty()) {
                        downPrompt();
                    }
                } else downPrompt();
            } else {
                    if (editor.getText() == null || editor.getText().isEmpty()) {
                        if (!getChildren().contains(promptText))
                            getChildren().add(promptText);
                    }
            }

            if (control.getFieldType().equals(FieldType.FILLED)) {
                closeFilled();
            }
        }

    };

    public void upPrompt() {

        if (animation.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (up) return;

        double  tY = !control.getFieldType().equals(FieldType.FILLED) ?
                - ( getSkinnable().getHeight() / 2 ) - spacing : -(promptText.getHeight() - spacing);

//        System.out.println( promptText.getHeight() /2);

        double translateY = -( promptText.getHeight()/ 2+1);


        double translateX = control.getFieldType().equals(FieldType.FILLED) ?
                - spacing : - (iconWidth - paddingSize);
//        double  translateY = - ( editor.getHeight() ) - spacing;

        up = true;

        KeyFrame yIni = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateYProperty(), 0, Interpolator.EASE_BOTH));

        KeyFrame yEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateYProperty(), translateY, Interpolator.EASE_BOTH));

        KeyFrame xsInit = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.scaleXProperty(), 1));

        KeyFrame xsEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.scaleXProperty(), 0.9));

        KeyFrame ysInit = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.scaleYProperty(), 1));
        KeyFrame ysEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.scaleYProperty(), 0.9));

        KeyFrame xInitNoLEad = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateXProperty(), promptText.getTranslateX()));

        KeyFrame xEndNoLEad = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateXProperty(), 0));

        KeyFrame xInitLead = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateXProperty(), 0 ));

        KeyFrame xEndLead = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateXProperty(),
                        translateX));

        if (!getChildren().contains(buttonLeadIcon)) {
            animation.getKeyFrames().setAll(
                    yIni, yEnd, xsInit, xsEnd,
                    ysInit, ysEnd, xInitNoLEad, xEndNoLEad);
        } else {
            animation.getKeyFrames().setAll(
                    yIni, yEnd, xsInit, xsEnd, ysInit, ysEnd,
                    xInitLead, xEndLead);
        }
//        clipPrompt.setX();

        pseudoClassStateChanged(FLOAT_PSEUDO_CLASS, true);
        animation.play();
    }

    private void downPrompt() {
        if (animation.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (!up) return;

        up = false;

        pseudoClassStateChanged(FLOAT_PSEUDO_CLASS, false);

        KeyFrame yIni = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateYProperty(), promptText.getTranslateY(), Interpolator.EASE_BOTH));

        KeyFrame yEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateYProperty(), 0, Interpolator.EASE_BOTH));

        KeyFrame xsInit = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.scaleXProperty(), 0.9));

        KeyFrame xsEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.scaleXProperty(), 1, Interpolator.EASE_BOTH));

        KeyFrame ysInit = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.scaleYProperty(), 0.9));
        KeyFrame ysEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.scaleYProperty(), 1, Interpolator.EASE_BOTH));

        KeyFrame xInitNoLEad = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateXProperty(), promptText.getTranslateX(), Interpolator.EASE_BOTH));

        KeyFrame xEndNoLEad = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateXProperty(), 0, Interpolator.EASE_BOTH));

        KeyFrame xInitLead = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateXProperty(),  promptText.getTranslateX(), Interpolator.EASE_BOTH));

        KeyFrame xEndLead = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateXProperty(), 0));

        if (!getChildren().contains(buttonLeadIcon)) {
            animation.getKeyFrames().setAll(
                    yIni, yEnd, xsInit, xsEnd, ysInit, ysEnd, xInitNoLEad, xEndNoLEad);
        } else {
            animation.getKeyFrames().setAll(
                    yIni, yEnd, xsInit, xsEnd, ysInit, ysEnd,
                    xInitLead, xEndLead);
        }

        animation.play();
    }



    private @NotNull ListView<Model> createListView(ObservableList<Model> items) {
        return new GNListView<>(items) {

            @Override
            protected double computePrefHeight(double width) {
                return filteredList.size() * getFixedCellSize() + 5;
            }

            @Override
            protected double computeMinHeight(double width) {
                return getFixedCellSize();
            }

            @Override
            protected double computePrefWidth(double height) {
                return control.getWidth();
            }
        };
    }

    private void configSuggestionList() {
        filteredList = new FilteredList<>(control.getSuggestionList(), p -> true);
        listView = createListView(filteredList);
        listView.setCellFactory(control.getCellFactory());
        Popup popup = new Popup();

        popup.getScene().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

        popup.getContent().add(listView);

        editor.textProperty().addListener(new ChangeListener<>() {

            private boolean isEqual;

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    keep = false;

                    isEqual = filteredList.stream().anyMatch(p -> p.getName().equals(newValue));
                    filteredList.setPredicate(p -> {
                        if (!isEqual){
                            return p.getName().toLowerCase().contains(newValue.toLowerCase());
                        } else return true;
                    });

                    Bounds bounds = control.localToScreen(control.getBoundsInLocal());

                    if (!isEqual) {
                        if (filteredList.size() > 0) {

                            if (control.getFieldType().equals(FieldType.FILLED)) {
                                popup.show(control, bounds.getMinX() - 20, (bounds.getMaxY() -7 ) - helperText.getHeight());
                            } else
                            popup.show(control, bounds.getMinX() - 19, (bounds.getMaxY() -8) - helperText.getHeight());

                        }
                        else popup.hide();
                    }
                    else popup.hide();
                }
            }
        });

        editor.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                popup.hide();
            }
        });

        listView.setOnMouseClicked(event -> {
            editor.setText(listView.getSelectionModel().getSelectedItem().getName());
            filteredList.setPredicate(p -> false);
                editor.positionCaret(editor.getLength());
                popup.hide();
        });

        listView.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                editor.setText(listView.getSelectionModel().getSelectedItem().getName());
                filteredList.setPredicate(p -> false);
                editor.positionCaret(editor.getLength());
                popup.hide();
            }
        });
    }

    private void openFilled() {

        if (filledAnimation.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (opened) return;

        opened = true;

        filledAnimation.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(filledBorder.widthProperty(), 0)
                ),
                new KeyFrame(Duration.millis(animationDuration),
                        new KeyValue(filledBorder.widthProperty(), control.getWidth())
                )
        );

        filledAnimation.setOnFinished(event ->
                filledBorder.widthProperty().bind(control.widthProperty()));

        filledAnimation.play();
    }

    private void closeFilled() {

        filledBorder.widthProperty().unbind();

        if (filledAnimation.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (!opened) return;

        opened = false;

        filledAnimation.getKeyFrames().setAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(filledBorder.widthProperty(), control.getWidth())
                ),
                new KeyFrame(Duration.millis(animationDuration),
                        new KeyValue(filledBorder.widthProperty(), 0)
                )
        );

        filledAnimation.setOnFinished(event -> {
            filledBorder.widthProperty().unbind();
        });

        filledAnimation.play();
    }

    private void setClearFocus(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            if ((editor.getLength() > 0)) if (!getChildren().contains(buttonTrailIcon))
                getChildren().add(buttonTrailIcon);
        } else {
            getChildren().remove(buttonTrailIcon);
        }
    }

    private void setAddButtonOnText(ObservableValue<? extends String> observable, String oldValue, String  newValue) {
//        if (control instanceof GNPasswordBoxB) {
//            if (newValue.length() > 0 && control.getTrayAction().equals(TrayAction.VIEWER)) {
//                if (!getChildren().contains(buttonTrailIcon))
//                    getChildren().add(buttonTrailIcon);
//                buttonTrailIcon.setMouseTransparent(false);
//            } else {
//                getChildren().removeAll(buttonTrailIcon);
//            }
//        }
//        else if (newValue.length() > 0) {
//            if (!getChildren().contains(buttonTrailIcon))
//                getChildren().add(buttonTrailIcon);
//        }
    }


    private class UnMaskPasswordSkin extends TextFieldSkin {

        public UnMaskPasswordSkin(TextField control) {
            super(control);
        }

        @Override
        protected String maskText(String txt) {
            final char BULLET = '\u25cf';
            if (getSkinnable() instanceof PasswordField && shouldMaskText) {
                int n = txt.length();
                StringBuilder passwordBuilder = new StringBuilder(n);
                passwordBuilder.append(String.valueOf(BULLET).repeat(n));

                return passwordBuilder.toString();
            } else {
                return txt;
            }
        }
    }



}
