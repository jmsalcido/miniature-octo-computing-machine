package com.internship.remindersfacebookapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public FragmentPageAdapter(FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager);
        fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}