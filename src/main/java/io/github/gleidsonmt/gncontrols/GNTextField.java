package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.converters.FieldTypeConverter;
import io.github.gleidsonmt.gncontrols.converters.LeadIconTypeConverter;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.model.Model;
import io.github.gleidsonmt.gncontrols.options.FieldType;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.*;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.SizeConverter;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DefaultProperty("control")
@SuppressWarnings("unused")
public class GNTextField extends TextField implements Component {

    private final ObjectProperty<Callback<ListView<Model>, ListCell<Model>>> cellFactory =
            new SimpleObjectProperty<>(this, "cellFactory");

    private final StringProperty additionalText = new SimpleStringProperty();
    private final StringProperty helperText     = new SimpleStringProperty();

    private final ObjectProperty<ObservableList<Model>> suggestionList =
            new SimpleObjectProperty<>(FXCollections.observableArrayList());

    public GNTextField() {
        this(null);
    }

    public GNTextField(String text) {
        setText(text);
        setPrefSize(100, 40);
//        setLeadIcon(Icons.CONTACT);
        getStyleClass().add("gn-text-field");

    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNTextFieldSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getControlStylesheet();
    }

    @Override
    public void setCommonStylesheet() {
        getStylesheets().add(getCommonStylesheet());
    }

    private List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

    private final StyleableBooleanProperty floatPrompt
            = new StyleableBooleanProperty(false) {

        public CssMetaData<GNTextField, Boolean> getCssMetaData() {
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

    private final StyleableObjectProperty<Number> maxLength =
        new StyleableObjectProperty<>(0) {
            @Override
            public Object getBean() {
                return this;
            }

            @Override
            public String getName() {
                return "maxLength";
            }

            @Override
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.MAX_LENGTH;
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

        private static final CssMetaData<GNTextField, Boolean>      FLOAT_PROMPT;
        private static final CssMetaData<GNTextField, FieldType>    FIELD_TYPE;
        private static final CssMetaData<GNTextField, Boolean>      VISIBLE_HELPER_TEXT;
        private static final CssMetaData<GNTextField, Number>       MAX_LENGTH;
        private static final CssMetaData<GNTextField, Icons>        LEAD_ICON;

//        private static final CssMetaData<GNTextField, Boolean> ACTION_TYPE;
//        private static final CssMetaData<GNTextField, Boolean> VISIBLE_COUNT;

        private StyleableProperties() {}

        static {

            FLOAT_PROMPT = new CssMetaData<>(
                    "-gn-float-prompt",
                    BooleanConverter.getInstance(), false) {

                @Override
                public boolean isSettable(GNTextField styleable) {
                    return !styleable.floatPrompt.isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(GNTextField styleable) {
                    return styleable.floatPromptProperty();
                }
            };

            FIELD_TYPE = new CssMetaData<>(
                    "-gn-field-type",
                    FieldTypeConverter.getInstance()) {

                @Override
                public boolean isSettable(GNTextField styleable) {
                    return !styleable.fieldType.isBound();
                }

                @Override
                public StyleableProperty<FieldType> getStyleableProperty(GNTextField styleable) {
                    return styleable.fieldTypeProperty();
                }
            };

            VISIBLE_HELPER_TEXT = new CssMetaData<>(
                    "-gn-helper-visible", BooleanConverter.getInstance()) {
                @Override
                public boolean isSettable(GNTextField styleable) {
                    return !styleable.visibleHelperText.isBound();
                }

                @Override
                public StyleableProperty<Boolean> getStyleableProperty(GNTextField styleable) {
                    return styleable.visibleHelperTextProperty();
                }
            };

            MAX_LENGTH = new CssMetaData<>(
                    "-gn-max-length", SizeConverter.getInstance(), 0)
            {
                @Override
                public boolean isSettable(GNTextField styleable) {
                    return !styleable.maxLength.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(GNTextField styleable) {
                    return styleable.maxLengthProperty();
                }
            };

            LEAD_ICON = new CssMetaData<>(
                    "-gn-lead-icon",
                    LeadIconTypeConverter.getInstance()) {

                @Override
                public boolean isSettable(GNTextField styleable) {
                    return !styleable.leadIcon.isBound();
                }

                @Override
                public StyleableProperty<Icons> getStyleableProperty(GNTextField styleable) {
                    return styleable.leadIconProperty();
                }
            };

            List<CssMetaData<? extends Styleable, ?>> styleables
                    = new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables,
                    FLOAT_PROMPT, VISIBLE_HELPER_TEXT,
                    FIELD_TYPE, MAX_LENGTH, LEAD_ICON
//                  ACTION_TYPE,
//                  VISIBLE_COUNT, VISIBLE_MESSAGE
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

    public Number getMaxLength() {
        return maxLength.get();
    }

    public StyleableObjectProperty<Number> maxLengthProperty() {
        return maxLength;
    }

    public void setMaxLength(Number maxLength) {
        this.maxLength.set(maxLength);
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
}
