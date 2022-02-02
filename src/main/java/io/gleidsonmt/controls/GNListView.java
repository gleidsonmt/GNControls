package io.gleidsonmt.controls;

import io.gleidsonmt.controls.model.Model;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.Objects;

public class GNListView<T extends Model> extends ListView<T> {


    public GNListView() {
        this(null);
    }

    public GNListView(ObservableList<T> items) {
        super(items);

        getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

    }
}
