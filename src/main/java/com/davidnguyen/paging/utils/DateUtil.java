package com.laptrinhb2a.PagingCustom.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String date2StringByPattern(Date date, String pattern) throws ParseException {
        if (date == null || DataUtil.isNullOrEmpty(pattern)) {
            return null;
        }

        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
}
