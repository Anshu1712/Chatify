package com.example.chatify.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class cont {

    public static String getDate(String milliSeconds){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return format.format(calendar.getTime());
    }
    public static String getTime(String milliSeconds){
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return format.format(calendar.getTime());
    }
}
