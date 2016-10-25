package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * GKislin
 * 07.01.2015.
 */
public class TimeUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }
    public static String formatLocalDateTime(LocalDateTime dateTime)
    {
        if (dateTime == null ) dateTime = LocalDateTime.now();
        return dateTime.format(formatter);
    }

    public static LocalDateTime formatStringToLocalDateTime(String dateString)
    {
        if (dateString == null || "".equals(dateString)) return LocalDateTime.now();
        return LocalDateTime.parse(dateString,formatter);
    }
}
