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
package io.gleidsonmt.controls.material.icon;

import javafx.scene.shape.SVGPath;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  23/11/2021
 */
public class IconContainer extends SVGPath {

    private String name;

    public IconContainer() {

    }

    public IconContainer(String name) {
        for (Icons i : Icons.values()) {
            if (i.toString().equals(name)) {
                setContent(i);
                getStyleClass().add("icon");
                this.name = name;
            }
        }
    }

    public IconContainer(Icons icon) {
        setContent(icon);
        getStyleClass().add("icon");
        name = icon.name();
    }

    public void setContent(Icons icon) {
        setContent(icon.getContent());
        name = icon.name();
    }

    public String getName() {
        return name;
    }
}
