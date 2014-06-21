package com.internship.remindersfacebookapp.app;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.internship.remindersfacebookapp.models.RemindersUser;

import java.util.Arrays;

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
	RemindersUser mRemindersUser;
	private static final String TAG = "MainFragment";
    private UiLifecycleHelper uiHelper;
    private LoginButton mLoginButton;
    //google variables
    private SignInButton mSignInButton;
    private static final int PROFILE_PIC_SIZE = 400;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    /*
    The session status callback calls the onSessionState method, which handles
    the state change.
     */
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /*
        Lifecycle method start
        */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);
		mLoginButton = (LoginButton) view.findViewById(R.id.loginButton);
		mLoginButton.setFragment(this);
		mLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        mSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
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
    public void onStop() {
        super.onStop();
        uiHelper.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
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
    /*
    End of lifecycle methods with the uihelper
     */

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Log.i(TAG, "Logged in...");
		if (state.isOpened()) {
            mLoginButton.setVisibility(View.INVISIBLE);
			Request.newMeRequest(session, new Request.GraphUserCallback() {
				// callback after Graph API response with user object
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
                        RemindersUser.IS_FB_OR_G=0;
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
            mLoginButton.setVisibility(View.VISIBLE);
			Log.i(TAG, "Logged out...");
		}
	}

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        // Get user's information
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                RemindersUser.IS_FB_OR_G=1;
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String userID=currentPerson.getId();
                String imageURL=currentPerson.getImage().getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                mRemindersUser =new RemindersUser(
                        personName,
                        email,
                        userID,
                        imageURL);
                Intent viewPagerIntent = new Intent(getActivity().getApplicationContext(), ViewPagerActivity.class);
                viewPagerIntent.putExtra(RemindersUser.USERNAME, mRemindersUser.getName());
                viewPagerIntent.putExtra(RemindersUser.MAIL, mRemindersUser.getMail());
                viewPagerIntent.putExtra(RemindersUser.IMAGE, mRemindersUser.getImage());
                viewPagerIntent.putExtra(RemindersUser.USER_ID, mRemindersUser.getUserId());
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    //TODO
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), getActivity(),0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onClick(View view) {
        signInWithGplus();
    }

    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }
}