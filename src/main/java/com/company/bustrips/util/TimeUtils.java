package com.company.bustrips.util;

import java.time.Duration;
import java.time.LocalTime;

public  class TimeUtils {
    public static LocalTime parseGivenTime(String time){
        String[] parts= time.trim().split(":");
        int hours=Integer.parseInt(parts[0])%24;
        int minutes=Integer.parseInt(parts[1]);
        int seconds=Integer.parseInt(parts[2]);
        return LocalTime.of(hours,minutes,seconds);
    }



}
