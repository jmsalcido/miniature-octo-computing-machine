package com.internship.remindersfacebookapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.internship.remindersfacebookapp.models.RemindersUser;

import java.util.Arrays;

public class LoginFragment extends Fragment {
	RemindersUser mRemindersUser;
	private static final String TAG = "MainFragment";
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private UiLifecycleHelper uiHelper;
//some odd
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);
		LoginButton loginButton = (LoginButton) view.findViewById(R.id.loginButton);
		loginButton.setFragment(this);
		loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null &&
				(session.isOpened() || session.isClosed()) ) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Session.getActiveSession().closeAndClearTokenInformation();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Log.i(TAG, "Logged in...");
		if (state.isOpened()) {
			Request.newMeRequest(session, new Request.GraphUserCallback() {
				// callback after Graph API response with user object
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
						mRemindersUser =new RemindersUser(
								user.getName(),
								user.getProperty("email").toString(),
								user.getId(),
                                user.getId());
						Intent viewPagerIntent = new Intent(getActivity().getApplicationContext(), ViewPagerActivity.class);
						viewPagerIntent.putExtra(RemindersUser.USERNAME, mRemindersUser.getName());
						viewPagerIntent.putExtra(RemindersUser.MAIL, mRemindersUser.getMail());
						viewPagerIntent.putExtra(RemindersUser.IMAGE, mRemindersUser.getImage());
                        viewPagerIntent.putExtra(RemindersUser.USER_ID, mRemindersUser.getUserId());
						startActivity(viewPagerIntent);
					}
				}
			}).executeAsync();

		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}
}