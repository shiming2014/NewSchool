package com.example.newschool.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 世铭 on 2018/5/6.
 */

public class DateUtil {

    public static String formatDate(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 a hh:mm");
        return sdf.format(date);
    }

}
