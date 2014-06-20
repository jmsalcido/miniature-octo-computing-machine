package com.internship.remindersfacebookapp.adapters;

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

public class SQLiteAdapter extends SQLiteOpenHelper{
    //Constant and variables declaration
	private static final String TAG = "SQL";
	private static final int DATABASE_VERSION=1;
	private static String DATABASE_NAME="FacebookReminderDB";

    //Table for users name
    private static final String TABLE_FACEBOOK_USERS = "facebook_users";
    //Table for users columns
    private static final String COLUMN_FACEBOOK_USER_ID = "user_id";
    private static final String COLUMN_IMAGE_ID= "image";
    private static final String COLUMN_NAME= "name";
    private static final String COLUMN_MAIL = "mail";

    //Table for reminders name
    private static final String TABLE_REMINDERS = "reminders";
    //Table for reminders columns
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_USER_ID = "user_id";
    //state 0 is for inactive and 1 is for inactive.
    private static final String COLUMN_STATE = "state";

    //Strings for demo header
    private static final String EXAMPLE_CONTENT = "Example content";
    private static final String EXAMPLE_DATE = "26 June 2014";

    //Querys for table creation
    private static final String CREATE_TABLE_REMINDERS_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS reminders("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "content TEXT, "+
                    "date TEXT, "+
                    "user_id TEXT,"+
                    "state INTEGER,"+
                    "FOREIGN KEY(user_id) REFERENCES facebook_users(user_id))";

    private static final String CREATE_TABLE_FACEBOOK_USER_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS facebook_users("+
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "image TEXT,"+
                    "name TEXT,"+
                    "mail TEXT)";

    /*
     * End of variable and constants declaration.
     */
    public SQLiteAdapter(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.w(TAG, db.getPath());
		db.execSQL(CREATE_TABLE_REMINDERS_IF_NOT_EXISTS);
        db.execSQL(CREATE_TABLE_FACEBOOK_USER_IF_NOT_EXISTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Please Ringo implement something here.
	}


	public void insertReminders(Reminder reminder, FacebookUser facebookUser){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CONTENT, reminder.getContent());
		values.put(COLUMN_DATE,reminder.getDate());
		values.put(COLUMN_USER_ID,facebookUser.getUserId());
		values.put(COLUMN_STATE, reminder.getState());
		db.insert(TABLE_REMINDERS, null, values);
	}


	public void insertFacebookUser(FacebookUser facebookUser){
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
		Cursor c = db.rawQuery("SELECT * FROM "
                +TABLE_FACEBOOK_USERS
                +" where user_id="+facebookUser.getUserId()
                +";"
                ,null);
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


	public List<Reminder> selectReminder(FacebookUser facebookUser, int state){
		String activeRemindersQuery = "SELECT * FROM "
                +TABLE_REMINDERS
                +" where user_id="+facebookUser.getUserId()
                +" AND state=1;";
        String expiredRemindersQuery = "SELECT * FROM "
                +TABLE_REMINDERS
                +" where user_id="+facebookUser.getUserId()
                +" AND state=0;";

        List<Reminder> reminderList = new ArrayList<Reminder>();
		SQLiteDatabase db = this.getReadableDatabase();

        Cursor c;
        if (state==1){
            c = db.rawQuery(activeRemindersQuery, null);
        }else{
            c = db.rawQuery(expiredRemindersQuery, null);
        }

		if(c.moveToFirst()){
			do{
                Reminder reminder = new Reminder();
				reminder.setContent(c.getString(1));
				reminder.setDate(c.getString(2));
				reminder.setUserId(c.getString(3));
				reminder.setState(Integer.parseInt(c.getString(4)));
				//Do something Here with values
				Log.w(TAG, reminder.getContent()+"/"+reminder.getDate());
				reminderList.add(reminder);
			}while(c.moveToNext());
		}
		c.close();
		db.close();
		return reminderList;
	}

    public void deleteAllReminders(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM REMINDERS");
    }

    public int selectLastReminderId(){
        String selectQuery = "SELECT id FROM "
                +TABLE_REMINDERS+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        int requestCode=0;
        if(c.moveToFirst()) {
            do {
                requestCode=Integer.parseInt(c.getString(0));
            } while (c.moveToNext());
        }
        return requestCode;
    }

    public void updateStateToInactive(String searchedId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE reminders SET state=0 WHERE id="+searchedId;
        db.execSQL(query);
    }
}
