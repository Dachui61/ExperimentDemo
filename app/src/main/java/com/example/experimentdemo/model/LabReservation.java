package com.example.experimentdemo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LabReservation {
    @SerializedName("开放序号")
    private int openNumber;

    @SerializedName("开始时间")
    private String startTime;

    @SerializedName("实验室名")
    private String labName;

    @SerializedName("预约人数")
    private int reservationCount;

    @SerializedName("指导教师")
    private String teacher;

    @SerializedName("开放类型")
    private String openType;

    @SerializedName("实验项目列表")
    private List<String> experimentList;

    // 构造方法和其他方法，getters和setters省略

    public LabReservation(int openNumber, String startTime, String labName, int reservationCount, String teacher, String openType, List<String> experimentList) {
        this.openNumber = openNumber;
        this.startTime = startTime;
        this.labName = labName;
        this.reservationCount = reservationCount;
        this.teacher = teacher;
        this.openType = openType;
        this.experimentList = experimentList;
    }

    public LabReservation() {
    }

    public int getOpenNumber() {
        return openNumber;
    }

    public void setOpenNumber(int openNumber) {
        this.openNumber = openNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public int getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public List<String> getExperimentList() {
        return experimentList;
    }

    public void setExperimentList(List<String> experimentList) {
        this.experimentList = experimentList;
    }

    @Override
    public String toString() {
        return "LabReservation{" +
                "openNumber=" + openNumber +
                ", startTime='" + startTime + '\'' +
                ", labName='" + labName + '\'' +
                ", reservationCount=" + reservationCount +
                ", teacher='" + teacher + '\'' +
                ", openType='" + openType + '\'' +
                ", experimentList=" + experimentList +
                '}';
    }
}