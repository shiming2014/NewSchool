package com.example.newschool.utils;


import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 世铭 on 2018/4/29.
 */

public class FileNameUtil {
    private static String a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

//文件名
    private static String getDateStream(){
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm_");
        return sdf.format(d);
    }

    public static String getInvitedCode() {

        char[] rands = new char[6];
        for (int i = 0; i < rands.length; i++) {
            int rand = (int) (Math.random() * a.length());
            rands[i] = a.charAt(rand);

        }
        return String.valueOf(rands);

    }

    public static  String getMyFileName(){

        return getDateStream()+getInvitedCode();
    }

}
