package com.internship.remindersfacebookapp.adaptersandhelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.internship.remindersfacebookapp.models.FacebookUser;
import com.internship.remindersfacebookapp.models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper{
	private static final String TAG = "SQL";
	private static final int DATABASE_VERSION=1;
	private static String DATABASE_NAME="FacebookReminderDB";

	public SQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(TAG, db.getPath());
		String CREATE_TABLE_REMINDERS_IF_NOT_EXISTS =
				"CREATE TABLE IF NOT EXISTS reminders("+
				"id INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"content TEXT, "+
				"date TEXT, "+
				"user_id TEXT,"+
				"state INTEGER,"+
				"FOREIGN KEY(user_id) REFERENCES facebook_users(user_id))";
		db.execSQL(CREATE_TABLE_REMINDERS_IF_NOT_EXISTS);

		String CREATE_TABLE_FACEBOOKUSER_IF_NOT_EXISTS =
				"CREATE TABLE IF NOT EXISTS facebook_users("+
				"user_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"image TEXT,"+
				"name TEXT,"+
				"mail TEXT)";
		db.execSQL(CREATE_TABLE_FACEBOOKUSER_IF_NOT_EXISTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/*
	 *Init some constants
	 */
	//Table name
	private static final String TABLE_REMINDERS = "reminders";
	//Columns
	private static final String COLUMN_CONTENT = "content";
	private static final String COLUMN_DATE = "date";
	private static final String COLUMN_USER_ID = "user_id";
	private static final String COLUMN_STATE = "state";

	public void insertReminders(Reminder reminder, FacebookUser facebookUser){
		Log.d(TAG,reminder.toString());
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CONTENT, reminder.getContent());
		values.put(COLUMN_DATE,reminder.getDate());
		values.put(COLUMN_USER_ID,facebookUser.getUserId());
		values.put(COLUMN_STATE, reminder.getState());
		db.insert(TABLE_REMINDERS, null, values);
	}
	//Table name
	private static final String TABLE_FACEBOOK_USERS = "facebook_users";
	//Columns
	private static final String COLUMN_FACEBOOK_USER_ID = "user_id";
	private static final String COLUMN_IMAGE_ID= "image";
	private static final String COLUMN_NAME= "name";
	private static final String COLUMN_MAIL = "mail";

	public void insertFacebookUser(FacebookUser facebookUser){
		Log.d(TAG,facebookUser.toString());
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_FACEBOOK_USER_ID, facebookUser.getUserId());
		values.put(COLUMN_IMAGE_ID, facebookUser.getImage());
		values.put(COLUMN_NAME, facebookUser.getName());
		values.put(COLUMN_MAIL, facebookUser.getMail());

		db.insert(TABLE_FACEBOOK_USERS, null, values);
	}

	public void selectFacebookUser(FacebookUser facebookUser){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM "+TABLE_FACEBOOK_USERS+" where user_id="+facebookUser.getUserId()+";", null);
		if(c.moveToFirst()){
			do{
				Log.w(TAG, c.getString(0)+
						"/"+c.getString(1)+
						"/"+c.getString(2)+
						"/"+c.getString(3));
			}while(c.moveToNext());
		}
		c.close();
		db.close();
	}
	public List<Reminder> selectReminder(FacebookUser facebookUser){
		List<Reminder> reminderList = new ArrayList<Reminder>();
		Reminder reminder = new Reminder();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM "+TABLE_REMINDERS+" where user_id="+facebookUser.getUserId()+";", null);
		if(c.moveToFirst()){
			do{
				reminder.setContent(c.getString(1));
				reminder.setDate(c.getString(2));
				reminder.setUserId(c.getString(3));
				reminder.setState(c.getString(4));
				//Do something Here with values
				Log.w(TAG, reminder.getContent()+"/"+reminder.getDate());
				reminderList.add(reminder);
			}while(c.moveToNext());
		}else{
			reminder.setContent("Example content");
			reminder.setDate("This user has no reminders yet");
			reminder.setUserId(facebookUser.getImage());
			reminder.setState("1");
			reminderList.add(reminder);
		}
		Log.w(TAG, reminderList.get(0).getContent());
		c.close();
		db.close();
		return reminderList;
	}

	public void deleteReminder(Reminder reminder){
	}
}
