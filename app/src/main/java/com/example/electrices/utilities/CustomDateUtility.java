package com.example.electrices.utilities;

import java.util.Calendar;
import java.util.Locale;

public class CustomDateUtility {

    private final Calendar calendar;
    private final int year;
    private final int month;
    private final int monthDay;

    public CustomDateUtility(){
        this.calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1; // Months start from zero so we have to add one to much reality.
        this.monthDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public CustomDateUtility getInstance(){
        return new CustomDateUtility();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getMonthDay() {
        return monthDay;
    }

    public String getTodaysDate(){
        return makeDateString(year, month, monthDay);
    }

    public String getTodaysDateForFirestoreQuery(){
        return makeDateStringForFirestoreQuery(year, month, monthDay);
    }

    public String makeDateString(int year, int month, int monthDay) {
        return getWeekDayFromDate(year, month, monthDay) + ", " + monthDay + " " + getMonthFormat(month) + ", " + year;
    }

    private String getWeekDayFromDate(int year, int month, int monthDay){
        calendar.set(year, month-1, monthDay); // Here month is minus one because it is imported to a Calendar method, and for Calendar the months start from zero.
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return getWeekDayFormat(dayOfWeek);
    }

    private String getWeekDayFormat(int dayOfWeek){
        if(dayOfWeek == 1)
            return "Sunday";
        if(dayOfWeek == 2)
            return "Monday";
        if(dayOfWeek == 3)
            return "Tuesday";
        if(dayOfWeek == 4)
            return "Wednesday";
        if(dayOfWeek == 5)
            return "Thursday";
        if(dayOfWeek == 6)
            return "Friday";
        if (dayOfWeek == 7)
            return "Saturday";

        // Default should never happen
        return "NaN";
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        // Default should never happen
        return "NaN";
    }

    public String makeDateStringForFirestoreQuery(int year, int month, int monthDay){
        String monthString = String.valueOf(month);
        String dayString = String.valueOf(monthDay);
        if(monthDay < 10 && month >= 10){ // Only day is below ten
            dayString = String.format("0%s", dayString);
        }
        if(month < 10 && monthDay >= 10){ // Only month is below ten
            monthString = String.format("0%s", monthString);
        }
        if(monthDay < 10 && month < 10){ // Both day and month are below ten
            dayString = String.format("0%s", dayString);
            monthString = String.format("0%s", monthString);
        }
        return String.format(Locale.ENGLISH,"%d-%s-%s", year, monthString, dayString);
    }

    public int[] getTodaysDateWithOffset(int yearsOffset, int monthsOffset, int daysOffset){
        int[] dateArray = new int[3];
        int offsetYear = this.year + yearsOffset;
        int offsetMonth = this.month + monthsOffset;
        offsetMonth = offsetMonth -1;
        int offsetMonthDay = this.monthDay + daysOffset;
        dateArray[0] = offsetYear;
        dateArray[1] = offsetMonth;
        dateArray[2] = offsetMonthDay;
        return dateArray;
    }

    public long getDateInMillis(int[] date){
        calendar.set(date[0], date[1], date[2]);
        return calendar.getTimeInMillis();
    }
}


