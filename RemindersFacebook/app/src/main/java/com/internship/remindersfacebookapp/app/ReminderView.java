package com.internship.remindersfacebookapp.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.internship.remindersfacebookapp.models.Reminder;

public class ReminderView extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String content;
        String date;
        setContentView(R.layout.activity_reminder_view);
        content=getIntent().getStringExtra(Reminder.CONTENT);
        date=getIntent().getStringExtra(Reminder.DATE);
        TextView contentTextView = (TextView) findViewById(R.id.content_text_view);
        TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
        contentTextView.setText(content);
        dateTextView.setText(date);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reminder_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
