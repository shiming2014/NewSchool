package com.example.newschool.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by 世铭 on 2018/5/28.
 */

public class SignToCourse extends BmobObject {
    private StudentInfo studentInfo;
    private CourseInfo courseInfo;
    private String imei;
    private BmobGeoPoint bmobGeoPoint;

    public String getDistenceToTeacher() {
        return distenceToTeacher;
    }

    public void setDistenceToTeacher(String distenceToTeacher) {
        this.distenceToTeacher = distenceToTeacher;
    }

    private String distenceToTeacher;
    private BmobDate issueTime,deadline;

    public BmobDate getDeadline() {
        return deadline;
    }

    public void setDeadline(BmobDate deadline) {
        this.deadline = deadline;
    }



    public BmobDate getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(BmobDate issueTime) {
        this.issueTime = issueTime;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public BmobGeoPoint getBmobGeoPoint() {
        return bmobGeoPoint;
    }

    public void setBmobGeoPoint(BmobGeoPoint bmobGeoPoint) {
        this.bmobGeoPoint = bmobGeoPoint;
    }
}
