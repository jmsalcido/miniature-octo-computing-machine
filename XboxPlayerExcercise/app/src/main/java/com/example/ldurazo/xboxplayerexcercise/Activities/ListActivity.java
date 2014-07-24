package com.example.ldurazo.xboxplayerexcercise.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.example.ldurazo.xboxplayerexcercise.R;
import com.example.ldurazo.xboxplayerexcercise.adapters.DataWrapper;
import com.example.ldurazo.xboxplayerexcercise.adapters.SearchAdapter;
import com.example.ldurazo.xboxplayerexcercise.models.Track;

import java.util.ArrayList;

public class ListActivity extends BaseActivity{
    private ListView listView;
    private ArrayList<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        DataWrapper dw =(DataWrapper) getIntent().getSerializableExtra("List");
        tracks = dw.getTracks();
        initUI();
    }

    @Override
    protected void initUI() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new SearchAdapter(ListActivity.this , tracks));
    }
}
