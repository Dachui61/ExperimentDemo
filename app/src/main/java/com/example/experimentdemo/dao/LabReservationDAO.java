package com.example.experimentdemo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import com.example.experimentdemo.DatabaseHelper;
import com.example.experimentdemo.model.LabReservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LabReservationDAO {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public LabReservationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertLabReservation(LabReservation labReservation) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_OPEN_NUMBER, labReservation.getOpenNumber());
        values.put(DatabaseHelper.KEY_START_TIME, labReservation.getStartTime());
        values.put(DatabaseHelper.KEY_LAB_NAME, labReservation.getLabName());
        values.put(DatabaseHelper.KEY_RESERVATION_COUNT, labReservation.getReservationCount());
        values.put(DatabaseHelper.KEY_TEACHER, labReservation.getTeacher());
        values.put(DatabaseHelper.KEY_OPEN_TYPE, labReservation.getOpenType());
        values.put(DatabaseHelper.KEY_EXPERIMENT_LIST, TextUtils.join(",", labReservation.getExperimentList()));

        database.insert(DatabaseHelper.TABLE_LAB_RESERVATION, null, values);
    }

    public List<LabReservation> getAllLabReservations() {
        List<LabReservation> labReservations = new ArrayList<>();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_LAB_RESERVATION,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                LabReservation labReservation = new LabReservation();
                labReservation.setOpenNumber(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_OPEN_NUMBER)));
                labReservation.setStartTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_START_TIME)));
                labReservation.setLabName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_LAB_NAME)));
                labReservation.setReservationCount(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.KEY_RESERVATION_COUNT)));
                labReservation.setTeacher(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TEACHER)));
                labReservation.setOpenType(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_OPEN_TYPE)));
                // 解析实验项目列表（以逗号分隔）
                String experimentListString = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_EXPERIMENT_LIST));
                List<String> experimentList = Arrays.asList(experimentListString.split("\\s*,\\s*"));
                labReservation.setExperimentList(experimentList);

                labReservations.add(labReservation);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return labReservations;
    }
    // 其他操作方法...

}
