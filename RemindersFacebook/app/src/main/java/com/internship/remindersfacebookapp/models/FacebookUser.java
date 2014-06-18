package com.internship.remindersfacebookapp.models;


public class FacebookUser {
	public static String USERNAME="profile_name";
	public static String MAIL="profile_mail";
	public static String IMAGE="profile_image";
	private String mName;
	private String mMail;
	private String mImage;
	private long userId;

	public FacebookUser(String userName, String userMail, String userImage) {
		mName=userName;
		mMail=userMail;
		mImage=userImage;
		userId = Long.parseLong(mImage);
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
		return userId;
	}
}
