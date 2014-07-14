package com.example.ldurazo.androidfirebase.services.RESTful;

import android.util.Log;
import android.os.AsyncTask;

import com.example.ldurazo.androidfirebase.interfaces.HttpTransport;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class HttpClientImplementation extends AsyncTask<String, Void, String> implements HttpTransport {
    private static final String TAG = "AsyncTask";

    @Override
    protected String doInBackground(String... params) {
        Get();
        Post();
        Put();
        Delete();
        return null;
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the result
    }

    @Override
    public void Get() {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        HttpGet request = new HttpGet("https://ldurazoandroid.firebaseio.com/user.json"); //use your Firebase URL
        String line;
        try {
            response = client.execute(request);
            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            while ((line = rd.readLine()) != null) {
                Log.w(TAG, line);
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void Post() {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        HttpPost httppost = new HttpPost("https://ldurazoandroid.firebaseio.com/user.json");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lhol", "2j");
            jsonObject.put("aajaa", "ddkddd");
            httppost.setEntity(new StringEntity(jsonObject.toString()));

            // Execute HTTP Post Request
            response = client.execute(httppost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Put() {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        HttpPut httpPut = new HttpPut("https://ldurazoandroid.firebaseio.com/object.json");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lsl", "uuuu2");
            jsonObject.put("aada", "dduuuuddd");
            httpPut.setEntity(new StringEntity(jsonObject.toString()));

            response = client.execute(httpPut);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void Delete() {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        HttpDelete httpDelete = new HttpDelete("https://ldurazoandroid.firebaseio.com/object.json");
        try {
            client.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

