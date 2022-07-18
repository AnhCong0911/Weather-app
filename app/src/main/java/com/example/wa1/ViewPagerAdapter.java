package com.example.wa1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<CityName> mCityList;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<CityName> list) {
        super(fm, behavior);
        this.mCityList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mCityList == null || mCityList.isEmpty()) {
            return null;
        }
        CityName sentData = mCityList.get(position);
        WeatherFragment newDataFragment = new WeatherFragment();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("data_key", sentData);
        newDataFragment.setArguments(mBundle);
        return newDataFragment;
    }

    @Override
    public int getCount() {
        if (mCityList != null) {
            return mCityList.size();
        }
        return 0;
    }
}
