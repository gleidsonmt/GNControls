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
package io.gleidsonmt.controls.lab;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.time.LocalDate;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  09/12/2018
 */
public class CalendarPane extends Control {

    private CalendarSkeleton skeleton = new CalendarSkeleton();
    private Locale locale;
    private LocalDate date;

    public CalendarPane(){
        this(LocalDate.now(), Locale.getDefault());
    }

    public CalendarPane(LocalDate date){
        this(date, Locale.getDefault());
    }

    public CalendarPane(LocalDate date, Locale locale) {
        super();
        this.date = date;
        this.locale = locale;
        skeleton.setLocale(locale);
        skeleton.setDate(date);
        getStyleClass().add("calendar-pane");
    }

    private boolean horizontalLines = true;

    public void setHorizontalLines(boolean lines){
        this.horizontalLines = lines;
    }

    public boolean isHorizontalLines() {
        return horizontalLines;
    }

    void refresh(LocalDate date) {
        skeleton.setDate(date);
        setSkin(new CalendarSkin(this));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new CalendarSkin(this);
    }

    CalendarSkeleton getSkeleton() {
        return skeleton;
    }
}
