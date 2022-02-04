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
package io.github.gleidsonmt.gncontrols.skin;

import io.github.gleidsonmt.gncontrols.GNTextArea;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  14/01/2022
 */
public class GNTextAreaSkin extends TextAreaSkin {

    private final Timeline animation = new Timeline();

    private final double paddingSize = 5;
    private final double spacing = 2;
    double animationDuration = 100;
    private boolean up = false;
    private boolean  keep= false;

    private final Label helperText = new Label();
    private final Label promptText = new Label("Prompt Text Field");
    private final Label countText = new Label("100/100");

    private GNTextArea control;

    private static final PseudoClass FLOAT_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("float");

    private final Path caret;

    ScrollPane scrollPane ;
    Region region ;

    public GNTextAreaSkin(GNTextArea control) {
        super(control);
        this.control = control;

        promptText.textProperty().bindBidirectional(control.promptTextProperty());
        promptText.getStyleClass().add("prompt-text");
        promptText.setMouseTransparent(true);
        promptText.toBack();

        control.focusedProperty().addListener(focusedListener);

        hidePrompt();

        if (control.getMaxLength().intValue() > 0) {
            getChildren().add(countText);
            updateMaxLength();
        }

        registerChangeListener(control.floatPromptProperty(), c -> {
            updateFloating((boolean) c.getValue());
        });


        scrollPane = (ScrollPane) getChildren().get(0);
        region = (Region) scrollPane.getContent();

        caret = (Path) region.getChildrenUnmodifiable().get(3);

        control.selectedTextProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) keep = true;
        });

        control.textProperty().addListener(this::changed);

        control.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (control.getText() != null) {
                if (control.getText().startsWith(" "))
                    updateMaxLength(newValue.intValue() - 1);
                else updateMaxLength(newValue.intValue());
            }
        });

        registerChangeListener(control.maxLengthProperty(), c -> {
            if ( ((int) c.getValue()) < 1) {
                getChildren().remove(countText);
                control.removeEventFilter(KeyEvent.KEY_TYPED, maxEvent);
            } else {
                if (!getChildren().contains(countText)) {
                    getChildren().add(countText);
                }
                control.addEventFilter(KeyEvent.KEY_TYPED, maxEvent);

            }
            updateMaxLength((int) c.getValue());
        });

        control.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            System.out.println(event.getCode());
        });



//        Text text = (Text) region.getChildrenUnmodifiable().get(0);
//        text.setOpacity(0);

    }

    private final EventHandler<KeyEvent> maxEvent = new EventHandler<>() {
        @Override
        public void handle(KeyEvent event) {
            System.out.println(event.getEventType());
            if (control.getLength() >= control.getMaxLength().intValue() && !keep) {
                if (control.getMaxLength().intValue() != -1)
                    event.consume();
            }
        }
    };

    private void updateMaxLength() {
        updateMaxLength(control.getLength(), control.getMaxLength().intValue());
    }

    private void updateMaxLength(int act) {
        countText.setText(act + "/" + control.getMaxLength().intValue());
    }

    private void updateMaxLength(int act, int max) {
        countText.setText(act + "/" + max);
    }

    private void updateFloating(boolean value) {
//        hidePrompt();
        if (value) {

            if (!getChildren().contains(promptText))
                getChildren().add(promptText);

            if (control.getText() != null) {
                if (!control.getText().isEmpty()) {
//                    double translateY = -(promptText.getHeight() + 1);
                    promptText.setTranslateY(-22);

                    promptText.setTranslateX(0 - spacing);

                    promptText.setScaleX(0.9);
                    promptText.setScaleY(0.9);
                    up = true;
                }
            }


        } else {

            getChildren().removeAll(promptText);

            promptText.setTranslateX(0);
            promptText.setTranslateY(0);
            promptText.setScaleX(1);
            promptText.setScaleY(1);
            up = false;
        }
    }


    public void hidePrompt(boolean value) {
        if (value) {
            control.setStyle("-fx-prompt-text-fill : transparent;");
            if (!getChildren().contains(promptText)) {
                getChildren().add(promptText);
            } else {
                control.setStyle(null);
                getChildren().remove(promptText);
            }
        }
    }

    private void hidePrompt() {
        if (control.isFloatPrompt()) {
            control.setStyle("-fx-prompt-text-fill : transparent;");
            if (!getChildren().contains(promptText)) {
                getChildren().add(promptText);
            }
        } else {
            control.setStyle(null);
            getChildren().remove(promptText);
        }
    }

    ChangeListener<Boolean> focusedListener = (observable, oldValue, newValue) -> {

        if (newValue) { // focused

            if (control.isFloatPrompt()) {
                if (!up) {
                    upPrompt();
                }
            } else {
                getChildren().removeAll(promptText);
            }

//            if (control.getFieldType().equals(FieldType.FILLED)) {
//                openFilled();
//            }

        } else { // no focus
            if (control.isFloatPrompt()) {
                if (control.getText() != null) {
                    if (control.getText().isEmpty()) {
                        downPrompt();
                    }
                } else downPrompt();
            } else {
                if (control.getText() == null || control.getText().isEmpty()) {
                    if (!getChildren().contains(promptText))
                        getChildren().add(promptText);
                }
            }


//            if (textField.getFieldType().equals(FieldType.FILLED)) {
//                closeFilled();
//            }
        }

    };

    @Override
    protected void layoutChildren(double x, double y, double w, double h) {
        super.layoutChildren(x, y, w, h);

        // ref to a padding
        layoutInArea(promptText, x + 6, y + 10, w, h, 8, HPos.LEFT, VPos.TOP);
        layoutInArea(countText, x, y + 22, w, h, 0, HPos.RIGHT, VPos.BOTTOM);

    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return promptText.getWidth();
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return promptText.prefWidth(height);
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return  countText.getHeight();
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return countText.getHeight();
    }

    public void upPrompt() {

        if (control.getText() == null) control.setText("");

        if (animation.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (up) return;

        double  translateY = - ( promptText.getHeight()  +1);

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
                new KeyValue(promptText.translateXProperty(), 0 - spacing));

        animation.getKeyFrames().setAll(
                yIni, yEnd, xsInit, xsEnd,
                ysInit, ysEnd, xInitNoLEad, xEndNoLEad);

        pseudoClassStateChanged(FLOAT_PSEUDO_CLASS, up);
        animation.play();

    }

    private void downPrompt() {

        hidePrompt();

        if (animation.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (!up) return;

        up = false;

        KeyFrame yIni = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateYProperty(), promptText.getTranslateY(), Interpolator.EASE_BOTH));

        KeyFrame yEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateYProperty(), 0, Interpolator.EASE_BOTH));

        KeyFrame xsInit = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.scaleXProperty(), 0.9));

        KeyFrame xsEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.scaleXProperty(), 1));

        KeyFrame ysInit = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.scaleYProperty(), 0.9));
        KeyFrame ysEnd = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.scaleYProperty(), 1));

        KeyFrame xInitNoLEad = new KeyFrame(Duration.ZERO,
                new KeyValue(promptText.translateXProperty(), promptText.getTranslateX()));

        KeyFrame xEndNoLEad = new KeyFrame(Duration.millis(animationDuration),
                new KeyValue(promptText.translateXProperty(), 0));

        animation.getKeyFrames().setAll(
                yIni, yEnd, xsInit, xsEnd, ysInit, ysEnd, xInitNoLEad, xEndNoLEad);

        pseudoClassStateChanged(FLOAT_PSEUDO_CLASS, up);

        animation.play();
    }

    private void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
            keep = false;
        }
    }


}
