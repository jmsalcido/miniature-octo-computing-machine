package com.internship.remindersfacebookapp.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internship.remindersfacebookapp.adaptersandhelpers.ReminderListAdapter;
import com.internship.remindersfacebookapp.adaptersandhelpers.SQLiteHelper;
import com.internship.remindersfacebookapp.models.FacebookUser;

public class RemindersActiveFragment extends ListFragment {
protected static int BUNDLE_SIZE = 1;
    public static RemindersActiveFragment newInstance() {
        RemindersActiveFragment selfInstance = new RemindersActiveFragment();
	    selfInstance.setArguments(new Bundle(BUNDLE_SIZE));
        return selfInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminders_fragments, container, false);
	    SQLiteHelper db = new SQLiteHelper(view.getContext());
	    Bundle extras = getActivity().getIntent().getExtras();
	    FacebookUser facebookUser = new FacebookUser(
			    extras.getString(FacebookUser.USERNAME),
			    extras.getString(FacebookUser.MAIL),
			    extras.getString(FacebookUser.IMAGE));
	    this.setListAdapter(new ReminderListAdapter(view.getContext(), db.selectReminder(facebookUser,1)));
        return view;
    }
}