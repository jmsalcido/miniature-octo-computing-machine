package com.example.ldurazo.androidfirebase.services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ldurazo.androidfirebase.interfaces.HttpTransport;
import com.example.ldurazo.androidfirebase.models.Person;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class SpringImplementation extends AsyncTask<String, Void, String> implements HttpTransport {
    private static final String TAG = "AsyncTask";

    @Override
    protected String doInBackground(String... params) {
        Get();
        /*Post();
        Put();
        Delete();*/
        return null;
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the result
    }


    @Override
    public void Get() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Person person = restTemplate.getForObject("https://ldurazoandroid.firebaseio.com/randomobject.json", Person.class);
            Log.w(TAG, person.getName()+"/"+ person.getAge());
        }catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
    }

    @Override
    public void Post() {

    }

    @Override
    public void Put() {

    }

    @Override
    public void Delete() {

    }
}

