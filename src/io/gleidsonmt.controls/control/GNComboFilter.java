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
package com.gn.control;

import com.gn.skin.ComboFilter;
import com.gn.skin.GNDatePickerSkin;
import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.FormatStyle;
import java.util.logging.Filter;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  07/05/2019
 */
public class GNComboFilter<T>  extends ComboBoxBase<T> {


    private static final String DEFAULT_STYLE_CLASS = "combo-box";

    private boolean wasSetAllCalled = false;
    private int previousItemCount = -1;

    private FilteredList<T> filteredList = new FilteredList<>(FXCollections.observableArrayList(), p -> false);

    public GNComboFilter() {
        this(FXCollections.<T>observableArrayList());
    }

    public GNComboFilter(ObservableList<T> items) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setItems(items);


    }


    private void updateValue(T newValue) {
        if (! valueProperty().isBound()) {
            setValue(newValue);
        }
    }

    public final void setSelectionModel(SingleSelectionModel<T> value) { selectionModel.set(value); }
    public final SingleSelectionModel<T> getSelectionModel() { return selectionModel.get(); }
    public final ObjectProperty<SingleSelectionModel<T>> selectionModelProperty() { return selectionModel; }

    private ObjectProperty<SingleSelectionModel<T>> selectionModel = new SimpleObjectProperty<SingleSelectionModel<T>>(this, "selectionModel") {
        private SingleSelectionModel<T> oldSM = null;
        @Override protected void invalidated() {
            if (oldSM != null) {
                oldSM.selectedItemProperty().removeListener(selectedItemListener);
            }
            SingleSelectionModel<T> sm = get();
            oldSM = sm;
            if (sm != null) {
                sm.selectedItemProperty().addListener(selectedItemListener);
            }
        }
    };

    private ChangeListener<T> selectedItemListener = new ChangeListener<T>() {
        @Override public void changed(ObservableValue<? extends T> ov, T t, T t1) {
            if (wasSetAllCalled && t1 == null) {
                // no-op: fix for RT-22572 where the developer was completely
                // replacing all items in the ComboBox, and expecting the
                // selection (and ComboBox.value) to remain set. If this isn't
                // here, we would updateValue(null).
                // Additional fix for RT-22937: adding the '&& t1 == null'.
                // Without this, there would be circumstances where the user
                // selecting a new value from the ComboBox would end up in here,
                // when we really should go into the updateValue(t1) call below.
                // We should only ever go into this clause if t1 is null.
            } else {
                updateValue(t1);
            }

            wasSetAllCalled = false;
        }
    };

    private ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactory =
            new SimpleObjectProperty<Callback<ListView<T>, ListCell<T>>>(this, "cellFactory");
    public final void setCellFactory(Callback<ListView<T>, ListCell<T>> value) { cellFactoryProperty().set(value); }
    public final Callback<ListView<T>, ListCell<T>> getCellFactory() {return cellFactoryProperty().get(); }
    public ObjectProperty<Callback<ListView<T>, ListCell<T>>> cellFactoryProperty() { return cellFactory; }

    public ObjectProperty<ListCell<T>> buttonCellProperty() { return buttonCell; }
    private ObjectProperty<ListCell<T>> buttonCell =
            new SimpleObjectProperty<ListCell<T>>(this, "buttonCell");
    public final void setButtonCell(ListCell<T> value) { buttonCellProperty().set(value); }
    public final ListCell<T> getButtonCell() {return buttonCellProperty().get(); }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ComboFilter<>(this);
    }

    private IntegerProperty visibleRowCount
            = new SimpleIntegerProperty(this, "visibleRowCount", 10);
    public final void setVisibleRowCount(int value) { visibleRowCount.set(value); }
    public final int getVisibleRowCount() { return visibleRowCount.get(); }
    public final IntegerProperty visibleRowCountProperty() { return visibleRowCount; }

    private ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<ObservableList<T>>(this, "items");
    public final void setItems(ObservableList<T> value) { itemsProperty().set(value); }
    public final ObservableList<T> getItems() {return items.get(); }
    public ObjectProperty<ObservableList<T>> itemsProperty() { return items; }

    public ObjectProperty<StringConverter<T>> converterProperty() { return converter; }
    private ObjectProperty<StringConverter<T>> converter =
            new SimpleObjectProperty<StringConverter<T>>(this, "converter", defaultStringConverter());
    public final void setConverter(StringConverter<T> value) { converterProperty().set(value); }
    public final StringConverter<T> getConverter() {return converterProperty().get(); }


    private  <T> StringConverter<T> defaultStringConverter() {
        return new StringConverter<T>() {
            @Override public String toString(T t) {
                return t == null ? null : t.toString();
            }

            @Override public T fromString(String string) {
                return (T) string;
            }
        };
    }

    private ReadOnlyObjectWrapper<TextField> editor;

    public final TextField getEditor() {
        return editorProperty().get();
    }

    private TextField textField;
    public final ReadOnlyObjectProperty<TextField> editorProperty() {
        if (editor == null) {
            editor = new ReadOnlyObjectWrapper<TextField>(this, "editor");
            textField = new ComboBoxListViewSkin.FakeFocusTextField();
            editor.set(textField);
        }
        return editor.getReadOnlyProperty();
    }

    // package for testing
    static class ComboBoxSelectionModel<T> extends SingleSelectionModel<T> {
        private final GNComboFilter<T> comboBox;

        public ComboBoxSelectionModel(final GNComboFilter<T> cb) {
            if (cb == null) {
                throw new NullPointerException("ComboBox can not be null");
            }
            this.comboBox = cb;

            selectedIndexProperty().addListener(valueModel -> {
                // we used to lazily retrieve the selected item, but now we just
                // do it when the selection changes.
                setSelectedItem(getModelItem(getSelectedIndex()));
            });

            /*
             * The following two listeners are used in conjunction with
             * SelectionModel.select(T obj) to allow for a developer to select
             * an item that is not actually in the data model. When this occurs,
             * we actively try to find an index that matches this object, going
             * so far as to actually watch for all changes to the items list,
             * rechecking each time.
             */
            itemsObserver = new InvalidationListener() {
                private WeakReference<ObservableList<T>> weakItemsRef = new WeakReference<>(comboBox.getItems());

                @Override public void invalidated(Observable observable) {
                    ObservableList<T> oldItems = weakItemsRef.get();
                    weakItemsRef = new WeakReference<>(comboBox.getItems());
                    updateItemsObserver(oldItems, comboBox.getItems());
                }
            };
            this.comboBox.itemsProperty().addListener(new WeakInvalidationListener(itemsObserver));
            if (comboBox.getItems() != null) {
                this.comboBox.getItems().addListener(weakItemsContentObserver);
            }
        }

        // watching for changes to the items list content
        private final ListChangeListener<T> itemsContentObserver = new ListChangeListener<T>() {
            @Override public void onChanged(Change<? extends T> c) {
                if (comboBox.getItems() == null || comboBox.getItems().isEmpty()) {
                    setSelectedIndex(-1);
                } else if (getSelectedIndex() == -1 && getSelectedItem() != null) {
                    int newIndex = comboBox.getItems().indexOf(getSelectedItem());
                    if (newIndex != -1) {
                        setSelectedIndex(newIndex);
                    }
                }

                int shift = 0;
                while (c.next()) {
                    comboBox.wasSetAllCalled = comboBox.previousItemCount == c.getRemovedSize();

                    if (c.wasReplaced()) {
                        // no-op
                    } else if (c.wasAdded() || c.wasRemoved()) {
                        if (c.getFrom() <= getSelectedIndex() && getSelectedIndex()!= -1) {
                            shift += c.wasAdded() ? c.getAddedSize() : -c.getRemovedSize();
                        }
                    }
                }

                if (shift != 0) {
                    clearAndSelect(getSelectedIndex() + shift);
                }

                comboBox.previousItemCount = getItemCount();
            }
        };

        // watching for changes to the items list
        private final InvalidationListener itemsObserver;

        private WeakListChangeListener<T> weakItemsContentObserver =
                new WeakListChangeListener<T>(itemsContentObserver);


        private void updateItemsObserver(ObservableList<T> oldList, ObservableList<T> newList) {
            // update listeners
            if (oldList != null) {
                oldList.removeListener(weakItemsContentObserver);
            }
            if (newList != null) {
                newList.addListener(weakItemsContentObserver);
            }

            // when the items list totally changes, we should clear out
            // the selection and focus
            int newValueIndex = -1;
            if (newList != null) {
                T value = comboBox.getValue();
                if (value != null) {
                    newValueIndex = newList.indexOf(value);
                }
            }
            setSelectedIndex(newValueIndex);
        }

        // API Implementation
        @Override protected T getModelItem(int index) {
            final ObservableList<T> items = comboBox.getItems();
            if (items == null) return null;
            if (index < 0 || index >= items.size()) return null;
            return items.get(index);
        }

        @Override protected int getItemCount() {
            final ObservableList<T> items = comboBox.getItems();
            return items == null ? 0 : items.size();
        }
    }
}
