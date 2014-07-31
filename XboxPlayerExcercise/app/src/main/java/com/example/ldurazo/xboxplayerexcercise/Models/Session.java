package com.example.ldurazo.xboxplayerexcercise.models;

/**
 * Created by ldurazo on 7/24/2014 and 2:12 PM.
 */
public class Session {
    private static long tokenExpireTime;
    private static String accessToken;
    private static Session instance;

    public static final String CLIENT_SECRET = "LDKFAP345sdklfj542564654";
    public static final String CALLBACK_URL = "http://luisdurazoa.tumblr.com/";
    public static final String SERVICE = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13";
    public static final String SCOPE = "http://music.xboxlive.com";
    public static final String SCOPE_SERVICE = "https://music.xboxlive.com";
    public static final String GRANT_TYPE = "client_credentials";
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    public static final String CLIENT_ID = "musicplayer_internship_ldurazo";

    public static void initInstance(){
        if(instance == null){
            instance = new Session();
        }
    }

    public static Session getInstance()
    {
        // Return the instance
        return instance;
    }

    private Session()
    {
        // Constructor hidden because this is a singleton
    }

    public static Long getTokenExpireTime(){
        return tokenExpireTime;
    }

    public static void setTokenExpireTime(Long tokenExpireTime){
        Session.tokenExpireTime = tokenExpireTime;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        Session.accessToken = accessToken;
    }
}
