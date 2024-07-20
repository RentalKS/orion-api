package com.orion.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateUtil {
    public static ZoneId UTC_ZONE_ID = ZoneId.of("UTC");

    private static DateTimeFormatterBuilder df = new DateTimeFormatterBuilder();

    public static LocalDate convertToLocalDate(Long longDate) {
        return Instant.ofEpochMilli(longDate).atZone(UTC_ZONE_ID).toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Long longDate) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(longDate), UTC_ZONE_ID);
    }

    public static LocalTime convertToLocalTime(Long longTime) {
        return LocalTime.ofInstant(Instant.ofEpochMilli(longTime), UTC_ZONE_ID);
    }

    public static long localDateTimeToMilliseconds(LocalDateTime localDateTime){
        if(localDateTime==null)
            return 0;
        return ZonedDateTime.of(localDateTime, UTC_ZONE_ID).toInstant().toEpochMilli();
    }

    public static long localDateToMilliseconds(LocalDate localDate){
        if(localDate==null)
            return 0;
        Instant instant = localDate.atStartOfDay(UTC_ZONE_ID).toInstant();
        return instant.toEpochMilli();
    }

    public static LocalDateTime now(){
        return LocalDateTime.now(UTC_ZONE_ID);
    }

    public static LocalDate nowLocalDate(){
        return LocalDate.now(UTC_ZONE_ID);
    }

    public static String convertLongToString(Long date){
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
       return  df2.format(date);
    }

    public static String convertLongToStringYMD(Long date){
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        return  df2.format(date);
    }

    public static long stringToLong(String localDateTime) {
        if (localDateTime.contains(".")) {
            localDateTime = localDateTime.substring(0, localDateTime.indexOf('.'));
        }
        if (localDateTime.contains("T")) {
            localDateTime = localDateTime.replace("T", " ");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(localDateTime, formatter);
        if (ldt == null)
            return 0;

        return ZonedDateTime.of(ldt, UTC_ZONE_ID).toInstant().toEpochMilli();
    }
}
