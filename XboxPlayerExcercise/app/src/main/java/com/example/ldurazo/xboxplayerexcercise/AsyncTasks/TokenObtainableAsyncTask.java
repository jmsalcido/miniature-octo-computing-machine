package com.example.ldurazo.xboxplayerexcercise.AsyncTasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ldurazo.xboxplayerexcercise.Activities.PlayerActivity;
import com.example.ldurazo.xboxplayerexcercise.Models.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class TokenObtainableAsyncTask extends AsyncTask<String, Void, String> {
    private Activity activity;

    public TokenObtainableAsyncTask(Activity activity) {
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
        String postData = "client_id=" + Constants.CLIENT_ID
                + "&client_secret=" + Constants.CLIENT_SECRET
                + "&scope=" + Constants.SCOPE
                + "&grant_type=" + Constants.GRANT_TYPE;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(Constants.SERVICE);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("client_id",Constants.CLIENT_ID));
            nameValuePairs.add(new BasicNameValuePair("client_secret",Constants.CLIENT_SECRET));
            nameValuePairs.add(new BasicNameValuePair("scope",Constants.SCOPE));
            nameValuePairs.add(new BasicNameValuePair("grant_type",Constants.GRANT_TYPE));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
            String line = null;
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
                Log.w(Constants.TAG, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "example";
    }
}
