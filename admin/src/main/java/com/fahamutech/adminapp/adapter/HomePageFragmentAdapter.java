package com.fahamutech.adminapp.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fahamutech.adminapp.fragment.HomeArticlesFragment;
import com.fahamutech.adminapp.fragment.HomeTestimonyFragment;

public class HomePageFragmentAdapter extends FragmentPagerAdapter {

    public HomePageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeArticlesFragment();
        } else if (position == 1) {
            return new HomeTestimonyFragment();
        } else return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Category";
        } else if (position == 1) {
            return "Testimony";
        }else return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
