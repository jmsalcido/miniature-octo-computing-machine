package com.example.ldurazo.xboxplayerexcercise.models;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldurazo on 7/24/2014 and 2:12 PM.
 */
public class AppSession {
    private long tokenExpireTime;
    private String accessToken;
    private static AppSession instance;
    private static final String TAG = "com.example.ldurazo.xboxplayerexcercise.models.appsession";
    public static final String CLIENT_SECRET = "LDKFAP345sdklfj542564654";
    public static final String CALLBACK_URL = "http://luisdurazoa.tumblr.com/";
    public static final String SERVICE = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
    public static final String SCOPE = "http://music.xboxlive.com";
    public static final String REFRESH_SCOPE = "https%3a%2f%2fapi.datamarket.azure.com%2f";
    public static final String SCOPE_SERVICE = "https://music.xboxlive.com";
    public static final String GRANT_TYPE = "client_credentials";
    public static final String GRANT_TYPE_REFRESH = "refresh_token";
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String CLIENT_ID = "musicplayer_internship_ldurazo";

    public static void initInstance() {
        if (instance == null) {
            instance = new AppSession();
        }
    }

    public static AppSession getInstance() {
        // Return the instance
        return instance;
    }

    private AppSession() {
        // Constructor hidden because this is a singleton
    }

    public Long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(Long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void refreshAccessToken() {
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 5000; //Timeout until a connection is established.
            int timeoutSocket = 50000; //Timeout for waiting for data.
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost request = new HttpPost(AppSession.SERVICE);
            request.setHeader("Content_type", AppSession.CONTENT_TYPE);
            request.setHeader("Accept", "application/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("client_id", CLIENT_ID));
            nameValuePairs.add(new BasicNameValuePair("client_secret", CLIENT_SECRET));
            nameValuePairs.add(new BasicNameValuePair("scope", SCOPE));
            nameValuePairs.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = entity.getContent();
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                Log.w(TAG, stringBuilder.toString());
                JSONObject responseObject = new JSONObject(stringBuilder.toString());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

