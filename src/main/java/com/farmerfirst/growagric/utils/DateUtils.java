package com.farmerfirst.growagric.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    public static double calHoursDifference(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            System.out.println("XXXXXXXXXXXXXXXXXXXXX date "+date2+"  "+date1);
            Date dateTime1 = format.parse(date1);
            Date dateTime2 = format.parse(date2);

            double milliseconds = Math.abs(dateTime2.getTime() - dateTime1.getTime());
            System.out.println("XXXXXXXXXXXXXXXXXXXXXX "+milliseconds);
            double hoursDifference = milliseconds / (60 * 60 * 1000);

            return hoursDifference;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error
        }
    }

    public static int getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeekInt;
    }

    public static int formatToDayOfWeek(String date_time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int dayOfWeekInt = 0;
        try {
            Date date = sdf.parse(date_time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK);
        }catch (Exception ex){}

        return dayOfWeekInt;
    }

    public static long convertToUTCTimestamp(String date_time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Create a date object representing your local time
            Date localDate = sdf.parse(date_time);

            // Get the UTC timestamp in milliseconds
            long utcTimestampMillis = localDate.getTime();

            System.out.println("Local Date: " + localDate);
            System.out.println("UTC Timestamp (milliseconds): " + utcTimestampMillis);

            return utcTimestampMillis;
        }catch (Exception ex){
            return -1;
        }
    }

    public static void dateRange(String startDateStr,String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try{
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            calendar.setTime(startDate);

            while(calendar.getTime().compareTo(endDate)<=0){
                Date currentDate = calendar.getTime();

                calendar.add(Calendar.DAY_OF_MONTH,1);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
