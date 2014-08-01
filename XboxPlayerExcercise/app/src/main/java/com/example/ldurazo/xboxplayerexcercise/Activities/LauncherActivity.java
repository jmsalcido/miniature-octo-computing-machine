package com.example.ldurazo.xboxplayerexcercise.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.adapters.TokenRefreshBroadcastReceiver;
import com.example.ldurazo.xboxplayerexcercise.asynctasks.OnTokenTaskCallback;
import com.example.ldurazo.xboxplayerexcercise.asynctasks.TokenObtainableAsyncTask;
import com.example.ldurazo.xboxplayerexcercise.models.AppSession;


public class LauncherActivity extends BaseActivity implements OnTokenTaskCallback{
    private static final String TAG = "com.example.ldurazo.xboxplayerexcercise.activities.baseactivity";
    private ProgressDialog dialog;
    private TextView launcherText;
    private Animation animation;
    private PendingIntent tokenRefreshPendingIntent;
    private AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initToken();
        initVars();
        initUI();
    }

    protected void initToken(){
        new TokenObtainableAsyncTask(this).execute();
    }

    @Override
    protected void initVars() {
        dialog = new ProgressDialog(LauncherActivity.this);
        dialog.setTitle("Please wait...");
        dialog.setProgressStyle(R.style.AppTheme);
    }

    @Override
    protected void initUI(){
        launcherText = (TextView) findViewById(R.id.launchText);
        animation = AnimationUtils.loadAnimation(LauncherActivity.this, R.anim.blink);
        launcherText.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alarmManager!=null && tokenRefreshPendingIntent!=null){
            alarmManager.cancel(tokenRefreshPendingIntent);
        }
    }

    @Override
    public void onTokenReceived(String response) {
        Log.w(TAG, response);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        AppSession.getInstance().setTokenExpireTime(System.currentTimeMillis() + (1000 * 60 * 10));
        Intent tokenRefreshIntent = new Intent(this, TokenRefreshBroadcastReceiver.class);
        tokenRefreshPendingIntent = PendingIntent.getBroadcast
                (LauncherActivity.this, 0, tokenRefreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, AppSession.getInstance().getTokenExpireTime(), 1000 * 60 * 10, tokenRefreshPendingIntent);
        Intent searchIntent = new Intent(LauncherActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }


    @Override
    public void onTokenNotReceived() {
        if(!dialog.isShowing()){
            dialog.show();
        }
        new TokenObtainableAsyncTask(this).execute();
    }
}