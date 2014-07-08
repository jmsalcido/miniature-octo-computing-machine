package com.internship.remindersfacebookapp.app;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    private String mFlag = null;
    private Button mButtonAdd;
    private Button mButtonShow;
    Bundle mExtras;
    SQLiteAdapter db;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reminders);
		mContentText = (EditText) findViewById(R.id.reminder_editName);
		mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
		mExtras = getIntent().getExtras();
        mButtonShow = (Button) findViewById(R.id.button_show);
        mButtonAdd = (Button) findViewById(R.id.add_reminder_button);
		mRemindersUser = new RemindersUser(
				mExtras.getString(RemindersUser.USERNAME),
				mExtras.getString(RemindersUser.MAIL),
				mExtras.getString(RemindersUser.IMAGE),
                mExtras.getString(RemindersUser.USER_ID));
        mFlag = mExtras.getString(RemindersUser.FLAG);
        if (mFlag.equals("EDIT")){
            mButtonShow.setText("Change content");
            mButtonAdd.setText("Save changes");
        }
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
                db.deleteSelectedReminder(mExtras.getString(Reminder.CONTENT),mExtras.getString(Reminder.DATE));
                mReminder.setState(1);
                mReminder.setContent(mContentText.getText().toString());
                mReminder.setUserId(mRemindersUser.getUserId());
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

    public void ShowElements(View view){
        if(mContentText.getText().toString().trim().length()>0) {
            mContentText.setVisibility(View.INVISIBLE);
            mButtonShow.setVisibility(View.INVISIBLE);
            mButtonAdd.setVisibility(View.VISIBLE);
            mDatePicker.setVisibility(View.VISIBLE);
            mTimePicker.setVisibility(View.VISIBLE);
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            //noinspection ConstantConditions
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            Toast.makeText(this, "Now select the hour and date", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Please write the reminder content!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setAlarm(Calendar calendar, int requestCode){
        Intent mIntent = new Intent(this, ReminderBroadcastReceiver.class);
        mIntent.putExtra(Reminder.CONTENT, mReminder.getContent());
        mIntent.putExtra(Reminder.DATE, mReminder.getDate());
        mIntent.putExtra(Reminder.ID, String.valueOf(requestCode));
        mIntent.putExtra(RemindersUser.USER_ID, mRemindersUser.getUserId());
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, mIntent, 0);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mPendingIntent);
    }
}
