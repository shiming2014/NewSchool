package com.example.newschool.utils;


import android.graphics.Color;

import com.example.newschool.R;


public class ColorUtil {
    /**
     * 颜色模板类
     * <p>
     * 一共30种颜色
     */
    private static String[] COLORS = {
            "#b71c1c",
            "#880E4F",
            "#4A148C",
            "#311B92",
            "#1A237E",
            "#0D47A1",

            "#01579B",
            "#006064",
            "#004D40",
            "#1B5E20",
            "#33691E",
            "#827717",

            "#F57F17",
            "#FF6F00",
            "#E65100",
            "#BF360C",
            "#FF5722",
            "#FF9800",

            "#AD1457",
            "#BF360C",
            "#F57C00",
            "#4CAF50",
            "#009688",
            "#00BCD4",

            "#03A9F4",
            "#2196F3",
            "#3F51B5",
            "#673AB7",
            "#9C27B0",
            "#E91E63",


    };


    public static String getRandomColor() {
        int index = (int) (Math.random() * COLORS.length);
        return COLORS[index];
    }

    private static int COLOR_INDEX = 0;

    public static int nextColor() {
        if (COLOR_INDEX >= COLORS.length) {
            COLOR_INDEX = 0;
        }
        return Color.parseColor(COLORS[COLOR_INDEX++]);
    }
}
