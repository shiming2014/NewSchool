package com.example.newschool.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 世铭 on 2018/5/25.
 */

public class StudentInfo extends BmobUser {
    private String stuName;
    private String className;
    private String stuOrTeacher;
    private String stuNo;

    public BmobRelation getCourses() {
        return courses;
    }

    public void setCourses(BmobRelation courses) {
        this.courses = courses;
    }

    private BmobRelation courses;

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuOrTeacher() {
        return stuOrTeacher;
    }

    public void setStuOrTeacher(String stuOrTeacher) {
        this.stuOrTeacher = stuOrTeacher;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
