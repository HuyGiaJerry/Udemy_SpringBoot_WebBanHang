package com.project.shopapp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class ConvertDateUtil {

    public static LocalDateTime toLocalDateTime(Date date){
        return date == null ? null : date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date toDate(LocalDate date){
        return date == null ? null : date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant() != null ? Date.from(date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()) : null;
    }

}
