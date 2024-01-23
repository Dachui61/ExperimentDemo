package com.example.experimentdemo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.example.experimentdemo.model.LabReservation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ApiRequest {

    private static final Gson gson = new Gson();

    public static List<LabReservation> fetchLabData(String serialNumber) {
        try {
            OkHttpClient client = new OkHttpClient();

            String apiUrl = "https://sms.cqupt.edu.cn/openlab/roomboard/index/" + serialNumber;

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
}



