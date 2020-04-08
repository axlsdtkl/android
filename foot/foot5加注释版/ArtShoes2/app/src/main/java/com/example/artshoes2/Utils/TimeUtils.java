package com.example.artshoes2.Utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    @SuppressLint("SimpleDateFormat")
    public static String getNowDateTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
}
