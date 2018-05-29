package com.example.newschool.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 世铭 on 2018/5/23.
 */

public class SignInfo extends BmobObject {
    private CourseInfo courseInfo;
    private BmobGeoPoint bmobGeoPoint;
    private BmobDate deadline;
    private String signCode;
    private BmobRelation students;

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public BmobDate getDeadline() {
        return deadline;
    }

    public void setDeadline(BmobDate deadline) {
        this.deadline = deadline;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public BmobGeoPoint getBmobGeoPoint() {
        return bmobGeoPoint;
    }

    public void setBmobGeoPoint(BmobGeoPoint bmobGeoPoint) {
        this.bmobGeoPoint = bmobGeoPoint;
    }

    public BmobRelation getStudents() {
        return students;
    }

    public void setStudents(BmobRelation students) {
        this.students = students;
    }
}
