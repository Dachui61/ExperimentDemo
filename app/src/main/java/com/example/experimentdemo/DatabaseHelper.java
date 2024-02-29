package com.example.experimentdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.text.TextUtils;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库名称
    private   static final String DATABASE_NAME = "LabReservationDatabase";
    // 数据库版本
    private   static final int DATABASE_VERSION = 1;

    // 表名
    public  static final String TABLE_LAB_RESERVATION = "LabReservation";
    public  static final String TABLE_OPEN_LAB_STUDENT = "OpenLabStudent";
    public  static final String TABLE_FACE_FEATURE  = "FaceFeature";


    // 共同的列名
    public  static final String KEY_ID = "id";

    // Lab Reservation 表列
    public  static final String KEY_OPEN_NUMBER = "openNumber";
    public  static final String KEY_START_TIME = "startTime";
    public  static final String KEY_LAB_NAME = "labName";
    public  static final String KEY_RESERVATION_COUNT = "reservationCount";
    public  static final String KEY_TEACHER = "teacher";
    public  static final String KEY_OPEN_TYPE = "openType";
    public  static final String KEY_EXPERIMENT_LIST = "experimentList";

    // Open Lab Student 表列
    public  static final String KEY_RESERVATION_NUMBER = "reservationNumber";
    public  static final String KEY_STUDENT_ID = "studentId";
    public  static final String KEY_NAME = "name";
    public  static final String KEY_AUTHENTICATION_CODE = "authenticationCode";
    public  static final String KEY_FEATURE_VALUE = "featureValue";

    // Face Feature 表名称和列 (特征码表)
    public static final String KEY_FACE_STUDENT_ID = "studentId";
    public static final String KEY_FACE_FEATURE_VALUE = "featureValue";

    // 创建 Lab Reservation 表
    String CREATE_TABLE_LAB_RESERVATION = "CREATE TABLE " + TABLE_LAB_RESERVATION + "("
            + KEY_OPEN_NUMBER + " INTEGER PRIMARY KEY,"
            + KEY_START_TIME + " TEXT,"
            + KEY_LAB_NAME + " TEXT,"
            + KEY_RESERVATION_COUNT + " INTEGER,"
            + KEY_TEACHER + " TEXT,"
            + KEY_OPEN_TYPE + " TEXT,"
            + KEY_EXPERIMENT_LIST + " TEXT" + ")";

    // 创建 Open Lab Student 表
    String CREATE_TABLE_OPEN_LAB_STUDENT = "CREATE TABLE " + TABLE_OPEN_LAB_STUDENT + "("
            + KEY_RESERVATION_NUMBER + " INTEGER PRIMARY KEY,"
            + KEY_STUDENT_ID + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_AUTHENTICATION_CODE + " TEXT,"
            + KEY_FEATURE_VALUE + " TEXT" + ")";

    // 创建 Face Feature 表
    String CREATE_TABLE_FACE_FEATURE = "CREATE TABLE " + TABLE_FACE_FEATURE + "("
            + KEY_FACE_STUDENT_ID + " TEXT PRIMARY KEY,"
            + KEY_FACE_FEATURE_VALUE + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL(CREATE_TABLE_LAB_RESERVATION);
        db.execSQL(CREATE_TABLE_OPEN_LAB_STUDENT);
        db.execSQL(CREATE_TABLE_FACE_FEATURE);
    }

    // 清除 Lab Reservation 表数据
    public void clearLabReservationTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LAB_RESERVATION, null, null);
        db.close();
    }

    // 清除 Open Lab Student 表数据
    public void clearOpenLabStudentTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OPEN_LAB_STUDENT, null, null);
        db.close();
    }

    // 更新 Open Lab Student 表中的特征值
    public void updateStudentFeatureValue(String studentId, String newFeatureValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FEATURE_VALUE, newFeatureValue);

        db.update(TABLE_OPEN_LAB_STUDENT, values, KEY_FEATURE_VALUE + " = ?", new String[]{studentId});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果存在，删除旧表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LAB_RESERVATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPEN_LAB_STUDENT);

        // 创建新表
        onCreate(db);
    }

}