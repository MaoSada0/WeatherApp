package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GisService extends Service {

    static final String CHANNEL = "GisService";
    static final String INFO = "INFO";

    Handler handler;
    String currentCity = "London";


    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                String str = (String) msg.obj;

                Intent i = new Intent(CHANNEL);
                i.putExtra(INFO, str);
                sendBroadcast(i);
            }
        };


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String city = intent.getStringExtra("city");

        if (city != null && !city.equals(currentCity)) {
            currentCity = city;
            Thread weatherThread = new Thread(new HttpRequest(handler, city));
            weatherThread.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
