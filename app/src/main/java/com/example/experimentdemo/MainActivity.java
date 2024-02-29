package com.example.experimentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.experimentdemo.model.LabReservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView currentTimeTextView;
    private TextView currentTimeTextView2;
    private ListView openLabListView;
    private TextView roomName;
    private TextView nextLabOpHoursTV;
    private TextView nextLabOpItemTV;
    private TextView nextLabTeacher;
    private TextView nextLabOpContinueItem;
    private List<LabReservation> labReservationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initializeDatabase(this);
    }

    private static void initializeDatabase(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // 检查数据库是否已存在
        if (!databaseExists(dbHelper)) {
            // 如果数据库不存在，则创建和初始化
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // 关闭数据库连接
            db.close();
        }
    }

    private static boolean databaseExists(DatabaseHelper dbHelper) {
        SQLiteDatabase checkDB = null;
        try {
            // 尝试打开数据库
            checkDB = SQLiteDatabase.openDatabase(dbHelper.getDatabaseName(), null, SQLiteDatabase.OPEN_READONLY);
            // 关闭数据库连接
            if (checkDB != null) {
                checkDB.close();
            }
        } catch (SQLiteException e) {
            // 数据库不存在
            return false;
        }
        return checkDB != null;
    }

    private void init(){
        // 隐藏状态栏和导航栏
        View decorView = getWindow().getDecorView();


        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        currentTimeTextView = findViewById(R.id.currentTime);
        currentTimeTextView2 = findViewById(R.id.currentTime2);
        openLabListView = findViewById(R.id.openLabListView);

        roomName = findViewById(R.id.experimentRoomName);
        nextLabOpHoursTV = findViewById(R.id.openLabOpeningHours);
        nextLabOpItemTV = findViewById(R.id.openLabOpeningItem);
        nextLabTeacher = findViewById(R.id.openLabTeacher);
        nextLabOpContinueItem = findViewById(R.id.openLabContinueItem);

        // 使用 Handler 定时更新时间
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateCurrentTime();
                handler.postDelayed(this, 1000); // 每隔1秒更新一次时间
            }
        });

        //测试请求数据 参数为班牌序列号
        new AsyncTask<String, Void, List<LabReservation>>() {

            @Override
            protected List<LabReservation> doInBackground(String... params) {
                return ApiRequest.fetchLabData(params[0]);
            }

            @Override
            protected void onPostExecute(List<LabReservation> result) {
                if (result != null) {
                    // 处理数据
                    labReservationList = result;
                    showOpenLab();
                } else {
                    // 处理网络请求失败的情况
                }
            }
        }.execute("VCPIDOM70L");

    }

    private void showOpenLab(){

        //设置实验室名称
        if(labReservationList.isEmpty()){
            //开放信息为空 设置默认的实验室名称
            //1、需要查找是否有持久化的数据

            //2、没有持久化的数据则设置为默认值
            roomName.setText("实验室");

        }else{
            //不为空
            roomName.setText(labReservationList.get(0).getLabName());
        }


        // 获取当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);

        // 筛选当前时间之后的实验信息
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

        // 将筛选后的实验信息列表展示在界面的 openLabListView 上
        displayUpcomingReservations(upcomingReservations);
    }

    private String getItemName(LabReservation continueLab){
        String itemName;
        if ("场地".equals(continueLab.getOpenType())) {
            itemName = "开放场地";
        } else {
            //有待测试
            itemName = continueLab.getExperimentList().get(0); // 或者其他适当的字段
        }
        return itemName;
    }
    private void displayUpcomingReservations(List<LabReservation> reservations) {

        // 显示当前的课程列表
        // 创建适配器，将实验信息列表展示在 ListView 上
        ArrayAdapter<SpannableString> adapter = new ArrayAdapter<SpannableString>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                //通过getView设置字体大小
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);

                // 设置字体大小为40sp
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

                return view;
            }
        };

        // 填充适配器的数据
        for (LabReservation reservation : reservations) {
            String itemName;
            if ("场地".equals(reservation.getOpenType())) {
                itemName = "开放场地";
            } else {
                //有待测试
                itemName = reservation.getExperimentList().get(0); // 或者其他适当的字段
            }

            String dateTimeString = reservation.getStartTime();

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
            try {
                Date date = inputFormat.parse(dateTimeString);
                String timeOnly = outputFormat.format(date);
                String displayText = timeOnly + "   " + itemName;

                // 将itemName显示为绿色
                SpannableString spannableString = new SpannableString(displayText);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.argb(255, 4, 78, 42));
                spannableString.setSpan(colorSpan, displayText.indexOf(itemName), displayText.indexOf(itemName) + itemName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                adapter.add(spannableString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        openLabListView.setAdapter(adapter);


    }

    private void updateCurrentTime() {
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

    private String getWeekDayString(int dayOfWeek) {
        String[] weekDays = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return weekDays[dayOfWeek];
    }
}