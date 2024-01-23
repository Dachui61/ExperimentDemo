package com.example.experimentdemo.model;

import com.google.gson.annotations.SerializedName;

public class StudentFaceFeature {

    @SerializedName("学号")
    private String studentId;

    @SerializedName("特征码")
    private String featureValue;

    @Override
    public String toString() {
        return "FaceFeature{" +
                "studentId='" + studentId + '\'' +
                ", featureValue='" + featureValue + '\'' +
                '}';
    }

    public StudentFaceFeature() {
    }

    public StudentFaceFeature(String studentId, String featureValue) {
        this.studentId = studentId;
        this.featureValue = featureValue;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(String featureValue) {
        this.featureValue = featureValue;
    }
}

