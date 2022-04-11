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
package io.gleidsonmt.controls.skin;

import io.gleidsonmt.controls.behavior.GNComboFilterBehavior;
import io.gleidsonmt.controls.control.GNComboFilter;
import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.scene.control.skin.ComboBoxPopupControl;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  07/05/2019
 */
public class ComboFilter<T> extends ComboBoxPopupControl<T> {

    private GNComboFilter<T> comboBox;
    private TextField displayNode;
    private final ListView<T> listView;
    private FilteredList<T> filteredList = new FilteredList<T>(FXCollections.observableArrayList(), p -> true);

    private static final String COMBO_BOX_ROWS_TO_MEASURE_WIDTH_KEY = "comboBoxRowsToMeasureWidth";

    private ObservableList<T> comboBoxItems;

//    private final InvalidationListener itemsObserver;

    private boolean listViewSelectionDirty = false;
    private boolean listSelectionLock = false;

    private ObservableList<T> listViewItems;

    private Callback<ListView<T>, ListCell<T>> cellFactory;
    private ListCell<T> buttonCell;

    private ObservableList imutableList = FXCollections.observableArrayList();

    public ComboFilter(GNComboFilter<T> comboBox) {
        super(comboBox, new GNComboFilterBehavior<>(comboBox));

        this.comboBox = comboBox;
        listView = createListView();

        imutableList = comboBox.getItems();
        filteredList = (FilteredList<T>) comboBox.getItems(); // filteredList vai ser o combox items

        listView.setItems(comboBox.getItems());

//        this.listView.setManaged(false); // não redimensiona
//        getChildren().add(listView); fica estranho

//        this.getSkinnable().minWidthProperty().bind(this.listView.minWidthProperty());

//        itemsObserver = observable -> {
//            updateComboBoxItems();
//            updateListViewItems();
//        };


        try {
            Field helper = comboBox.focusedProperty().getClass().getSuperclass().getDeclaredField("helper");
            helper.setAccessible(true);
            ExpressionHelper value = (ExpressionHelper) helper.get(comboBox.focusedProperty());
            Field changeListenersField = value.getClass().getDeclaredField("changeListeners");
            changeListenersField.setAccessible(true);
            ChangeListener[] changeListeners = (ChangeListener[]) ((ChangeListener[]) changeListenersField.get(value));

            for (int i = changeListeners.length - 1; i > 0; --i) {
                if (changeListeners[i] != null && changeListeners[i].getClass().getName().contains("ComboBoxPopupControl")) {
                    comboBox.focusedProperty().removeListener(changeListeners[i]);
                    break;
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException var7) {
            var7.printStackTrace();
        }

//        this.comboBox.itemsProperty().addListener(new WeakInvalidationListener(itemsObserver));

//        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<T>() {
//            @Override
//            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
//                displayNode.setText(newValue.toString());
//            }
//        });

//        comboBox.valueProperty().bind(listView.getSelectionModel().selectedItemProperty());

//        updateListViewItems();
//        updateCellFactory();
//
//        updateButtonCell();
//
//        // Fix for RT-19431 (also tested via ComboBoxListViewSkinTest)
//        updateValue();

        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("")) {
                filteredList.setPredicate(professional -> professional.toString().toLowerCase().contains(newValue.toLowerCase()));

                if (filteredList.size() > 0) {
                    listView.getSelectionModel().selectFirst();
                    if (listView.getSelectionModel().getSelectedItem().toString().toLowerCase().equals(displayNode.getText().toLowerCase())) {
                        comboBox.setValue(listView.getSelectionModel().getSelectedItem());
                        updateOnClicked();
                    } else {
                        comboBox.show();
                    }
                } else {
                    comboBox.show();
                }
            } else {
                comboBox.setValue(null);
                filteredList.setPredicate(null);
                comboBox.show();
            }
        });

//        comboBox.valueProperty().bind(listView.getSelectionModel().selectedItemProperty());

        comboBox.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    listView.setPrefWidth(comboBox.getWidth());
                }
            }
        });
//
        comboBox.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                comboBox.setValue(listView.getSelectionModel().getSelectedItem());
                if (listView.getSelectionModel().selectedItemProperty().getValue() != null) {
                    displayNode.setText(listView.getSelectionModel().selectedItemProperty().getValue().toString());

                    if (comboBox.isShowing()) {
                        // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
                        // Make sure DatePicker button is in sync.
                        comboBox.hide();
                    }
                }
            }
        });

        listView.setOnMousePressed(event -> {
            updateOnClicked();
        });

        comboBox.focusedProperty().addListener((ov, t, hasFocus) -> {
            if (getEditor() != null) {
                // Fix for the regression noted in a comment in RT-29885.
                ((FakeFocusTextField)getEditor()).setFakeFocus(hasFocus);

                // JDK-8120120 (aka RT-21454) and JDK-8136838
                if (!hasFocus) {
                    setTextFromTextFieldIntoComboBoxValue();
                }
            }
        });

//        registerChangeListener(comboBox.itemsProperty(), "ITEMS");
//        registerChangeListener(comboBox.promptTextProperty(), "PROMPT_TEXT");
//        registerChangeListener(comboBox.visibleRowCountProperty(), "VISIBLE_ROW_COUNT");
//        registerChangeListener(comboBox.converterProperty(), "CONVERTER");
//        registerChangeListener(comboBox.buttonCellProperty(), "BUTTON_CELL");
//        registerChangeListener(comboBox.valueProperty(), "VALUE");
//        registerChangeListener(comboBox.editableProperty(), "EDITABLE");
    }

    private void updateOnClicked() {
        displayNode.setText(listView.getSelectionModel().getSelectedItem().toString());

        if (comboBox.isShowing()) {
            // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
            // Make sure DatePicker button is in sync.
            comboBox.hide();
        }
        filteredList.setPredicate(null);
    }


    private void updateValue() {
        if (comboBox.isShowing()) {
            comboBox.hide();
            listView.refresh();
        }
        if (comboBox.getValue() != null) {
            displayNode.setText(comboBox.getValue().toString());
        } else displayNode.setText(null);


    }

    private int getIndexOfComboBoxValueInItemsList() {
        T value = comboBox.getValue();
        int index = filteredList.indexOf(value);
        return index;
    }

    @Override
    protected Node getPopupContent() {
        return listView;
    }

    @Override
    protected TextField getEditor() {
        return ((GNComboFilter) getSkinnable()).getEditor();
    }

    @Override
    protected StringConverter<T> getConverter() {
        return ((GNComboFilter) getSkinnable()).getConverter();
    }

    @Override
    public Node getDisplayNode() {
        if (displayNode == null) {
            displayNode = getEditableInputNode();
            displayNode.getStyleClass().add("combo-display-node");
            updateDisplayNode();
        }
        displayNode.setEditable(comboBox.isEditable());

        return displayNode;
    }

    public void syncWithAutoUpdate() {

        if (!getPopup().isShowing() && comboBox.isShowing()) {
            // Popup was dismissed. Maybe user clicked outside or typed ESCAPE.
            // Make sure DatePicker button is in sync.
            comboBox.hide();
        }
    }

    private boolean itemCountDirty;
    private final ListChangeListener<T> listViewItemsListener = new ListChangeListener<T>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends T> c) {
            itemCountDirty = true;
            getSkinnable().requestLayout();
        }
    };

    private ListView<T> createListView() {
        final ListView<T> _listView = new ListView<T>() {

            {
                setFixedCellSize(45);
                getProperties().put("selectFirstRowByDefault", false);

//                setOnMousePressed(event -> updateOnClicked());
            }



//            @Override
//            protected double computeMinWidth(double height) {
//
//                System.out.println("compute min width = " + comboBox.getWidth());
//
//
//                return comboBox.getWidth();
//            }

            @Override
            protected double computeMinHeight(double width) {

                double ph;
                double ch = filteredList.size() * (getFixedCellSize() == -1 ? 30 : getFixedCellSize() + (getInsets().getTop() + getInsets().getBottom())); // new test

                if (filteredList.size() > 4) {
                    ph = Math.min(ch, 100);
                } else {
                    ph = ch;
                }


                return ph;
            }

            //
            @Override
            protected double computePrefWidth(double height) {
                double pw;
                pw = Math.max(100, comboBox.getWidth());
                return Math.max(50, pw);
            }

            //
            @Override
            protected double computePrefHeight(double width) {
//                System.out.println(getFixedCellSize()); colocar o fixed size depois
                return getListViewPrefHeight();
            }
        };

        _listView.setFocusTraversable(false);
        _listView.setId("list-view");
//        _listView.placeholderProperty().bind(comboBox.placeholderProperty());
        _listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        _listView.setFocusTraversable(false);

        _listView.setOnKeyPressed(t -> {
            // TODO move to behavior, when (or if) this class becomes a SkinBase
            if (t.getCode() == KeyCode.ENTER ||
                    t.getCode() == KeyCode.SPACE ||
                    t.getCode() == KeyCode.ESCAPE) {
                comboBox.hide();
            }
        });

        return _listView;
    }

    public void updateComboBoxItems() {

        comboBoxItems = comboBox.getItems(); // comboBoxItems recebe os items de combobox
        comboBoxItems = comboBoxItems == null ? FXCollections.<T>emptyObservableList() : comboBoxItems; // simples metodo para evitar null exception
    }


    private Callback<ListView<T>, ListCell<T>> getDefaultCellFactory() {
        return new Callback<ListView<T>, ListCell<T>>() {
            @Override
            public ListCell<T> call(ListView<T> listView) {
                return new ListCell<T>() {
                    @Override
                    public void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        updateDisplayText(this, item, empty);
                    }
                };
            }
        };
    }

    // return a boolean to indicate that the cell is empty (and therefore not filled)
    private boolean updateDisplayText(ListCell<T> cell, T item, boolean empty) {
        if (empty) {
            if (cell == null) return true;
            cell.setGraphic(null);
            cell.setText(null);
            return true;
        } else if (item instanceof Node) {
            Node currentNode = cell.getGraphic();
            Node newNode = (Node) item;
            if (currentNode == null || !currentNode.equals(newNode)) {
                cell.setText(null);
                cell.setGraphic(newNode);
            }
            return newNode == null;
        } else {
            // run item through StringConverter if it isn't null
            StringConverter<T> c = comboBox.getConverter();
            String s = item == null ? comboBox.getPromptText() : (c == null ? item.toString() : c.toString(item));
            cell.setText(s);
            cell.setGraphic(null);
            return s == null || s.isEmpty();
        }
    }

    private double getListViewPrefHeight() {
        double ph;
        double ch = filteredList.size() * 30;

        if (filteredList.size() > 4) {
            ph = Math.min(ch, 100);
        } else {
            ph = ch;
        }
        return ph;
    }
}