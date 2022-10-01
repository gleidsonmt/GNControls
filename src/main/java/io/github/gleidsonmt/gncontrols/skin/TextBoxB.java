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

import io.github.gleidsonmt.gncontrols.converters.FieldTypeConverter;
import io.github.gleidsonmt.gncontrols.converters.LeadIconTypeConverter;
import io.github.gleidsonmt.gncontrols.converters.TrayActionConverter;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.options.model.Model;
import io.github.gleidsonmt.gncontrols.options.FieldType;
import io.github.gleidsonmt.gncontrols.options.TrayAction;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.SizeConverter;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  14/02/2022
 */
public class TextBoxB extends Control {

    private final ObjectProperty<Callback<ListView<Model>, ListCell<Model>>> cellFactory =
            new SimpleObjectProperty<>(this, "cellFactory");

    private final StringProperty additionalText = new SimpleStringProperty();
    private final StringProperty helperText     = new SimpleStringProperty();

    private final ObjectProperty<ObservableList<Model>> suggestionList =
            new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private final ObjectProperty<TextField> editor = new SimpleObjectProperty<>();

    private final BooleanProperty valid = new SimpleBooleanProperty(true);

    private static final PseudoClass ERROR_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("error");

    private static final PseudoClass CHECKED_PSEUDO_CLASS =
            PseudoClass.getPseudoClass("checked");


    public TextBoxB() {

        getStyleClass().add("text-box");
        setFocusTraversable(false);

        validProperty().addListener((observable, oldValue, newValue) -> {
           pseudoClassStateChanged(ERROR_PSEUDO_CLASS, !newValue);
        });

    }

    public void reset() {
        valid.set(true);
        pseudoClassStateChanged(ERROR_PSEUDO_CLASS, false);
        pseudoClassStateChanged(CHECKED_PSEUDO_CLASS, false);
    }

    protected void createPasteAction(TextField textField) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();

        if (clipboard.hasString()) {
            final String text = clipboard.getString();
            if (text != null) {
                if (getVisibleCount()) {
                    int major = text.length();
                    int comparator = getCount().intValue() - textField.getLength();
                    String sub ;
                    if ( (major + textField.getLength()) < getCount().intValue() ) {
                        //
                        textField.replaceSelection(text);
                    } else {
                        sub = text.substring(0,
                                comparator);
                        textField.replaceSelection(sub);
                    }
                } else {
                    textField.replaceSelection(text);
                }
            }
        }
    }
    
    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    private final StyleableBooleanProperty floatPrompt
            = new StyleableBooleanProperty(false) {

        public CssMetaData<TextBoxB, Boolean> getCssMetaData() {
            return StyleableProperties.FLOAT_PROMPT;
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "floatPrompt";
        }
    };

    private final StyleableObjectProperty<FieldType> fieldType
            = new StyleableObjectProperty<>(FieldType.OUTLINED)  {

        @Override
        public CssMetaData<? extends Styleable, FieldType> getCssMetaData() {
            return StyleableProperties.FIELD_TYPE;
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "fieldType";
        }
    };

    private final StyleableObjectProperty<Boolean> visibleHelperText =
            new StyleableObjectProperty<>(false) {
                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "visibleHelperText";
                }

                @Override
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.VISIBLE_HELPER_TEXT;
                }
            };

    private final StyleableObjectProperty<Boolean> visibleCount =
            new StyleableObjectProperty<>(false) {
                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "visibleCount";
                }

                @Override
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.VISIBLE_COUNT;
                }
            };

    private final StyleableObjectProperty<Number> count =
            new StyleableObjectProperty<>(0) {
                @Override
                public Object getBean() {
                    return this;
                }

                @Override
                public String getName() {
                    return "count";
                }

                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.COUNT;
                }
            };

    private final StyleableObjectProperty<Icons> leadIcon
            = new StyleableObjectProperty<>(Icons.NONE) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "leadIcon";
        }

        @Override
        public CssMetaData<? extends Styleable, Icons> getCssMetaData() {
            return StyleableProperties.LEAD_ICON;
        }
    };

    private final StyleableObjectProperty<Icons> trayIcon
            = new StyleableObjectProperty<>(Icons.NONE) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "trayIcon";
        }

        @Override
        public CssMetaData<? extends Styleable, Icons> getCssMetaData() {
            return StyleableProperties.TRAY_ICON;
        }
    };

    private final StyleableObjectProperty<TrayAction> trayAction
            = new StyleableObjectProperty<>(TrayAction.NONE) {

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "trayAction";
        }

        @Override
        public CssMetaData<? extends Styleable, TrayAction> getCssMetaData() {
            return StyleableProperties.TRAY_ACTION;
        }
    };

    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        if (this.STYLEABLES == null) {
            List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(getClassCssMetaData());
            styleables.addAll(getClassCssMetaData());
            styleables.addAll(Control.getClassCssMetaData());
            this.STYLEABLES = Collections.unmodifiableList(styleables);

        }
        return this.STYLEABLES;
    }

    private static class StyleableProperties {

        private final static List<CssMetaData<? extends Styleable, ?>> CHILD_STYLEABLES;

        private static final CssMetaData<TextBoxB, Boolean>      FLOAT_PROMPT;
        private static final CssMetaData<TextBoxB, FieldType>    FIELD_TYPE;
        private static final CssMetaData<TextBoxB, Boolean>      VISIBLE_HELPER_TEXT;
        private static final CssMetaData<TextBoxB, Number>       COUNT;
        private static final CssMetaData<TextBoxB, Boolean>      VISIBLE_COUNT;
        private static final CssMetaData<TextBoxB, Icons>        LEAD_ICON;
        private static final CssMetaData<TextBoxB, Icons>        TRAY_ICON;
        private static final CssMetaData<TextBoxB, TrayAction>   TRAY_ACTION;

//        private static final CssMetaData<Box, Boolean> ACTION_TYPE;
//        private static final CssMetaData<Box, Boolean> VISIBLE_COUNT;

        private StyleableProperties() {}

        static {

            FLOAT_PROMPT = new CssMetaData<>(
                    "-gn-float-prompt",
                    BooleanConverter.getInstance(), false) {

                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.floatPrompt.isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(TextBoxB styleable) {
                    return styleable.floatPromptProperty();
                }
            };

            FIELD_TYPE = new CssMetaData<>(
                    "-gn-field-type",
                    FieldTypeConverter.getInstance()) {

                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.fieldType.isBound();
                }

                @Override
                public StyleableProperty<FieldType> getStyleableProperty(TextBoxB styleable) {
                    return styleable.fieldTypeProperty();
                }
            };

            VISIBLE_HELPER_TEXT = new CssMetaData<>(
                    "-gn-visible-helper", BooleanConverter.getInstance()) {
                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.visibleHelperText.isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(TextBoxB styleable) {
                    return styleable.visibleHelperTextProperty();
                }
            };

            VISIBLE_COUNT = new CssMetaData<>(
                    "-gn-visible-count", BooleanConverter.getInstance()) {
                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.visibleCount.isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(TextBoxB styleable) {
                    return styleable.visibleCountProperty();
                }
            };

            COUNT = new CssMetaData<>(
                    "-gn-count", SizeConverter.getInstance(), 0)
            {
                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.count.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(TextBoxB styleable) {
                    return styleable.countProperty();
                }
            };

            LEAD_ICON = new CssMetaData<>(
                    "-gn-lead-icon",
                    LeadIconTypeConverter.getInstance()) {

                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.leadIcon.isBound();
                }

                @Override
                public StyleableProperty<Icons> getStyleableProperty(TextBoxB styleable) {
                    return styleable.leadIconProperty();
                }
            };

            TRAY_ICON = new CssMetaData<>(
                    "-gn-tray-icon",
                    LeadIconTypeConverter.getInstance()) {

                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.trayIcon.isBound();
                }

                @Override
                public StyleableProperty<Icons> getStyleableProperty(TextBoxB styleable) {
                    return styleable.trayIconProperty();
                }
            };

            TRAY_ACTION = new CssMetaData<>(
                    "-gn-tray-action", TrayActionConverter.getInstance()) {

                @Override
                public boolean isSettable(TextBoxB styleable) {
                    return !styleable.trayAction.isBound();
                }

                @Override
                public StyleableProperty<TrayAction> getStyleableProperty(TextBoxB styleable) {
                    return styleable.trayActionProperty();
                }
            };

            List<CssMetaData<? extends Styleable, ?>> styleables
                    = new ArrayList<>(Control.getClassCssMetaData());

            Collections.addAll(styleables,
                    FLOAT_PROMPT, VISIBLE_HELPER_TEXT,
                    FIELD_TYPE, COUNT, LEAD_ICON,
                    TRAY_ACTION, VISIBLE_COUNT, TRAY_ICON
            );

            CHILD_STYLEABLES = Collections.unmodifiableList(styleables);

        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.CHILD_STYLEABLES;
    }

    public final void setCellFactory(Callback<ListView<Model>, ListCell<Model>> value) { cellFactoryProperty().set(value); }

    public ObjectProperty<Callback<ListView<Model>, ListCell<Model>>> cellFactoryProperty() { return cellFactory; }

    public final Callback<ListView<Model>, ListCell<Model>> getCellFactory() {
        return cellFactoryProperty().get();
    }

    public String getAdditionalText() {
        return additionalText.get();
    }

    public StringProperty additionalTextProperty() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText.set(additionalText);
    }

    public String getHelperText() {
        return helperText.get();
    }

    public StringProperty helperTextProperty() {
        return helperText;
    }

    public void setHelperText(String helperText) {
        this.helperText.set(helperText);
    }

    public boolean isFloatPrompt() {
        return floatPrompt.get();
    }

    public StyleableBooleanProperty floatPromptProperty() {
        return floatPrompt;
    }

    public void setFloatPrompt(boolean floatPrompt) {
        this.floatPrompt.set(floatPrompt);
    }

    public ObservableList<Model> getSuggestionList() {
        return suggestionList.get();
    }

    public ObjectProperty<ObservableList<Model>> suggestionListProperty() {
        return suggestionList;
    }

    public void setSuggestionList(ObservableList<Model> suggestionList) {
        this.suggestionList.set(suggestionList);
    }

    public FieldType getFieldType() {
        return fieldType.get();
    }

    public StyleableObjectProperty<FieldType> fieldTypeProperty() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType.set(fieldType);
    }

    public Boolean getVisibleHelperText() {
        return visibleHelperText.get();
    }

    public StyleableObjectProperty<Boolean> visibleHelperTextProperty() {
        return visibleHelperText;
    }

    public void setVisibleHelperText(Boolean visibleHelperText) {
        this.visibleHelperText.set(visibleHelperText);
    }

    public Icons getLeadIcon() {
        return leadIcon.get();
    }

    public StyleableObjectProperty<Icons> leadIconProperty() {
        return leadIcon;
    }

    public void setLeadIcon(Icons leadIcon) {
        this.leadIcon.set(leadIcon);
    }

    public TrayAction getTrayAction() {
        return trayAction.get();
    }

    public StyleableObjectProperty<TrayAction> trayActionProperty() {
        return trayAction;
    }

    public void setTrayAction(TrayAction trayAction) {
        this.trayAction.set(trayAction);
    }

    public TextField getEditor() {
        return editor.get();
    }

    public ObjectProperty<TextField> editorProperty() {
        return editor;
    }

    public void setEditor(TextField editor) {
        this.editor.set(editor);
    }

    public StringProperty textProperty() {
        return editor.get().textProperty();
    }

    public String getText() {
        return editor.get().getText();
    }

    public void setText(String text) {
        editor.get().setText(text);
    }

    public void setPromptText(String promptText) {
        editor.get().setPromptText(promptText);
    }

    public StringProperty promptTextProperty() {
        return editor.get().promptTextProperty();
    }

    public String getPromptText() {
        return editor.get().getPromptText();
    }

    public Number getCount() {
        return count.get();
    }

    public StyleableObjectProperty<Number> countProperty() {
        return count;
    }

    public void setCount(Number count) {
        this.count.set(count);
    }

    public Boolean getVisibleCount() {
        return visibleCount.get();
    }

    public StyleableObjectProperty<Boolean> visibleCountProperty() {
        return visibleCount;
    }

    public void setVisibleCount(Boolean visibleCount) {
        this.visibleCount.set(visibleCount);
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid.set(valid);
    }

    public StyleableObjectProperty<Icons> trayIconProperty() {
        return trayIcon;
    }

    public Icons getTrayIcon() {
        return trayIcon.get();
    }

    public void setTrayIcon(Icons trayIcon) {
        this.trayIcon.set(trayIcon);
    }
}
