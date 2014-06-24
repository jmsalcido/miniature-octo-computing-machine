package com.internship.remindersfacebookapp.models;


public class RemindersUser {
	public static String USERNAME="profile_name";
	public static String MAIL="profile_mail";
	public static String IMAGE="profile_image";
    public static String USER_ID="user id";
    public static int IS_FB_OR_G=0;
	private String mName;
	private String mMail;
	private String mImage;
	private long mUserId;

	public RemindersUser(String userName, String userMail, String userId, String url) {
		mName=userName;
		mMail=userMail;
		mImage=url;
		mUserId = Long.parseLong(userId);
	}

	public String getImage() {
		return mImage;
	}

	public String getName() {
		return mName;
	}

	public String getMail() {
		return mMail;
	}

	public long getUserId() {
		return mUserId;
	}
}
