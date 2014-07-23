package com.example.ldurazo.androidfirebase.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.ldurazo.androidfirebase.R;
import com.example.ldurazo.androidfirebase.services.HttpTransportService;


public class MainActivity extends Activity {
private static final String TAG = "Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, HttpTransportService.class);
        startService(intent);
    }
}
