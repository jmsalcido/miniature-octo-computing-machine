package com.internship.remindersfacebookapp.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.internship.remindersfacebookapp.models.FacebookUser;

public class ReminderActivity extends Activity {
	private EditText mNameEditText;
	private Button mReminderButton;
	private DatePicker mDatePicker;
	private FacebookUser mFacebookUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reminders);
		mNameEditText = (EditText) findViewById(R.id.reminder_editName);
		mReminderButton = (Button) findViewById(R.id.add_reminder_button);
		mDatePicker = (DatePicker) findViewById(R.id.datePicker);
		Bundle extras = getIntent().getExtras();
		mFacebookUser = new FacebookUser(
				extras.getString(FacebookUser.USERNAME),
				extras.getString(FacebookUser.MAIL),
				extras.getString(FacebookUser.IMAGE));
	}
}
