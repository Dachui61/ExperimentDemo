package com.example.experimentdemo;

import static com.example.experimentdemo.ApiRequest.fetchstudentData;
import static com.example.experimentdemo.utils.LabReservationUtils.getCurrentExperiment;
import static com.example.experimentdemo.utils.LabReservationUtils.getItemName;
import static com.example.experimentdemo.utils.LabReservationUtils.getUpcomingReservations;
import static com.example.experimentdemo.utils.LabReservationUtils.updateCurrentTime;

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
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.experimentdemo.model.LabReservation;
import com.example.experimentdemo.model.OpenLabStudent;
import com.example.experimentdemo.utils.LabReservationUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
    private TextView experimentItemName;
    private TextView roomName;
    private TextView LabOpHoursTV;
    private TextView LabOpItemTV;
    private TextView LabTeacher;
    private TextView LabOpContinueItem;

    private TextView sumAttendNum; //变化
    private TextView curAttendNum; //
    private TextView sumResNum; //固定值
    private TextView curResNum; //固定值
    private List<LabReservation> labReservationList;

    private List<OpenLabStudent> openLabStudents ;
    //总签到数
    private int today_attend_cnt = 0;
    //当前时段签到数
    private int cur_attend_cnt = 0;
    //总预约数
    private int today_reservation = 0;
    //当前时段预约数
    private int cur_reservation = 0;

    //最近签到人员信息
    private TextView rescentAttendPerson;



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
        LabOpHoursTV = findViewById(R.id.openLabOpeningHours);
        LabOpItemTV = findViewById(R.id.openLabOpeningItem);
        LabTeacher = findViewById(R.id.openLabTeacher);
        LabOpContinueItem = findViewById(R.id.openLabContinueItem);

        sumResNum = findViewById(R.id.sumBookingNum);
        curResNum = findViewById(R.id.curBookingNum);
        sumAttendNum = findViewById(R.id.sumSignupNum);
        curAttendNum = findViewById(R.id.curSignupNum);

        experimentItemName = findViewById(R.id.experimentItemName);
        rescentAttendPerson = findViewById(R.id.experimentStatusTextView6);

        // 使用 Handler 定时更新时间
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateCurrentTime(currentTimeTextView,currentTimeTextView2);
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
                    //更新总预约数
                    for(LabReservation lr : labReservationList) {
                        today_reservation += lr.getReservationCount();
                    }
                    showOpenLab();
                } else {
                    // 处理网络请求失败的情况
                }
            }
        }.execute("VCPIDOM70L");


        //绑定按钮 签到
        Button faceRecognitionButton = findViewById(R.id.faceRecognitionButton);
        faceRecognitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里定义按钮点击时执行的操作
                // 例如，调用人脸认证的方法
                String deviceId = "VCPIDOM70L";
//                String reserveId = "531";
                //测试
//                int reserveId = 531;
//                根据学号和时间来获取对应的预约序号
                String studentId = "2020210531";
                getStudentRID(studentId, new OnStudentRIDReceivedListener() {
                    @Override
                    public void onStudentRIDReceived(OpenLabStudent stu) {
                        // 在这里处理获取到的预约序号
                        if (stu != null) {
                            // 找到了相应的学生，执行你需要的逻辑
                            performFaceRecognition(deviceId, stu);
                        } else {
                            // 未找到相应的学生或者网络请求失败，执行相应的逻辑
                            Toast.makeText(v.getContext(), "当前实验未检查到学生信息，请检查后重试", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void getStudentRID(String studentId, final OnStudentRIDReceivedListener listener) {
        LabReservation labReservation = getCurrentExperiment(labReservationList);
        if (labReservation == null) {
            listener.onStudentRIDReceived(null); // 回调函数通知调用方结果
        } else {
            int openLabindex = labReservation.getOpenNumber();
            new AsyncTask<String, Void, List<OpenLabStudent>>() {

                @Override
                protected List<OpenLabStudent> doInBackground(String... params) {
                    return ApiRequest.fetchstudentData(params[0]);
                }

                @Override
                protected void onPostExecute(List<OpenLabStudent> result) {
                    if (result != null) {
                        // 处理数据
                        int reservationNumber = -1; // 默认值
                        OpenLabStudent res = null;
                        for (OpenLabStudent stu : result) {
                            if (stu.getStudentId().equals(studentId)) { // 使用 equals 方法比较字符串
                                reservationNumber = stu.getReservationNumber();
                                res = stu;
                                break; // 找到学生后停止遍历
                            }
                        }
                        listener.onStudentRIDReceived(res); // 回调函数通知调用方结果
                    } else {
                        // 处理网络请求失败的情况
                        listener.onStudentRIDReceived(null); // 回调函数通知调用方结果
                    }
                }
            }.execute(String.valueOf(openLabindex));
        }
    }

    // 定义一个回调接口
    interface OnStudentRIDReceivedListener {
        void onStudentRIDReceived(OpenLabStudent stu);
    }


    //显示整体信息
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

        sumResNum.setText(String.valueOf(today_reservation));
        displayCurLab(labReservationList);

        List<LabReservation> upcomingReservations = getUpcomingReservations(labReservationList);

        // 将筛选后的实验信息列表展示在界面的 openLabListView 上
        displayUpcomingReservations(upcomingReservations);
    }

    public void addAttendNum(){
        //总签到数
        today_attend_cnt++;
        //当前时段签到数
        cur_attend_cnt++;

        sumAttendNum.setText(String.valueOf(today_attend_cnt)); //变化
        curAttendNum.setText(String.valueOf(cur_attend_cnt)); //



    }


    private void displayCurLab(List<LabReservation> reservations){
        LabReservation reservation = getCurrentExperiment(reservations);
        if(reservation != null){
            //有实验信息
            TextView experimentStatus = findViewById(R.id.experimentStatusTextView);
            experimentStatus.setText("实验室开放中");
            cur_reservation = reservation.getReservationCount();
            curResNum.setText(String.valueOf(cur_reservation));

            experimentItemName.setText(getItemName(reservation));

            LabOpHoursTV.setText(reservation.getStartTime());
            LabTeacher.setText(reservation.getTeacher());
            LabOpItemTV.setText(reservation.getOpenType());
            LabReservation next = getUpcomingReservations(reservations).get(0);

            LabOpContinueItem.setText(getItemName(next));
        }else{
            //没有实验信息
            TextView experimentStatus = findViewById(R.id.experimentStatusTextView);
            experimentStatus.setText("实验室关闭中");

            experimentItemName.setText("无");
            LabOpHoursTV.setText("无");
            LabTeacher.setText("无");
            LabOpItemTV.setText("无");
            LabOpContinueItem.setText("无");
            curResNum.setText("0");
            curAttendNum.setText("0");

        }
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

    public void performFaceRecognition(String deviceId,OpenLabStudent stu) {
        // 调用方法
        boolean arrive = true; // 到达状态，可以根据需要设置
        String reserveId = String.valueOf(stu.getReservationNumber());
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                return ApiRequest.sendAttendance(params[0],params[1],Boolean.parseBoolean(params[2]));
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    // 处理数据
                    System.out.println(result);
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(result, JsonObject.class);
                    boolean res = jsonResponse.get("result").getAsBoolean();
                    if(res){
                        //签到成功
                        addAttendNum();
                        //获取当前时间
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String currentDateTime = dateFormat.format(new Date());
                        String recentInfo = stu.getName()+" "+currentDateTime;
                        rescentAttendPerson.setText(recentInfo);
                    }
                    return;
                } else {
                    // 处理网络请求失败的情况
                }
            }
        }.execute(deviceId, reserveId, String.valueOf(arrive));
    }

}