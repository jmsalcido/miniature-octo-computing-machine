package com.example.ldurazo.xboxplayerexcercise.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ldurazo.xboxplayerexcercise.models.Constants;
import com.example.ldurazo.xboxplayerexcercise.models.Track;

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

public class SearchAsyncTask extends AsyncTask<Void, Void, ArrayList<Track>> {
    private String token;
    private String searchQuery;
    private String searchType;
    private OnSearchTaskCallback callback;

    public SearchAsyncTask(String token, String searchQuery, String searchType, OnSearchTaskCallback callback) {
        try {
            token = URLEncoder.encode(token, "UTF-8");
            this.token = token;
            this.callback = callback;
            this.searchQuery=searchQuery;
            this.searchType =searchType;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ArrayList<Track> doInBackground(Void... voids) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            // Calls the search flow to receive the json string
            InputStream inputStream = establishConnection();
            if(inputStream!=null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
                String inputLine;
                while((inputLine=bufferedReader.readLine())!=null){
                    stringBuilder.append(inputLine);
                    Log.w(Constants.TAG, inputLine);
                }
                return retrieveSearchResults(stringBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_LIST;
    }

    private InputStream establishConnection(){
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 10000; //Timeout until a connection is established.
            int timeoutSocket = 10000; //Timeout for waiting for data.
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpClient client = new DefaultHttpClient(httpParameters);
            String query = Constants.SCOPE_SERVICE+"/1/content/music/search?q="+searchQuery+"&accessToken=Bearer+"+"http%253a%252f%252fschemas.xmlsoap.org%252fws%252f2005%252f05%252fidentity%252fclaims%252fnameidentifier%3Dmusicplayer_internship_ldurazo%26http%253a%252f%252fschemas.microsoft.com%252faccesscontrolservice%252f2010%252f07%252fclaims%252fidentityprovider%3Dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26Audience%3Dhttp%253a%252f%252fmusic.xboxlive.com%26ExpiresOn%3D1406242478%26Issuer%3Dhttps%253a%252f%252fdatamarket.accesscontrol.windows.net%252f%26HMACSHA256%3DBox61JIlS%252b%252fEMbB2nelwsFHImNibGTHC0jRj7ZfHa4w%253d";
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

    private ArrayList<Track> retrieveSearchResults(String jsonString){
        try {
            ArrayList<Track> resultList= new ArrayList();
            JSONObject parentData = new JSONObject(jsonString);
            if(!parentData.isNull("Error")){
                Log.e(Constants.TAG, "There was no suitable result");
            }else{
                JSONObject searchTypeObject = parentData.getJSONObject(searchType);
                JSONArray searchResults = searchTypeObject.getJSONArray("Items");
                JSONObject searchObject;
                Track track;
                for (int i=0; i<searchResults.length();i++){
                    searchObject = searchResults.getJSONObject(i);
                    track = new Track(searchObject.getString("Id"),
                            searchObject.getString("Name"),
                            searchObject.getString("ImageUrl"),
                            searchType);
                    resultList.add(track);
                }
                return resultList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Constants.EMPTY_LIST;
    }

    @Override
    protected void onPostExecute(ArrayList<Track> s) {
        callback.onSearchCompleted(s);
        super.onPostExecute(s);
    }
}
