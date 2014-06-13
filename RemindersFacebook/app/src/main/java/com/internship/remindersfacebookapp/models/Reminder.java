package com.internship.remindersfacebookapp.models;

public class Reminder {
	private String mContent;
	private String mDate;
	private String mUserId;
	private String mState;

	public Reminder(){

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

	public String getState() {
		return mState;
	}

	public void setState(String state) {
		mState=state;
	}
}
