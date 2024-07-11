package com.example.steam_tracker.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormat {

    private String formattedDate;
    private int dayOfYear;
    private int year;

    static Calendar calendar;

    public DateFormat()
    {
    }

    public DateFormat(String formattedDate, int dayOfYear,int year) {
        this.formattedDate = formattedDate;
        this.dayOfYear = dayOfYear;
        this.year = year;
    }

    private int getDayNumberOfYear(SimpleDateFormat dateFormat)
    {
        return dateFormat.getCalendar().get(Calendar.DAY_OF_YEAR);
    }

    private int getMonth(SimpleDateFormat dateFormat) {
        return dateFormat.getCalendar().get(Calendar.MONTH);
    }

    private int getYear(SimpleDateFormat dateFormat) {
        return dateFormat.getCalendar().get(Calendar.YEAR);
    }

    public DateFormat getCurrentDate()
    {
        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getTime());
        return new DateFormat(formattedDate, getDayNumberOfYear(dateFormat),getYear(dateFormat));
    }

    public DateFormat getDateFromString(String date)
    {
        SimpleDateFormat dateFormat=null;
        String newDate="";
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            newDate = dateFormat.format(oldFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new DateFormat(newDate, getDayNumberOfYear(dateFormat),getYear(dateFormat));
    }

    public int getDayOfYear() {
        return this.dayOfYear;
    }

    public int getYear() {
        return this.year;
    }
}