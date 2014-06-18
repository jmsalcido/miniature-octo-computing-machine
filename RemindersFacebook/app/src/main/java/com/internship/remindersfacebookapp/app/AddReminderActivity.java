package com.internship.remindersfacebookapp.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.internship.remindersfacebookapp.adapters.SQLiteHelper;
import com.internship.remindersfacebookapp.models.FacebookUser;
import com.internship.remindersfacebookapp.models.Reminder;

import java.sql.Time;

public class AddReminderActivity extends Activity {
	private EditText mContentText;
	private DatePicker mDatePicker;
	private FacebookUser mFacebookUser;
    private Reminder mReminder = new Reminder();
    private TimePicker mTimePicker;
    SQLiteHelper db;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reminders);
		mContentText = (EditText) findViewById(R.id.reminder_editName);
		mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
		Bundle extras = getIntent().getExtras();
		mFacebookUser = new FacebookUser(
				extras.getString(FacebookUser.USERNAME),
				extras.getString(FacebookUser.MAIL),
				extras.getString(FacebookUser.IMAGE));
        db = new SQLiteHelper(getApplicationContext());
	}

    public void AddReminder(View view){
        mReminder.setContent(mContentText.getText().toString());
        mReminder.setUserId(String.valueOf(mFacebookUser.getUserId()));
        mReminder.setState(1);
        String fullDate = String.valueOf(mDatePicker.getDayOfMonth())
                +"/"+String.valueOf(mDatePicker.getMonth())
                +"/"+String.valueOf(mDatePicker.getMonth())
                +" - "+String.valueOf(mTimePicker.getCurrentHour())
                +":"+String.valueOf(mTimePicker.getCurrentHour());
        mReminder.setDate(fullDate);
        db.insertReminders(mReminder,mFacebookUser);
        finish();
    }
}
