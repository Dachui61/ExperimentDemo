package com.example.experimentdemo.utils;


import android.os.AsyncTask;
import android.widget.TextView;

import com.example.experimentdemo.ApiRequest;
import com.example.experimentdemo.model.LabReservation;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LabReservationUtils {
    //获取当前是否有实验
    public static LabReservation getCurrentExperiment(List<LabReservation> labReservationList){
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);

        for (LabReservation reservation : labReservationList) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startTime = sdf.parse(reservation.getStartTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(startTime);
                cal.add(Calendar.HOUR, 1); // 减去一小时
                Date oneHourAfterStartTime = cal.getTime();

                if (currentDate.after(startTime) && currentDate.before(oneHourAfterStartTime)) {
                    return reservation;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null; // 没有正在进行的实验
    }

    // 筛选当前时间之后的实验信息
    public static List<LabReservation> getUpcomingReservations(List<LabReservation> labReservationList){
        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);

        List<LabReservation> upcomingReservations = new ArrayList<>();
        for (LabReservation reservation : labReservationList) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startTime = sdf.parse(reservation.getStartTime());
                if (startTime.after(currentDate)) {
                    upcomingReservations.add(reservation);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return upcomingReservations;
    }

    public static void updateCurrentTime(TextView currentTimeTextView, TextView currentTimeTextView2) {
        // 获取当前日期时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());

        // 获取当前星期
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String currentWeekDay = getWeekDayString(dayOfWeek);

        // 更新 TextView
        String displayText = currentDateTime + "\n" + currentWeekDay;
        currentTimeTextView.setText(displayText);
        currentTimeTextView2.setText(displayText);
    }

    public static String getWeekDayString(int dayOfWeek) {
        String[] weekDays = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return weekDays[dayOfWeek];
    }

    public static String getItemName(LabReservation continueLab){
        String itemName;
        if ("场地".equals(continueLab.getOpenType())) {
            itemName = "开放场地";
        } else {
            //有待测试
            itemName = continueLab.getExperimentList().toString(); // 或者其他适当的字段
        }
        return itemName;
    }

}
