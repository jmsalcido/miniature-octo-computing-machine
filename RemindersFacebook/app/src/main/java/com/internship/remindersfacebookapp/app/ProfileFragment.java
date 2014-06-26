package com.internship.remindersfacebookapp.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.internship.remindersfacebookapp.models.RemindersUser;


public class ProfileFragment extends Fragment {
protected static int BUNDLE_SIZE = 1;

    public static final ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        Bundle bdl = new Bundle(BUNDLE_SIZE);
        profileFragment.setArguments(bdl);
        return profileFragment;
    }

	@Override
	public void onResume() {
		super.onResume();
		if (Session.getActiveSession().isClosed()) {
			getActivity().finish();
		}
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_information, container, false);
		Bundle extras = getActivity().getIntent().getExtras();
		RemindersUser remindersUser = new RemindersUser(
				extras.getString(RemindersUser.USERNAME),
				extras.getString(RemindersUser.MAIL),
				extras.getString(RemindersUser.IMAGE),
                extras.getString(RemindersUser.USER_ID));

		ProfilePictureView mProfilePicture = (ProfilePictureView) view.findViewById(R.id.profile_picture);
		TextView mUserMail = (TextView) view.findViewById(R.id.profile_mail);
		TextView mUserName = (TextView) view.findViewById(R.id.profile_name);

		 mProfilePicture.setProfileId(remindersUser.getImage());
		 mUserName.setText(remindersUser.getName());
		 mUserMail.setText(remindersUser.getMail());

		Button mButtonLogout = (Button) view.findViewById(R.id.logoutButton);
	    mButtonLogout.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    if (!Session.getActiveSession().isClosed()) {
				    Session.getActiveSession().closeAndClearTokenInformation();
				    onResume();
			    }
		    }
	    });
	    return view;
    }
}