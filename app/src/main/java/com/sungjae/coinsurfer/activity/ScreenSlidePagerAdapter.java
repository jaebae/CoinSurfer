package com.sungjae.coinsurfer.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sungjae.coinsurfer.activity.fragment.BalanceInfoFragment;
import com.sungjae.coinsurfer.activity.fragment.SettingFragment;
import com.sungjae.coinsurfer.activity.fragment.TradeHistoryFragment;

import java.util.ArrayList;

class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> mFragmentList = new ArrayList<>();

    public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        mFragmentList.add(new BalanceInfoFragment());
        mFragmentList.add(new TradeHistoryFragment());
        mFragmentList.add(new SettingFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
