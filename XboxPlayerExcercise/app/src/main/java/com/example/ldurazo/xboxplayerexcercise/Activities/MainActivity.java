package com.example.ldurazo.xboxplayerexcercise.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ldurazo.xboxplayerexcercise.asynctasks.SearchArtistAsyncTask;
import com.example.ldurazo.xboxplayerexcercise.models.Constants;
import com.example.ldurazo.xboxplayerexcercise.R;

public class MainActivity extends Activity {
    private String accessToken;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessToken=getIntent().getExtras().getString(Constants.TOKEN);
        initUI();
    }

    private void initUI(){
        textView = (TextView) findViewById(R.id.textView);
    }

    public void searchArtist(View v){
        new SearchArtistAsyncTask(accessToken).execute();
    }
}
