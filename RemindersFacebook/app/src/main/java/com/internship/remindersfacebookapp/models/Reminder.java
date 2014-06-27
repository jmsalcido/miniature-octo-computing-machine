package com.internship.remindersfacebookapp.models;

public class Reminder {
    public static final String CONTENT = "CONTENT";
    public static final String DATE = "DATE";
    public static final String USER_ID = "USER_ID";
    public static final String ID = "ID";
	private String mContent;
	private String mDate;
	private String mUserId;
    private int mAlarmRequestCode;
    private int mState;

    public int getAlarmRequestCode() {
        return mAlarmRequestCode;
    }

    public void setAlarmRequestCode(int alarmRequestCode) {
        mAlarmRequestCode = alarmRequestCode;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent=content;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		mDate=date;
	}

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String userId) {
		mUserId=userId;
	}

}
