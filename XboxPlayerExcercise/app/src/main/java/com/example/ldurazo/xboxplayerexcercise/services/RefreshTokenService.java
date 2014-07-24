package com.example.ldurazo.xboxplayerexcercise.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.ldurazo.xboxplayerexcercise.models.Constants;


public class RefreshTokenService extends IntentService {

    public RefreshTokenService(){
        super(Constants.TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while(true){
            synchronized (this){
                try{
                    wait(3000);
                    Log.w(Constants.TAG,"HERE");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
