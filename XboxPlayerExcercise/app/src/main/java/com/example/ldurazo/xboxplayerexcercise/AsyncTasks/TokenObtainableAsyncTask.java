package com.example.ldurazo.xboxplayerexcercise.AsyncTasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.ldurazo.xboxplayerexcercise.Activities.PlayerActivity;
import com.example.ldurazo.xboxplayerexcercise.Models.Constants;


public class TokenObtainableAsyncTask extends AsyncTask<String,Void,String>{
    private Activity activity;

    public TokenObtainableAsyncTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Intent playerIntent = new Intent(activity, PlayerActivity.class);
        playerIntent.putExtra(Constants.TOKEN, result);
        activity.startActivity(playerIntent);
        activity.finish();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "example";
    }
}
