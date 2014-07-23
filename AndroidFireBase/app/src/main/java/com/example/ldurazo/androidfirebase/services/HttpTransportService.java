package com.example.ldurazo.androidfirebase.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.ldurazo.androidfirebase.services.RESTful.SpringImplementation;

public class HttpTransportService extends Service {
    private static final String TAG = "Service";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //new HttpClientImplementation().execute(TAG);
        new SpringImplementation().execute(TAG);
        stopSelf();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}

