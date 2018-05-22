package com.example.newschool.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 世铭 on 2018/5/20.
 */

public class CourseInfo extends BmobObject {
    private Integer status;
    private String color;
    private String courseName, className;
    private TeacherInfo teacherInfo;
    private BmobRelation students;
    private BmobRelation notify;//作业等
    private BmobRelation material;//资料等

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public TeacherInfo getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public BmobRelation getStudents() {
        return students;
    }

    public void setStudents(BmobRelation students) {
        this.students = students;
    }

    public BmobRelation getNotify() {
        return notify;
    }

    public void setNotify(BmobRelation notify) {
        this.notify = notify;
    }

    public BmobRelation getMaterial() {
        return material;
    }

    public void setMaterial(BmobRelation material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "status=" + status +
                ", color=" + color +
                ", courseName='" + courseName + '\'' +
                ", className='" + className + '\'' +
                ", teacherInfo=" + teacherInfo +
                ", students=" + students +
                ", notify=" + notify +
                ", material=" + material +
                "} " + super.toString();
    }
}
