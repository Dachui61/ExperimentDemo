package com.example.experimentdemo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.experimentdemo.DatabaseHelper;
import com.example.experimentdemo.model.OpenLabStudent;

import java.util.ArrayList;
import java.util.List;

public class OpenLabStudentDAO {

    private DatabaseHelper dbHelper;

    public OpenLabStudentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入学生信息
    public long insertOpenLabStudent(OpenLabStudent student) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_RESERVATION_NUMBER, student.getReservationNumber());
        values.put(DatabaseHelper.KEY_STUDENT_ID, student.getStudentId());
        values.put(DatabaseHelper.KEY_NAME, student.getName());
        values.put(DatabaseHelper.KEY_AUTHENTICATION_CODE, student.getAuthenticationCode());
        values.put(DatabaseHelper.KEY_FEATURE_VALUE, student.getFeatureValue());
        long id = db.insert(DatabaseHelper.TABLE_OPEN_LAB_STUDENT, null, values);
        db.close();
        return id;
    }

    // 更新学生信息
    public int updateOpenLabStudent(OpenLabStudent student) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FEATURE_VALUE, student.getFeatureValue());
        int rowsAffected = db.update(
                DatabaseHelper.TABLE_OPEN_LAB_STUDENT,
                values,
                DatabaseHelper.KEY_STUDENT_ID + " = ?",
                new String[]{String.valueOf(student.getStudentId())});
        db.close();
        return rowsAffected;
    }

    // 清除所有学生信息
    public void deleteAllOpenLabStudents() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_OPEN_LAB_STUDENT, null, null);
        db.close();
    }

    // 查询所有学生信息
    public List<OpenLabStudent> getAllOpenLabStudents() {
        List<OpenLabStudent> studentList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_OPEN_LAB_STUDENT,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    OpenLabStudent student = new OpenLabStudent();
                    student.setReservationNumber(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_RESERVATION_NUMBER)));
                    student.setStudentId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_STUDENT_ID)));
                    student.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_NAME)));
                    student.setAuthenticationCode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_AUTHENTICATION_CODE)));
                    student.setFeatureValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FEATURE_VALUE)));
                    studentList.add(student);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return studentList;
    }
}
