package com.example.ldurazo.androidfirebase.services.RESTful;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ldurazo.androidfirebase.interfaces.HttpTransport;
import com.example.ldurazo.androidfirebase.models.Person;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class SpringImplementation extends AsyncTask<String, Void, String> implements HttpTransport {
    private static final String TAG = "AsyncTask";
    private Person person;
    private RestTemplate restTemplate = new RestTemplate();
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
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            person = restTemplate.getForObject("https://ldurazoandroid.firebaseio.com/randomobject.json", Person.class);
            Log.w(TAG, person.getName()+"/"+ person.getAge());
        }catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
    }

    @Override
    public void Post() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.postForEntity("https://ldurazoandroid.firebaseio.com/person.json", person, Person.class, TAG);
    }

    @Override
    public void Put() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.put("https://ldurazoandroid.firebaseio.com/lol.json", person, Person.class);
    }

    @Override
    public void Delete() {
        restTemplate = new RestTemplate();
        restTemplate.delete("https://ldurazoandroid.firebaseio.com/DELETE.json");
    }
}

