package com.internship.remindersfacebookapp.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.internship.remindersfacebookapp.models.RemindersUser;
import com.internship.remindersfacebookapp.models.Reminder;

import java.util.ArrayList;
import java.util.List;

public class SQLiteAdapter extends SQLiteOpenHelper{
    //Constant and variables declaration
	private static final String TAG = "SQL";
	private static final int DATABASE_VERSION=1;
    //Table for users name
    private static final String TABLE_REMINDERS_USERS = "reminders_users";
    //Table for users columns
    private static final String COLUMN_REMINDER_USER_ID = "user_id";
    private static final String COLUMN_IMAGE_ID= "image";
    private static final String COLUMN_NAME= "name";
    private static final String COLUMN_MAIL = "mail";
    private static final String CREATE_TABLE_REMINDER_USER_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS "+TABLE_REMINDERS_USERS+" ("+
                    COLUMN_REMINDER_USER_ID +" TEXT PRIMARY KEY,"+
                    COLUMN_IMAGE_ID+" TEXT,"+
                    COLUMN_NAME+" TEXT,"+
                    COLUMN_MAIL+" TEXT)";
    //Table for reminders name
    private static final String TABLE_REMINDERS = "reminders";
    //Table for reminders columns
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_USER_ID = "user_id";
    //state 0 is for inactive and 1 is for inactive.
    private static final String COLUMN_STATE = "state";

    //Querys for table creation
    private static final String CREATE_TABLE_REMINDERS_IF_NOT_EXISTS =
            "CREATE TABLE IF NOT EXISTS "+TABLE_REMINDERS+" ("+
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_CONTENT+" TEXT, "+
                    COLUMN_DATE+" TEXT, "+
                    COLUMN_USER_ID+" TEXT,"+
                    COLUMN_STATE+ " INTEGER,"+
                    "FOREIGN KEY("+COLUMN_USER_ID+") REFERENCES "+TABLE_REMINDERS_USERS+"("+ COLUMN_REMINDER_USER_ID +"))";
	private static String DATABASE_NAME="ReminderDB";

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
        db.execSQL(CREATE_TABLE_REMINDER_USER_IF_NOT_EXISTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Please Ringo implement something here.
	}


	public void insertReminders(Reminder reminder, RemindersUser remindersUser){
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_CONTENT, reminder.getContent());
		values.put(COLUMN_DATE,reminder.getDate());
		values.put(COLUMN_USER_ID, remindersUser.getUserId());
		values.put(COLUMN_STATE, reminder.getState());
		db.insert(TABLE_REMINDERS, null, values);
	}


	public void insertUser(RemindersUser remindersUser){
		SQLiteDatabase db = this.getWritableDatabase();
        Log.w(TAG, remindersUser.getUserId()+"/"+remindersUser.getImage()+"/"+remindersUser.getName()+"/"+remindersUser.getMail());
		ContentValues values = new ContentValues();
		values.put(COLUMN_REMINDER_USER_ID, remindersUser.getUserId());
		values.put(COLUMN_IMAGE_ID, remindersUser.getImage());
		values.put(COLUMN_NAME, remindersUser.getName());
		values.put(COLUMN_MAIL, remindersUser.getMail());

		db.insert(TABLE_REMINDERS_USERS, null, values);
	}


	public void selectUser(RemindersUser remindersUser){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM "
                + TABLE_REMINDERS_USERS
                +" where "+ COLUMN_REMINDER_USER_ID +"="+ remindersUser.getUserId()
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


	public List<Reminder> selectReminder(RemindersUser remindersUser, int state){
		String activeRemindersQuery = "SELECT * FROM "
                +TABLE_REMINDERS
                +" where "+ COLUMN_REMINDER_USER_ID +"='"+ remindersUser.getUserId()
                +"' AND "+COLUMN_STATE+"=1;";
        String expiredRemindersQuery = "SELECT * FROM "
                +TABLE_REMINDERS
                +" where "+ COLUMN_REMINDER_USER_ID +"='"+ remindersUser.getUserId()
                +"' AND "+COLUMN_STATE+"=0;";

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
        String selectQuery = "SELECT "+COLUMN_ID+" FROM "
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

    public boolean isReminderExisting(String searchedID){
        String selectQuery = "SELECT * FROM "
                +TABLE_REMINDERS+" WHERE "+COLUMN_ID+"="+searchedID+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        return c.moveToFirst();
    }

    public String getUserIdForNotificationReminder(String searchUser, String searchedId){
        String selectQuery = "SELECT * FROM "
                +TABLE_REMINDERS+" "
                +"WHERE "+COLUMN_USER_ID+"='"+searchUser+"' AND "+COLUMN_ID+"="+searchedId+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery,null);
        c.moveToFirst();
        return c.getString(3);
    }

    public void updateStateToInactive(String searchedId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "UPDATE "+TABLE_REMINDERS+" SET "+COLUMN_STATE+"=0 WHERE "+COLUMN_ID+"="+searchedId;
        db.execSQL(query);
    }

    public void deleteSelectedReminder(String content, String date){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "DELETE FROM REMINDERS WHERE "+COLUMN_CONTENT+"='"+content+"' AND "+COLUMN_DATE+"='"+date+"'";
        db.execSQL(query);

    }
}
