package com.example.experimentdemo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.experimentdemo.DatabaseHelper;
import com.example.experimentdemo.model.StudentFaceFeature;

import java.util.ArrayList;
import java.util.List;

public class FaceFeatureDao {

    private DatabaseHelper databaseHelper;

    public FaceFeatureDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void insertFaceFeature(StudentFaceFeature studentFaceFeature) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_STUDENT_ID, studentFaceFeature.getStudentId());
        values.put(DatabaseHelper.KEY_FEATURE_VALUE, studentFaceFeature.getFeatureValue());
        db.insert(DatabaseHelper.TABLE_FACE_FEATURE, null, values);
        db.close();
    }

    public void updateFaceFeature(StudentFaceFeature studentFaceFeature) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_FEATURE_VALUE, studentFaceFeature.getFeatureValue());
        db.update(DatabaseHelper.TABLE_FACE_FEATURE, values,
                DatabaseHelper.KEY_STUDENT_ID + " = ?",
                new String[]{String.valueOf(studentFaceFeature.getStudentId())});
        db.close();
    }

    public void deleteFaceFeature(String studentId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_FACE_FEATURE,
                DatabaseHelper.KEY_STUDENT_ID + " = ?",
                new String[]{studentId});
        db.close();
    }

    public String getFeatureValue(String studentId) {
        String featureValue = null;
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_FACE_FEATURE,
                new String[]{DatabaseHelper.KEY_FEATURE_VALUE},
                DatabaseHelper.KEY_STUDENT_ID + " = ?",
                new String[]{studentId},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            featureValue = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FEATURE_VALUE));
            cursor.close();
        }

        db.close();
        return featureValue;
    }

    public List<StudentFaceFeature> getAllFaceFeatures() {
        List<StudentFaceFeature> faceFeatures = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_FACE_FEATURE,
                null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                StudentFaceFeature faceFeature = new StudentFaceFeature();
                faceFeature.setStudentId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_STUDENT_ID)));
                faceFeature.setFeatureValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_FEATURE_VALUE)));
                faceFeatures.add(faceFeature);
            }
            cursor.close();
        }

        db.close();
        return faceFeatures;
    }
}

