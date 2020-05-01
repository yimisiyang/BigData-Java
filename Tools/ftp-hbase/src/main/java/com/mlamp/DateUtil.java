package com.mlamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final SimpleDateFormat LONG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getTimeStampStr(Date date){
        return LONG_DATE_FORMAT.format(date);
    }
}
