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
package com.gn.lab;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Separator;
import javafx.scene.control.SkinBase;

import java.time.LocalDate;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  09/12/2018
 */
public class CalendarSkin extends SkinBase<CalendarPane> {

    private CalendarSkeleton skeleton;

    protected CalendarSkin(CalendarPane control) {
        super(control);
        skeleton = getSkinnable().getSkeleton();
        refresh();
    }

    private void refresh() {
        getChildren().clear();

        String[][] val = skeleton.createSkeleton(skeleton.getLocale());

        for(int k = 0; k < skeleton.getColumns(); k++){
            CalendarCell cell = new CalendarCell(
                            val[0][k],
                            LocalDate.now(),
                            val[0][k]
                    );
            getChildren().add(cell);
        }

        for (int i = 1; i < skeleton.getRows(); i++) {
            for(int j = 0; j < skeleton.getColumns(); j++){
                CalendarCell cell = null;

                cell = new CalendarCell(
                        "day" + String.valueOf(LocalDate.parse(val[i][j]).getDayOfMonth()),
                        LocalDate.now(),
                        String.valueOf(LocalDate.parse(val[i][j]).getDayOfMonth())
                );

                // disable week buttons
                if(LocalDate.parse(val[i][j]).getMonthValue()  != skeleton.getDate().getMonthValue()){
                    cell.setDisable(true);
                }

                // select actual date
                if(LocalDate.parse(val[i][j]).equals(LocalDate.now())){
                    cell.setSelected(true);
                }
//                cell.setMouseTransparent(true);
                getChildren().add(cell);


            }
        }
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        double width = getSkinnable().getWidth();
        double height = getSkinnable().getHeight();

        double cellWidth = width / skeleton.getColumns();
        double cellHeight = height / skeleton.getRows();

        for (int i = 0; i < skeleton.getRows(); i++) {
            for (int j = 0; j < skeleton.getColumns(); j++) {

                if (getChildren().size() <= ((i * (skeleton.getColumns() )) + j)) {
                    break;
                }

                layoutInArea(getChildren().get((i * (skeleton.getColumns() )) + j), j * cellWidth, i * cellHeight, cellWidth, cellHeight, 0.0d, HPos.CENTER, VPos.CENTER);
            }
        }
    }
}
