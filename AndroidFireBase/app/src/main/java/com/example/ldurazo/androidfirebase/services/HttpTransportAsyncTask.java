package com.example.ldurazo.androidfirebase.services;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class HttpTransportAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = "ldurazo";
    @Override
    protected String doInBackground(String... strings) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://ldurazoandroid.firebaseio.com/user.json"); //use your Firebase URL
        HttpResponse response;
        String line=TAG;
        try {
            response = client.execute(request);

            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            while((line = rd.readLine()) != null){
                Log.w(TAG, line);
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return line;
    }

    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}

