package com.example.ldurazo.xboxplayerexcercise.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.adapters.SearchAdapter;
import com.example.ldurazo.xboxplayerexcercise.asynctasks.OnSearchTaskCallback;
import com.example.ldurazo.xboxplayerexcercise.asynctasks.SearchAsyncTask;
import com.example.ldurazo.xboxplayerexcercise.models.Constants;
import com.example.ldurazo.xboxplayerexcercise.models.Track;

import java.util.ArrayList;

public class SearchActivity extends Activity implements OnSearchTaskCallback{
    private ListView listView;
    TextView textView;
    EditText editText;
    RadioButton artistRadioButton;
    RadioButton albumRadioButton;
    RadioButton songRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        artistRadioButton = (RadioButton) findViewById(R.id.artist_radio_button);
        albumRadioButton = (RadioButton) findViewById(R.id.album_radio_button);
        songRadioButton = (RadioButton) findViewById(R.id.song_radio_button);
    }

    public void searchArtist(View v){
        String search_query;
        String searchType;
        if(!editText.getText().toString().equals(Constants.EMPTY_STRING)){
            search_query=editText.getText().toString();
            search_query = search_query.replaceAll("\\s+", "+");
            searchType = getSearchType();
            new SearchAsyncTask(Constants.ACCESS_TOKEN, search_query, searchType, this).execute();
        }else{
            Toast.makeText(SearchActivity.this, "Please introduce a search text", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSearchType(){
        if(artistRadioButton.isChecked()){
            return Constants.ARTISTS;
        }if(albumRadioButton.isChecked()){
            return Constants.ALBUMS;
        }if(songRadioButton.isChecked()){
            return Constants.TRACKS;
        }
        return Constants.ARTISTS;
    }

    @Override
    public void onSearchCompleted(ArrayList<Track> list) {
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new SearchAdapter(getApplicationContext(), list));
    }
}
