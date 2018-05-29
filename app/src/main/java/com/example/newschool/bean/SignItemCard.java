package com.example.newschool.bean;

/**
 * Created by 世铭 on 2018/5/24.
 */

public class SignItemCard {
    private String courseName, issueTime, deadline, signYes, signNo;

    @Override
    public String toString() {
        return "SignItemCard{" +
                "courseName='" + courseName + '\'' +
                ", issueTime='" + issueTime + '\'' +
                ", deadline='" + deadline + '\'' +
                ", signYes='" + signYes + '\'' +
                ", signNo='" + signNo + '\'' +
                '}';
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getSignYes() {
        return signYes;
    }

    public void setSignYes(String signYes) {
        this.signYes = signYes;
    }

    public String getSignNo() {
        return signNo;
    }

    public void setSignNo(String signNo) {
        this.signNo = signNo;
    }
}
