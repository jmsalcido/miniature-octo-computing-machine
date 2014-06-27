package com.internship.remindersfacebookapp.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.internship.remindersfacebookapp.adapters.ReminderBroadcastReceiver;
import com.internship.remindersfacebookapp.adapters.SQLiteAdapter;
import com.internship.remindersfacebookapp.models.RemindersUser;
import com.internship.remindersfacebookapp.models.Reminder;

import java.util.Calendar;

public class AddReminderActivity extends Activity {
	private EditText mContentText;
	private DatePicker mDatePicker;
	private RemindersUser mRemindersUser;
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
		mRemindersUser = new RemindersUser(
				extras.getString(RemindersUser.USERNAME),
				extras.getString(RemindersUser.MAIL),
				extras.getString(RemindersUser.IMAGE),
                extras.getString(RemindersUser.USER_ID));
        db = new SQLiteAdapter(getApplicationContext());
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void AddReminder(View view){
        //Cannot add expired date reminders or empty body reminders
        if(mContentText.getText().toString().trim().length()>0) {
            Calendar currentTime = Calendar.getInstance();
            int day=mDatePicker.getDayOfMonth();
            int month=mDatePicker.getMonth();
            int year=mDatePicker.getYear();
            int hour=mTimePicker.getCurrentHour();
            int minute=mTimePicker.getCurrentMinute();

            Calendar reminderTime = Calendar.getInstance();
            //noinspection ResourceType
            reminderTime.set(year,month,day);
            reminderTime.set(Calendar.HOUR_OF_DAY, hour);
            reminderTime.set(Calendar.MINUTE, minute);
            reminderTime.set(Calendar.SECOND, 0);

            if(currentTime.getTimeInMillis()>=reminderTime.getTimeInMillis()){
                Toast.makeText(this, "Please select a future date", Toast.LENGTH_SHORT).show();
            }else{
                mReminder.setState(1);
                mReminder.setContent(mContentText.getText().toString());
                mReminder.setUserId(String.valueOf(mRemindersUser.getUserId()));
                mReminder.setDate(reminderTime.getTime().toString());
                mReminder.setAlarmRequestCode(db.selectLastReminderId()+1);
                db.insertReminders(mReminder, mRemindersUser);
                setAlarm(reminderTime, db.selectLastReminderId());
                finish();
            }
        }else{
            Toast.makeText(this, "Please write the reminder content!", Toast.LENGTH_SHORT).show();
        }

    }

    public void setAlarm(Calendar calendar, int requestCode){
        Intent mIntent = new Intent(this, ReminderBroadcastReceiver.class);
        mIntent.putExtra(Reminder.CONTENT, mReminder.getContent());
        mIntent.putExtra(Reminder.DATE, mReminder.getDate());
        mIntent.putExtra(Reminder.ID, String.valueOf(requestCode));
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, mIntent, 0);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPendingIntent);
    }
}
