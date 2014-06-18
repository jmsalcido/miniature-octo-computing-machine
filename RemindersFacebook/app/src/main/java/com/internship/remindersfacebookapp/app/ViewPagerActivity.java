package com.internship.remindersfacebookapp.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.internship.remindersfacebookapp.adaptersandhelpers.SQLiteHelper;
import com.internship.remindersfacebookapp.models.FacebookUser;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerActivity extends FragmentActivity{
    pageAdapter mPageAdapter;
	FacebookUser mFacebookUser;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        List<Fragment> fragments = getFragments();
        mPageAdapter = new pageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
		Bundle extras = getIntent().getExtras();
		mFacebookUser = new FacebookUser(
				extras.getString(FacebookUser.USERNAME),
				extras.getString(FacebookUser.MAIL),
				extras.getString(FacebookUser.IMAGE));
        pager.setAdapter(mPageAdapter);
		SQLiteHelper db = new SQLiteHelper(getApplicationContext());
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
				Intent reminderActivity = new Intent(getApplicationContext(), ReminderActivity.class);
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

        fragmentList.add(ProfileFragment.newInstance());
        fragmentList.add(RemindersActiveFragment.newInstance());
        fragmentList.add(RemindersExpiredFragment.newInstance());

        return fragmentList;
    }

	public class pageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;
		public pageAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
			super(fragmentManager);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}
}
