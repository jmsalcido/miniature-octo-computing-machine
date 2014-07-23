package com.internship.remindersfacebookapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.plus.Plus;
import com.internship.remindersfacebookapp.adapters.LoadProfileImage;
import com.internship.remindersfacebookapp.models.RemindersUser;


public class ProfileFragment extends Fragment {
protected static int BUNDLE_SIZE = 1;
private ImageView mImageView;
    @Override
    public void onStop() {
        super.onStop();
        mImageView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageView = (ImageView) getActivity().findViewById(R.id.imageView);
    }

    public static final ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bdl = new Bundle(BUNDLE_SIZE);
        profileFragment.setArguments(bdl);
        return profileFragment;
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		MainActivity.mGoogleApiClient.connect();
        View view = inflater.inflate(R.layout.profile_information, container, false);
		Bundle extras = getActivity().getIntent().getExtras();
		RemindersUser remindersUser = new RemindersUser(
				extras.getString(RemindersUser.USERNAME),
				extras.getString(RemindersUser.MAIL),
				extras.getString(RemindersUser.IMAGE),
                extras.getString(RemindersUser.USER_ID));

        if(RemindersUser.IS_FB_USER){
            ProfilePictureView mProfilePicture = (ProfilePictureView) view.findViewById(R.id.profile_picture);
            mProfilePicture.setProfileId(remindersUser.getImage());
        }else{
            mImageView = (ImageView) view.findViewById(R.id.imageView);
            new LoadProfileImage(mImageView).execute(remindersUser.getImage());
        }

		TextView mUserMail = (TextView) view.findViewById(R.id.profile_mail);
		TextView mUserName = (TextView) view.findViewById(R.id.profile_name);

		 mUserName.setText(remindersUser.getName());
		 mUserMail.setText(remindersUser.getMail());

		Button mButtonLogout = (Button) view.findViewById(R.id.logoutButton);
	    mButtonLogout.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
                if(RemindersUser.IS_FB_USER){
                    if (!Session.getActiveSession().isClosed()) {
                        Session.getActiveSession().closeAndClearTokenInformation();
                        getActivity().finish();
                    }
                }else{
                    if (MainActivity.mGoogleApiClient.isConnected()) {
                        Plus.AccountApi.clearDefaultAccount(MainActivity.mGoogleApiClient);
                        MainActivity.mGoogleApiClient.disconnect();
                        getActivity().finish();
                    }
                }
		    }
	    });
	    return view;
    }
}