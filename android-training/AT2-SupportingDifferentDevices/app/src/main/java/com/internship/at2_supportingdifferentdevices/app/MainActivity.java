package com.internship.at2_supportingdifferentdevices.app;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpActionBar();
    }

    private void setUpActionBar(){
            ActionBar actionBar= getActionBar();
            actionBar.setTitle("API level of device: "+Build.VERSION_CODES.KITKAT);
    }
}
