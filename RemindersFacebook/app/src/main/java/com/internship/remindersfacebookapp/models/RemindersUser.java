package com.internship.remindersfacebookapp.models;


public class RemindersUser {
    public static boolean IS_FB_USER;
	public static String USERNAME="profile_name";
	public static String MAIL="profile_mail";
	public static String IMAGE="profile_image";
    public static String USER_ID="user_id";
    public static final String FLAG = "FLAG";
	private String mName;
	private String mMail;
	private String mImage;
	private String mUserId;

	public RemindersUser(String userName, String userMail, String userImage, String userId) {
		mName=userName;
		mMail=userMail;
		mImage=userImage;
		mUserId = userId;
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

	public String getUserId() {
		return mUserId;
	}
}
