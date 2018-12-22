package com.example.ij351.travelmaker;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public MainPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AFragment.newInstance(0, "Page # 1");
            case 1:
                return BFragment.newInstance(1, "Page # 2");
            case 2:
                return CFragment.newInstance(2, "Page # 3");
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page "+position;
    }
}
