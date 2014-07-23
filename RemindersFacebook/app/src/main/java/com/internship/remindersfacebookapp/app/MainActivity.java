package com.internship.remindersfacebookapp.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends Activity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    RemindersUser mRemindersUser;
    private static final String TAG = "MainFragment";
    private LoginButton FacebookLoginButton;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    //G+ Login variables
    private UiLifecycleHelper uiHelper;
    private static final int RC_SIGN_IN = 0;
    private static final int PROFILE_PIC_SIZE = 200;
    //TODO changes here
    public static GoogleApiClient mGoogleApiClient;
    public static String mCurrentUserId;
    /*TODO ABOVE*/
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    private SignInButton GoogleLoginButton;


    public String getCurrentUserId(){
        return mRemindersUser.getUserId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleLoginButton = (SignInButton) findViewById(R.id.btn_sign_in);
        GoogleLoginButton.setOnClickListener(this);
        FacebookLoginButton = (LoginButton) findViewById(R.id.loginButton);
        FacebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        mGoogleApiClient.connect();
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
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        Log.i(TAG, "Logged in...");
        if (state.isOpened()) {
            RemindersUser.IS_FB_USER =true;
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                // callback after Graph API response with user object
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        mRemindersUser = new RemindersUser(
                                user.getName(),
                                user.getProperty("email").toString(),
                                user.getId(),
                                user.getId());
                        mCurrentUserId = mRemindersUser.getUserId();
                        Intent viewPagerIntent = new Intent(getApplicationContext(), ViewPagerActivity.class);
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

    private void resolveSignInError(){
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mSignInClicked = false;
        // Get user's information
        getProfileInformation();
    }

    private void getProfileInformation(){
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                RemindersUser.IS_FB_USER =false;
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                personPhotoUrl = personPhotoUrl.substring(0,personPhotoUrl.length() - 2)+ PROFILE_PIC_SIZE;
                String personGooglePlusId = currentPerson.getId();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                mRemindersUser =new RemindersUser(
                        personName,
                        email,
                        personPhotoUrl,
                        personGooglePlusId);
                mCurrentUserId = mRemindersUser.getUserId();
                Intent viewPagerIntent = new Intent(this, ViewPagerActivity.class);
                viewPagerIntent.putExtra(RemindersUser.USERNAME, mRemindersUser.getName());
                viewPagerIntent.putExtra(RemindersUser.MAIL, mRemindersUser.getMail());
                viewPagerIntent.putExtra(RemindersUser.IMAGE, mRemindersUser.getImage());
                viewPagerIntent.putExtra(RemindersUser.USER_ID, mRemindersUser.getUserId());
                startActivity(viewPagerIntent);
            } else {
                Toast.makeText(this, "Please try again, some trouble appeared", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }


}