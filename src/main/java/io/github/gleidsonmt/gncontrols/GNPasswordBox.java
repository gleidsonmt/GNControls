package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.skin.GNPasswordBoxSkin;
import javafx.beans.DefaultProperty;
import javafx.scene.control.*;

@DefaultProperty("control")
@SuppressWarnings("unused")
public final class GNPasswordBox extends GNTextBox {

    public GNPasswordBox() {
        PasswordField passwordField = new PasswordField() {
            @Override
            public void paste() {
                createPasteAction(this);
            }
        };

        setEditor(passwordField);
        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> setFocused(newValue));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNPasswordBoxSkin(this);
    }


}