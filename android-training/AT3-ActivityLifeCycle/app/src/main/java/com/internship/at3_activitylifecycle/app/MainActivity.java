package com.internship.at3_activitylifecycle.app;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {
    int timesCreated=0;
    int timesStarted=0;
    int timesResumed=0;
    int timesPaused=0;
    int timesStopped=0;
    int timesRestarted=0;
    int timesDestroyed=0;
    TextView mTimesCreated;
    TextView mTimesStarted;
    TextView mTimesResumed;
    TextView mTimesPaused;
    TextView mTimesStopped;
    TextView mTimesRestarted;
    TextView mTimesDestroyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimesCreated =(TextView) findViewById(R.id.created);
        timesCreated++;
        mTimesCreated.setText("Times created: "+ timesCreated);
        mTimesStarted =(TextView) findViewById(R.id.started);
        mTimesResumed =(TextView) findViewById(R.id.resumed);
        mTimesPaused =(TextView) findViewById(R.id.paused);
        mTimesStopped =(TextView) findViewById(R.id.stopped);
        mTimesRestarted = (TextView) findViewById(R.id.restarted);
        mTimesDestroyed = (TextView) findViewById(R.id.destroyed);
   }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timesCreated = savedInstanceState.getInt("timesCreated");
        timesStarted = savedInstanceState.getInt("timesStarted");
        timesResumed = savedInstanceState.getInt("timesResumed");
        timesPaused = savedInstanceState.getInt("timesPaused");
        timesStopped = savedInstanceState.getInt("timesStopped");
        timesRestarted = savedInstanceState.getInt("timesRestarted");
        timesDestroyed = savedInstanceState.getInt("timesDestroyed");
    }

    @Override
    protected void onStart() {
        super.onStart();
        timesStarted++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        timesResumed++;
        mTimesResumed.setText("Times Resumed: " + timesResumed);
        mTimesPaused.setText("Times Paused: "+ timesPaused);
        mTimesStarted.setText("Times Started: "+ timesStarted);
        mTimesStopped.setText("Times stopped: "+ timesStopped);
        mTimesRestarted.setText("Times restarted: "+ timesRestarted);
        mTimesDestroyed.setText("Times destroyed: "+ timesDestroyed);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timesPaused++;
    }

    @Override
    protected void onStop() {
        super.onStop();

        timesStopped++;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        timesRestarted++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timesDestroyed++;
        android.os.Debug.stopMethodTracing();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt("timesCreated",timesCreated);
        outState.putInt("timesStarted",timesStarted);
        outState.putInt("timesResumed",timesResumed);
        outState.putInt("timesPaused",timesPaused);
        outState.putInt("timesStopped",timesStopped);
        outState.putInt("timesRestarted",timesRestarted);
        outState.putInt("timesDestroyed",timesDestroyed);
        super.onSaveInstanceState(outState);

    }
}
