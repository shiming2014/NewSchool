package com.example.newschool.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 世铭 on 2018/6/3.
 */

public class CourseMore extends BmobObject {
    private CourseInfo courseInfo;
    private String location;
    private String time;

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
