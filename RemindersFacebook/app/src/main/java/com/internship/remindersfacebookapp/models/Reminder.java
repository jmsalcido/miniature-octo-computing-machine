package com.internship.remindersfacebookapp.models;

public class Reminder {
	private String mContent;
	private String mDate;
	private String mUserId;
	private int mState;

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
