package com.example.newschool.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 世铭 on 2018/5/20.
 */

public class JoinedCourse extends BmobObject {
    private CourseInfo courses;
    private StudentInfo studentInfo;

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public CourseInfo getCourses() {
        return courses;
    }

    public void setCourses(CourseInfo courses) {
        this.courses = courses;
    }


}
