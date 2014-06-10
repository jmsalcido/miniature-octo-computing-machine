package com.internship.at4_dynamicui.app;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration config = getResources().getConfiguration();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Landscape_Fragment ls_fragment = new Landscape_Fragment();
            fragmentTransaction.replace(android.R.id.content, ls_fragment);
        }else{
            Portrait_Fragment pm_fragment = new Portrait_Fragment();
            fragmentTransaction.replace(android.R.id.content, pm_fragment);
        }
        fragmentTransaction.commit();
    }

}
