package com.example.ldurazo.xboxplayerexcercise.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.AsyncTasks.TokenObtainableAsyncTask;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new TokenObtainableAsyncTask(this).execute();
    }
}
