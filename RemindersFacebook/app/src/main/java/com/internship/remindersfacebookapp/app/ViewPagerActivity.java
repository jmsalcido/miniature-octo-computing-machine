package com.internship.remindersfacebookapp.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.internship.remindersfacebookapp.adapters.FragmentPageAdapter;
import com.internship.remindersfacebookapp.adapters.SQLiteAdapter;
import com.internship.remindersfacebookapp.models.FacebookUser;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerActivity extends FragmentActivity{
    FragmentPageAdapter mPageAdapter;
	FacebookUser mFacebookUser;
    public static final String HEADER_1 = "Active reminders";
    public static final String HEADER_2 = "Expired reminders";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        List<Fragment> fragments = getFragments();
        mPageAdapter = new FragmentPageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
		Bundle extras = getIntent().getExtras();
		mFacebookUser = new FacebookUser(
				extras.getString(FacebookUser.USERNAME),
				extras.getString(FacebookUser.MAIL),
				extras.getString(FacebookUser.IMAGE));
        pager.setAdapter(mPageAdapter);
		SQLiteAdapter db = new SQLiteAdapter(getApplicationContext());
		db.insertFacebookUser(mFacebookUser);
		db.selectFacebookUser(mFacebookUser);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(Session.getActiveSession().getState() == SessionState.CLOSED){
			this.finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.add_reminder:
				Intent reminderActivity = new Intent(getApplicationContext(), AddReminderActivity.class);
				reminderActivity.putExtra(FacebookUser.USERNAME, mFacebookUser.getName());
				reminderActivity.putExtra(FacebookUser.MAIL, mFacebookUser.getMail());
				reminderActivity.putExtra(FacebookUser.IMAGE, mFacebookUser.getImage());
				startActivity(reminderActivity);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_layout, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private List<Fragment> getFragments(){
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        RemindersFragment activeReminders = new RemindersFragment();
        RemindersFragment expiredReminders = new RemindersFragment();
        fragmentList.add(ProfileFragment.newInstance());
        fragmentList.add(activeReminders.newInstance(HEADER_1));
        fragmentList.add(expiredReminders.newInstance(HEADER_2));

        return fragmentList;
    }
}
