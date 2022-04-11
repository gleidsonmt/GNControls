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

import java.text.DateFormatSymbols;
import java.time.*;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  09/12/2018
 */
public class CalendarInShell {

    private CalendarInShell(){};

    public static void main(String[] args) {

        int columns = 7;
        int rows = 6;
        int count;

//        Locale.setDefault(Locale.US);

        LocalDate localDate = LocalDate.of(2022, 1, 29);

        System.out.println("--------------------------------");
        System.out.println("|          Calendar            |");
        System.out.println("--------------------------------");

        System.out.print("| ");

        DateFormatSymbols symbols = new DateFormatSymbols();
        String[] dayNames = symbols.getShortWeekdays();

        for(int k = 1; k < 8;k++){
            System.out.print(dayNames[k] + " ");
        }

        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        count = (localDate.with(fieldUS, 1).getDayOfMonth() - 1);

        if(localDate.getMonthValue() == 1){
            localDate = LocalDate.of(localDate.minusYears(1).getYear(), 12, count);
        } else
            localDate = LocalDate.of(localDate.getYear(), localDate.minusMonths(1).getMonth(), count);


        System.out.println(" |");

        for (int i = 0; i < rows ; i++){
            System.out.print("| ");
            for (int j = 0; j < columns; j++){

                if(localDate.getDayOfYear() == 365 || localDate.getDayOfYear() == 366){
                    localDate = LocalDate.of(
                            localDate.plusYears(1).getYear(),
                            localDate.plusMonths(1).getMonth(),
                            localDate.plusDays(1).getDayOfMonth()
                    );
                }

                else if(localDate.getDayOfMonth() == localDate.getMonth().maxLength()){
                    localDate = LocalDate.of(
                            localDate.getYear(),
                            localDate.plusMonths(1).getMonth(),
                            localDate.plusDays(1).getDayOfMonth()
                    );
                } else {
                    localDate = LocalDate.of(
                            localDate.getYear(),
                            localDate.getMonth(),
                            localDate.plusDays(1).getDayOfMonth()
                    );
                }

                System.out.printf(" %2d " , localDate.getDayOfMonth());
//                System.out.print(localDate + " "); // view complete date
            }
            System.out.println(" |");
        }
        System.out.println("--------------------------------");
    }
}
