package com.example.ldurazo.xboxplayerexcercise.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.ldurazo.xboxplayerexcercise.AsyncTasks.OnTokenTaskCallback;
import com.example.ldurazo.xboxplayerexcercise.AsyncTasks.TokenObtainableAsyncTask;
import com.example.ldurazo.xboxplayerexcercise.Models.Constants;
import com.example.ldurazo.xboxplayerexcercise.R;


public class LauncherActivity extends Activity implements OnTokenTaskCallback{
    ProgressDialog dialog;
    TextView launcherText;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(LauncherActivity.this);
        setContentView(R.layout.activity_launcher);

        new TokenObtainableAsyncTask(this).execute();
        initUI();
    }

    private void initUI(){
        launcherText = (TextView) findViewById(R.id.launchText);
        animation = AnimationUtils.loadAnimation(LauncherActivity.this, R.anim.blink);
        launcherText.startAnimation(animation);
    }

    @Override
    public void onTokenReceived(String response) {
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        Intent playerIntent = new Intent(LauncherActivity.this, MainActivity.class);
        playerIntent.putExtra(Constants.TOKEN, response);
        startActivity(playerIntent);
        finish();
    }

    @Override
    public void onTokenNotReceived() {
        if(!dialog.isShowing()){
            dialog.show();
        }
        new TokenObtainableAsyncTask(this).execute();
    }
}
