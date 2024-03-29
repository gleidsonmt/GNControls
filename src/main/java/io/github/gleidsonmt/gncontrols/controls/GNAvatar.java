/*
 *
 *    Copyright (C) Gleidson Neves da Silveira
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.gleidsonmt.gncontrols.controls;

import io.github.gleidsonmt.gncontrols.controls.skin.GNAvatarSkin;
import io.github.gleidsonmt.gncontrols.controls.status.Status;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Version 0.0.1
 * Create on  22/01/2023
 */
public class GNAvatar extends Control {

    private final ObjectProperty<Image> image =  new SimpleObjectProperty<>();
    private final DoubleProperty radius = new SimpleDoubleProperty();

    public GNAvatar() {
        this(20);
    }

    public GNAvatar(double _radius) {
        radius.set(_radius);
        setPrefSize(_radius, _radius);
    }

    public GNAvatar(Image image, double _radius) {
        radius.set(_radius);
        setPrefSize(_radius, _radius);
        setImage(image);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new GNAvatarSkin(this);
    }

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public double getRadius() {
        return radius.get();
    }

    public DoubleProperty radiusProperty() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius.set(radius);
    }

}
