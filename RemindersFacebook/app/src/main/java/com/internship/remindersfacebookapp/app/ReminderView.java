package com.internship.remindersfacebookapp.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.internship.remindersfacebookapp.models.Reminder;

public class ReminderView extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String content;
        String date;
        setContentView(R.layout.reminder_view);
        content=getIntent().getStringExtra(Reminder.CONTENT);
        date=getIntent().getStringExtra(Reminder.DATE);
        TextView contentTextView = (TextView) findViewById(R.id.content_text_view);
        TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
        contentTextView.setText(content);
        dateTextView.setText(date);
    }
    public void onExit(View view){
        finish();
    }
}
