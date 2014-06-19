package com.internship.remindersfacebookapp.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.internship.remindersfacebookapp.adapters.SQLiteAdapter;
import com.internship.remindersfacebookapp.models.FacebookUser;
import com.internship.remindersfacebookapp.models.Reminder;

import java.util.Calendar;

public class AddReminderActivity extends Activity {
	private EditText mContentText;
	private DatePicker mDatePicker;
	private FacebookUser mFacebookUser;
    private Reminder mReminder = new Reminder();
    private TimePicker mTimePicker;
    SQLiteAdapter db;
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
        db = new SQLiteAdapter(getApplicationContext());
	}

    public void AddReminder(View view){
        Calendar currentTime = Calendar.getInstance();
        int day=mDatePicker.getDayOfMonth();
        int month=mDatePicker.getMonth();
        int year=mDatePicker.getYear();
        int hour=mTimePicker.getCurrentHour();
        int minute=mTimePicker.getCurrentMinute();

        Calendar reminderTime = Calendar.getInstance();
        reminderTime.set(year,month,day);
        reminderTime.set(Calendar.HOUR_OF_DAY, hour);
        reminderTime.set(Calendar.MINUTE, minute);

        Log.w("test", currentTime.getTime().toString());
        Log.w("test", reminderTime.getTime().toString());

        mReminder.setContent(mContentText.getText().toString());
        mReminder.setUserId(String.valueOf(mFacebookUser.getUserId()));
        mReminder.setState(1);
        String fullDate = String.valueOf(day)
                +"/"+String.valueOf(month)
                +"/"+String.valueOf(year)
                +" - "+String.valueOf(hour)
                +":"+String.valueOf(minute);
        mReminder.setDate(fullDate);
        db.insertReminders(mReminder,mFacebookUser);
        finish();
    }
}
