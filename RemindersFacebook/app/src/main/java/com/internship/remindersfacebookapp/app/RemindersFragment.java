package com.internship.remindersfacebookapp.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.internship.remindersfacebookapp.adaptersandhelpers.ReminderListAdapter;
import com.internship.remindersfacebookapp.adaptersandhelpers.SQLiteHelper;
import com.internship.remindersfacebookapp.models.FacebookUser;

public class RemindersFragment extends ListFragment {
protected static int BUNDLE_SIZE = 1;
private String header;
private FacebookUser mFacebookUser;
    public static RemindersFragment newInstance(String message) {
        RemindersFragment selfInstance = new RemindersFragment();
	    selfInstance.setArguments(new Bundle(BUNDLE_SIZE));
	    selfInstance.header = message;
        return selfInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminders_fragments, container, false);
	    TextView remindersHeader = (TextView) view.findViewById(R.id.profile_name);
	    SQLiteHelper db = new SQLiteHelper(getActivity().getApplicationContext());
	    Bundle extras = getActivity().getIntent().getExtras();
	    mFacebookUser = new FacebookUser(
			    extras.getString(FacebookUser.USERNAME),
			    extras.getString(FacebookUser.MAIL),
			    extras.getString(FacebookUser.IMAGE));
	    remindersHeader.setText(header);
	    this.setListAdapter(new ReminderListAdapter(view.getContext(), db.selectReminder(mFacebookUser)));
        return view;
    }

}
