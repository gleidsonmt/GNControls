package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.converters.FieldTypeConverter;
import io.github.gleidsonmt.gncontrols.converters.LeadIconTypeConverter;
import io.github.gleidsonmt.gncontrols.converters.TrayActionConverter;
import io.github.gleidsonmt.gncontrols.material.icon.Icons;
import io.github.gleidsonmt.gncontrols.model.Model;
import io.github.gleidsonmt.gncontrols.options.FieldType;
import io.github.gleidsonmt.gncontrols.options.TrayAction;
import io.github.gleidsonmt.gncontrols.skin.GNPasswordFieldSkin;
import io.github.gleidsonmt.gncontrols.skin.GNTextFieldSkin;
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
public class GNPasswordField extends GNTextField {

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNPasswordFieldSkin(this);
    }
}