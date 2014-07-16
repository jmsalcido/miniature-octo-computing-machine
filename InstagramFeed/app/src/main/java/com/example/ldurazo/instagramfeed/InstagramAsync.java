package com.example.ldurazo.instagramfeed;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InstagramAsync extends AsyncTask<String, Void, String> {
    public final static String TAG = "INSTA";
    public final static String CLIENT_ID = "d52381c315fc46b88e1e0821af6c37a0";
    public final static String CLIENT_SECRET = "3acc7e996418463fa70d7220d41a495a";
    public final static String CALLBACK_URI = "http://luisdurazoa.tumblr.com";
    public final static String TOKEN_URL = "https://api.instagram.com/v1/media/popular/?client_id=" + CLIENT_ID;
    private ListActivity activity;
    private List<InstaObject> instaList;

    public InstagramAsync(ListActivity activity) {this.activity = activity;}

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        instaList = new ArrayList<InstaObject>();
        HttpResponse response;
        HttpGet request = new HttpGet(TOKEN_URL);
        String line;
        try {
            response = client.execute(request);
            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject parentData = new JSONObject(stringBuilder.toString());
            JSONArray parentDataArray = parentData.getJSONArray("data");
            JSONObject tempObject;
            JSONObject imagesObject;
            JSONObject captionObject;
            for(int i=0; i<parentDataArray.length();i++) {
                InstaObject instaObject = new InstaObject();
                tempObject = parentDataArray.getJSONObject(i);
                imagesObject = tempObject.getJSONObject("images").getJSONObject("thumbnail");
                instaObject.setSmallImage(imagesObject.getString("url"));
                imagesObject = tempObject.getJSONObject("images").getJSONObject("low_resolution");
                instaObject.setLargeImage(imagesObject.getString("url"));
                if(tempObject.isNull("caption")){
                    instaObject.setDescription("No description");
                } else{
                    captionObject = tempObject.getJSONObject("caption");
                    instaObject.setDescription(captionObject.getString("text"));
                }
                instaObject.setSmallImageBitmap(BitMapCreator(instaObject.getSmallImage()));
                instaObject.setLargeImageBitmap(BitMapCreator(instaObject.getLargeImage()));
                instaList.add(instaObject);
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.setListAdapter(new InstaGramAdapter(activity, instaList));
    }

    public Bitmap BitMapCreator(String imageurl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = null;
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
