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

    public static long getMinutesInBetween(LocalTime now, LocalTime arrival){
        long minutes=Duration.between(now, arrival).toMinutes();
        if (minutes<0){
            minutes+=24*60;

        }
        return minutes;
    }

    public static boolean isWithin2Hours(LocalTime now, LocalTime arrival){
        long min= getMinutesInBetween(now,arrival);
        if (min>=0 && min<=120){
            return true;
        }
        return false;
    }


}
