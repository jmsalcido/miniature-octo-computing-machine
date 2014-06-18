package com.internship.remindersfacebookapp.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internship.remindersfacebookapp.adaptersandhelpers.ReminderListAdapter;
import com.internship.remindersfacebookapp.adaptersandhelpers.SQLiteHelper;
import com.internship.remindersfacebookapp.models.FacebookUser;

public class RemindersExpiredFragment extends ListFragment {
protected static int BUNDLE_SIZE = 1;
private FacebookUser mFacebookUser;
    public static RemindersExpiredFragment newInstance() {
        RemindersExpiredFragment selfInstance = new RemindersExpiredFragment();
	    selfInstance.setArguments(new Bundle(BUNDLE_SIZE));
        return selfInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminders_fragments, container, false);
	    SQLiteHelper db = new SQLiteHelper(view.getContext());
	    Bundle extras = getActivity().getIntent().getExtras();
	    mFacebookUser = new FacebookUser(
			    extras.getString(FacebookUser.USERNAME),
			    extras.getString(FacebookUser.MAIL),
			    extras.getString(FacebookUser.IMAGE));
	    this.setListAdapter(new ReminderListAdapter(view.getContext(), db.selectReminder(mFacebookUser,0)));
        return view;
    }

}
