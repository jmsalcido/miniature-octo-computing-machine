package com.internship.at1_actionbar.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by ldurazo on 6/6/2014.
 */
public class ChildActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
    }


}
