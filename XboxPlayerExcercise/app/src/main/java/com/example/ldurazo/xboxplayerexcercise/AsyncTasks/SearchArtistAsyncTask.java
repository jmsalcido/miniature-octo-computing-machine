package com.example.ldurazo.xboxplayerexcercise.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ldurazo.xboxplayerexcercise.models.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchArtistAsyncTask extends AsyncTask<Void, Void, ArrayList<Object>> {
    private String token;
    private String searchQuery;
    private String searchType;
    public SearchArtistAsyncTask(String token, String searchQuery, String searchType) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
            this.token = token;
            this.searchQuery=searchQuery;
            this.searchType =searchType;
        } catch (UnsupportedEncodingException e) {
            this.token = Constants.ERROR;
            this.searchQuery=Constants.ERROR;
            this.searchType =Constants.ERROR;
            e.printStackTrace();
        }
    }

    @Override
    protected ArrayList<Object> doInBackground(Void... voids) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = establishConnection();
            if(inputStream!=null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
                String inputLine;
                while((inputLine=bufferedReader.readLine())!=null){
                    stringBuilder.append(inputLine);
                    Log.w(Constants.TAG, inputLine);
                }
                return retrieveArtist(stringBuilder.toString());
            }else {
                return Constants.EMPTY_LIST;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_LIST;
    }

    @Override
    protected void onPostExecute(ArrayList<Object> s) {
        super.onPostExecute(s);
    }

    private InputStream establishConnection(){
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 10000; //Timeout until a connection is established.
            int timeoutSocket = 10000; //Timeout for waiting for data.
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpClient client = new DefaultHttpClient(httpParameters);
            String query = Constants.SCOPE_SERVICE+"/1/content/music/search?q="+searchQuery+"&accessToken=Bearer+"+token;
            Log.w(Constants.TAG,query);
            HttpGet request = new HttpGet(query);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            return entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Object> retrieveArtist(String jsonString){
        try {
            ArrayList<Object> resultList= new ArrayList();
            JSONObject parentData = new JSONObject(jsonString);
            if(!parentData.isNull("Error")){
                Log.w(Constants.TAG, "Made it");
                return Constants.EMPTY_LIST;
            }else{
                JSONObject searchTypeObject = parentData.getJSONObject(searchType);
                JSONArray searchResults = searchTypeObject.getJSONArray("Items");
                JSONObject searchObject;
                for (int i=0; i<searchResults.length();i++){
                    searchObject = searchResults.getJSONObject(i);
                    resultList.add(searchObject);
                    Log.w(Constants.TAG, searchObject.getString("Name"));
                }
                return resultList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return Constants.EMPTY_LIST;
        }
    }
}
