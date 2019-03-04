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

import java.text.DateFormatSymbols;
import java.time.*;
import java.time.temporal.*;
import java.util.Locale;

/**
 * Returns an array with LocalDates of the current month. This array contains 7 columns and 7 rows,
 * the indices [0][n] are reserved to week names
 *
 * Example with the year 2016 month 1.
 *
 *  2015-12-27  2015-12-28  2015-12-29  2015-12-30  2015-12-31  2016-01-01  2016-01-02
 *  2016-01-03  2016-01-04  2016-01-05  2016-01-06  2016-01-07  2016-01-08  2016-01-09
 *  2016-01-10  2016-01-11  2016-01-12  2016-01-13  2016-01-14  2016-01-15  2016-01-16
 *  2016-01-17  2016-01-18  2016-01-19  2016-01-20  2016-01-21  2016-01-22  2016-01-23
 *  2016-01-24  2016-01-25  2016-01-26  2016-01-27  2016-01-28  2016-01-29  2016-01-30
 *  2016-01-31  2016-02-01  2016-02-02  2016-02-03  2016-02-04  2016-02-05  2016-02-06
 *
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  10/12/2018
 */
public class CalendarSkeleton {

    private LocalDate date;
    private LocalDate actualDate;
    private int year;
    private int monthValue;
    private Month month;
    private Locale locale;

    private int columns = 7;
    private int rows = 7;

    CalendarSkeleton(){
        actualDate = LocalDate.now();
        this.date = actualDate;
        this.locale = Locale.getDefault();
        this.year = date.getYear();
        this.monthValue = date.getMonthValue();
        this.date = LocalDate.of(year, monthValue, 1);
    }

    CalendarSkeleton(LocalDate date){
        actualDate = date;
        this.date = actualDate;
        this.year = date.getYear();
        this.locale = Locale.getDefault();
        this.monthValue = date.getMonthValue();
        this.date = LocalDate.of(year, monthValue, 1);
    }

    CalendarSkeleton(LocalDate date, Locale locale){
        actualDate = date;
        this.date = actualDate;
        this.locale = locale;
        this.monthValue = date.getMonthValue();
        this.date = LocalDate.of(year, monthValue, 1);
    }

    CalendarSkeleton(int year, int monthValue){
        this.date = LocalDate.now();
        this.year = year;
        this.monthValue = monthValue;
        this.date = LocalDate.of(year, monthValue, 1);
    }

    LocalDate getDate(){
        return actualDate;
    }

    void setDate(LocalDate date){
        this.actualDate = date;
    }

    Locale getLocale(){
        return locale;
    }

    void setLocale(Locale locale){
        this.locale = locale;
    }

    String[] getMonths(){
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] months = symbols.getMonths();
        return months;
    }

    String[][] createSkeleton(){

        String[][] days = new String[rows][columns];

        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] dayNames = symbols.getShortWeekdays();

        for(int k = 0; k < columns;k++){
            days[0][k] = dayNames[k + 1];
//            System.out.printf(" %s" , days[0][k]);
        }

        int firstDayCalendar;

//        year = 2018;
//        monthValue = 10;

        date = LocalDate.of(getDate().getYear(), getDate().getMonthValue(), 1);

        TemporalField fieldUS = WeekFields.of(locale).dayOfWeek();
        firstDayCalendar = (date.with(fieldUS, 1).getDayOfMonth() - 1);

        System.out.println(date);

        if(date.getMonthValue() == 1 && date.getDayOfMonth() == 1 && firstDayCalendar == 0){
            date = LocalDate.of(date.minusYears(1).getYear(), date.getMonthValue(), 1);
        } else if(date.getMonthValue() == 1){
            date = LocalDate.of(date.minusYears(1).getYear(), 12, firstDayCalendar);
        } else if(firstDayCalendar != 0){
            date = LocalDate.of(date.getYear(), date.minusMonths(1).getMonth(), firstDayCalendar);
        }

        for (int i = 1; i < rows ; i++){
            for (int j = 0; j < columns; j++){
                if(date.getDayOfYear() == 365 || date.getDayOfYear() == 366){
                    date = LocalDate.of(
                            date.plusYears(1).getYear(),
                            date.plusMonths(1).getMonth(),
                            date.plusDays(1).getDayOfMonth()
                    );
                } else if(date.getDayOfMonth() == date.getMonth().length(isLeapYear(date))){
                    date = LocalDate.of(
                            date.getYear(),
                            date.plusMonths(1).getMonth(),
                            date.plusDays(1).getDayOfMonth()
                    );
                } else {
                    date = LocalDate.of(
                            date.getYear(),
                            date.getMonth(),
                            date.plusDays(1).getDayOfMonth()
                    );
                }

                // if the month init in 1 on monday
                if(firstDayCalendar == 0){ // This is a hack :)
                    date = LocalDate.of(
                            date.getYear(),
                            date.getMonth(),
                            date.minusDays(1).getDayOfMonth()
                    );
                    firstDayCalendar = 1;
                }

                days[i][j] = date.toString();

//                System.out.printf(" %3s" , LocalDate.parse(days[i][j]).toString()); // view dates
            }
//            System.out.println(); // break lines
        }


        return days;
    }


    String[][] createSkeleton(Locale locale){

        String[][] days = new String[rows][columns];

        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] dayNames = symbols.getShortWeekdays();

        for(int k = 0; k < columns;k++){
            days[0][k] = dayNames[k + 1];
//            System.out.printf(" %s" , days[0][k]);
        }

        int firstDayCalendar;

//        year = 2018;
//        monthValue = 10;

        date = LocalDate.of(getDate().getYear(), getDate().getMonthValue(), 1);

        TemporalField fieldUS = WeekFields.of(locale).dayOfWeek();
        firstDayCalendar = (date.with(fieldUS, 1).getDayOfMonth() - 1);

        if(date.getMonthValue() == 1 && date.getDayOfMonth() == 1 && firstDayCalendar == 0){
            date = LocalDate.of(date.minusYears(1).getYear(), date.getMonthValue(), 1);
        } else if(date.getMonthValue() == 1){
            date = LocalDate.of(date.minusYears(1).getYear(), 12, firstDayCalendar);
        } else if(firstDayCalendar != 0){
            date = LocalDate.of(date.getYear(), date.minusMonths(1).getMonth(), firstDayCalendar);
        }

        for (int i = 1; i < rows ; i++){
            for (int j = 0; j < columns; j++){
                if(date.getDayOfYear() == 365 || date.getDayOfYear() == 366){
                    date = LocalDate.of(
                            date.plusYears(1).getYear(),
                            date.plusMonths(1).getMonth(),
                            date.plusDays(1).getDayOfMonth()
                    );
                } else if(date.getDayOfMonth() == date.getMonth().length(isLeapYear(date))){
                    date = LocalDate.of(
                            date.getYear(),
                            date.plusMonths(1).getMonth(),
                            date.plusDays(1).getDayOfMonth()
                    );
                } else {
                    date = LocalDate.of(
                            date.getYear(),
                            date.getMonth(),
                            date.plusDays(1).getDayOfMonth()
                    );
               }

                // if the month init in 1 on monday
                if(firstDayCalendar == 0){ // This is a hack :)
                    date = LocalDate.of(
                            date.getYear(),
                            date.getMonth(),
                            date.minusDays(1).getDayOfMonth()
                    );
                    firstDayCalendar = 1;
                }

                days[i][j] = date.toString();

//                System.out.printf(" %3s" , LocalDate.parse(days[i][j]).toString()); // view dates
            }
//            System.out.println(); // break lines
        }

        return days;
    }

    boolean isLeapYear(LocalDate date){
        return date.isLeapYear();
    }


    int getRows(){
        return rows;
    }

    int getColumns(){
        return columns;
    }

    public static void main(String[] args) {
        CalendarSkeleton calendarSkeleton = new CalendarSkeleton(LocalDate.of(2018, 3, 1));
        calendarSkeleton.createSkeleton(Locale.US);
        LocalDate date = LocalDate.of(2018, 9, 1);
        System.out.println(date.getMonth().maxLength());
    }
}
