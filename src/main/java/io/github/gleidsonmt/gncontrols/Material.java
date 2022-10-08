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

package io.github.gleidsonmt.gncontrols;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  11/09/2022
 */
public class Material {

    private final ObservableList<String> stylesheets = FXCollections.observableArrayList();


    public Material(@NamedArg("root") @NotNull Parent root) {
        setStylesheets();
        root.getStylesheets().setAll(stylesheets);
    }

    public Material(@NamedArg("root") @NotNull Scene scene) {
        setStylesheets();
        scene.getStylesheets().setAll(stylesheets);
    }

    public Material(@NamedArg("root") @NotNull Parent root, Theme theme) {
        setStylesheets();
        root.getStylesheets().setAll(stylesheets);
    }

    public Material(@NamedArg("root") @NotNull Scene root, Theme theme) {
        setStylesheets();
        root.getStylesheets().setAll(stylesheets);
    }

    private void setStylesheets() {
        stylesheets.setAll(
                Objects.requireNonNull(getClass().getResource("/fonts/fonts.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/core/colors.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/core/typographic.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/core/skeleton.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/core/bootstrap.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/core/imersive_scroll.css")).toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/core/drawer.css")).toExternalForm()
//                theme.toString()

        );
    }
}
