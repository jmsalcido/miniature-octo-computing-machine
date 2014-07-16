package com.example.ldurazo.instagramfeed;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;


public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            InstaObject instaObject;
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                instaObject = (InstaObject) getListView().getItemAtPosition(i);
                Intent detailActivity = new Intent(getApplicationContext(),DetailActivity.class);
                detailActivity.putExtra("LARGE_IMAGE", instaObject.getLargeImage());
                detailActivity.putExtra("CONTENT", instaObject.getDescription());
                startActivity(detailActivity);
                return true;
            }
        });
        new InstagramAsync(this).execute("A");
    }
}
