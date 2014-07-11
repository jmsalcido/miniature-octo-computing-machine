package com.example.ldurazo.androidfirebase.interfaces;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public interface HttpTransportHttpClient {

    public void GET();

    public void POST();

    public void PUT();

    public void DELETE();
}
