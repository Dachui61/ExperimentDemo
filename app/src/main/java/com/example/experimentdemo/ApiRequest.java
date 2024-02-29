package com.example.experimentdemo;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.experimentdemo.model.LabReservation;
import com.example.experimentdemo.model.OpenLabStudent;
import com.example.experimentdemo.model.StudentFaceFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiRequest {

    private static final Gson gson = new Gson();

    public static List<LabReservation> fetchLabData(String serialNumber) {
        try {
            OkHttpClient client = new OkHttpClient();

//            String apiUrl = "https://sms.cqupt.edu.cn/openlab/roomboard/index/" + serialNumber;
            String apiUrl = "http://10.16.59.240/roomboard/index/" + serialNumber;

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 将JSON数组映射到LabReservation类的实例列表
                Type listType = new TypeToken<List<LabReservation>>(){}.getType();
                return gson.fromJson(response.body().string(), listType);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<StudentFaceFeature> fetchfaceData(String serialNumber) {
        try {
            OkHttpClient client = new OkHttpClient();

//            String apiUrl = "https://sms.cqupt.edu.cn/openlab/roomboard/index/" + serialNumber;
            String apiUrl = "http://10.16.59.240/roomboard/faceids/" + serialNumber;

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 将JSON数组映射到LabReservation类的实例列表
                Type listType = new TypeToken<List<StudentFaceFeature>>(){}.getType();
                return gson.fromJson(response.body().string(), listType);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<OpenLabStudent> fetchstudentData(String openLabindex) {
        try {
            OkHttpClient client = new OkHttpClient();

//            String apiUrl = "https://sms.cqupt.edu.cn/openlab/roomboard/index/" + serialNumber;
            String apiUrl = "http://10.16.59.240/roomboard/students/" + openLabindex;

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // 将JSON数组映射到LabReservation类的实例列表
                Type listType = new TypeToken<List<OpenLabStudent>>(){}.getType();
                return gson.fromJson(response.body().string(), listType);
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sendAttendance(String deviceId, String reserveId, boolean arrive) {
        try {
            OkHttpClient client = new OkHttpClient();

            String apiUrl = "http://10.16.59.240/roomboard/setAttendance";

            // 构建请求体
            FormBody.Builder formBodyBuilder = new FormBody.Builder()
                    .add("Id", deviceId)
                    .add("reserveId", reserveId)
                    .add("arrive", String.valueOf(arrive));

            RequestBody requestBody = formBodyBuilder.build();

            // 构建请求
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build();

            // 发送请求并处理响应
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            String responseBody = response.body().string();
            // 如果需要处理响应内容，请在这里进行处理
            return responseBody;
        } catch (IOException e) {
            e.printStackTrace(); // 输出异常信息
            return null;
        }
    }
}



