package com.example.newschool.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 世铭 on 2018/6/2.
 */

public class StuHomework extends BmobObject {
    private HomeworkInfo homeworkInfo;
    private StudentInfo studentInfo;
    private Number score;
    private String status;//1提交 2 打回
    private List<BmobFile> files;

    public HomeworkInfo getHomeworkInfo() {
        return homeworkInfo;
    }

    public void setHomeworkInfo(HomeworkInfo homeworkInfo) {
        this.homeworkInfo = homeworkInfo;
    }

    public StudentInfo getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(StudentInfo studentInfo) {
        this.studentInfo = studentInfo;
    }

    public Number getScore() {
        return score;
    }

    public void setScore(Number score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BmobFile> getFiles() {
        return files;
    }

    public void setFiles(List<BmobFile> files) {
        this.files = files;
    }
}
