package com.example.ldurazo.xboxplayerexcercise.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.ldurazo.xboxplayerexcercise.models.Constants;
import com.example.ldurazo.xboxplayerexcercise.models.Token;

public class TokenRefreshBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(Constants.TAG, "masdfasdfasdf");
        AlarmManager alarmManager;
        Token.TOKEN_EXPIRE_TIME = System.currentTimeMillis()+5000;
        Intent tokenRefreshIntent = new Intent(context, this.getClass());
        PendingIntent tokenRefreshPendingIntent = PendingIntent.getBroadcast
                (context, 0, tokenRefreshIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, Token.TOKEN_EXPIRE_TIME, tokenRefreshPendingIntent);
    }
}
