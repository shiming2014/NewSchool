package com.example.newschool.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by 世铭 on 2018/5/18.
 */

public class TeacherInfo extends BmobUser {
    private String teacherName;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "TeacherInfo{" +
                "teacherName='" + teacherName + '\'' +
                "} " + super.toString();
    }
}
