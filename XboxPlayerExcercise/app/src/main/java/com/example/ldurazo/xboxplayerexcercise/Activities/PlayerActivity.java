package com.example.ldurazo.xboxplayerexcercise.Activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.ldurazo.xboxplayerexcercise.Models.Constants;
import com.example.ldurazo.xboxplayerexcercise.R;

public class PlayerActivity extends Activity {
    private String accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        accessToken=getIntent().getExtras().getString(Constants.TOKEN);
    }

}
