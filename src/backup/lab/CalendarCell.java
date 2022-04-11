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

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.time.LocalDate;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  09/12/2018
 */
public class CalendarCell extends ToggleButton {

    private LocalDate date;

    CalendarCell(int id, LocalDate date, String text) {
        setPrefSize(40, 40);
        date = date;
        setId(String.valueOf(id));
        setText(text);
    }


    CalendarCell(String id, LocalDate date, String text) {
        setPrefSize(50, 50);
        setAlignment(Pos.CENTER);
        getStyleClass().add("calendar-cell");
        date = date;
        setId(String.valueOf(id));
        setText(text);
    }
}
