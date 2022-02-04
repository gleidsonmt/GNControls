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
package io.github.gleidsonmt.gncontrols.test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  01/01/2022
 */
public class Item extends StackPane {

    private final ObjectProperty<ObservableList<Node>> options
            = new SimpleObjectProperty<>(this, "options", FXCollections.observableArrayList());

    private final Label legend = new Label("Legend");
    private final Options _options = new Options();

    public Item(Region control) {
        this("title", (Control) control);
    }
    public Item(String title, Control control) {

        setAlignment(Pos.TOP_LEFT);
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);
        legend.setText(title);

        legend.setStyle("-fx-text-fill : gray");

//            private Font(Object f, String family, String name, String style, double size) {

        content.getChildren().setAll(control, _options);
        content.setPadding(new Insets(20));
        content.setSpacing(20);
        HBox.setHgrow(control, Priority.ALWAYS);
//        HBox.setHgrow(_options, Priority.ALWAYS);
        getChildren().addAll(legend, content);

        setBorder(new Border(
                new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(0.3))
        ));

        _options.setSpacing(5);

        legend.setTranslateY(-13);
        legend.setTranslateX(8);
        legend.setPadding(new Insets(2));
        legend.setBackground(new Background(
                new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)
        ));

        options.get().addListener((ListChangeListener<Node>) c -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    _options.getChildren().addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    _options.getChildren().removeAll(c.getRemoved());
                }
            }
        });
    }

    public String getTitle() {
        return legend.getText();
    }

    public ObservableList<Node> getOptions() {
        return options.get();
    }

}
