package com.example.ldurazo.androidfirebase.services;


import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HttpTransportService extends IntentService {

    public HttpTransportService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

