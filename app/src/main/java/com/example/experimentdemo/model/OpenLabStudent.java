package com.example.experimentdemo.model;

import com.google.gson.annotations.SerializedName;

public class OpenLabStudent {
    @SerializedName("预约序号")
    private int reservationNumber;
    @SerializedName("学号")
    private String studentId;
    @SerializedName("姓名")
    private String name;
    @SerializedName("认证码")
    private String authenticationCode;
    @SerializedName("特征码")
    private String featureValue;

    public OpenLabStudent(int reservationNumber, String studentId, String name, String authenticationCode, String featureValue) {
        this.reservationNumber = reservationNumber;
        this.studentId = studentId;
        this.name = name;
        this.authenticationCode = authenticationCode;
        this.featureValue = featureValue;
    }

    public OpenLabStudent() {

    }

    @Override
    public String toString() {
        return "OpenLabStudent{" +
                "reservationNumber=" + reservationNumber +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", authenticationCode='" + authenticationCode + '\'' +
                ", featureValue='" + featureValue + '\'' +
                '}';
    }

    // Getters and setters for each member variable

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthenticationCode() {
        return authenticationCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }
}

