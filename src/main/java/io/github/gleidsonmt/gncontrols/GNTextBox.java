package io.github.gleidsonmt.gncontrols;

import io.github.gleidsonmt.gncontrols.skin.GNTextBoxSkin;
import io.github.gleidsonmt.gncontrols.skin.TextBox;
import javafx.beans.DefaultProperty;
import javafx.scene.control.*;

@DefaultProperty("control")
public class GNTextBox extends TextBox implements GNComponent {

    public GNTextBox() {
        this(null);
    }

    public GNTextBox(String text) {
//        setLeadIcon(Icons.CONTACT);
        getStyleClass().add("gn-text-field");
        setEditor(new TextField(){
            @Override
            public void paste() {
                createPasteAction(this);
            }
        });

        getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> setFocused(newValue));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNTextBoxSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getControlStylesheet();
    }

}