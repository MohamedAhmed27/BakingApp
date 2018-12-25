package com.example.mohamed.bakingapp.fragmentAdapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 8/16/2018.
 */

public class SectionPageAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList=new ArrayList<>();
    private final List<String> mFragmentTtitleList=new ArrayList<>();
    public SectionPageAdapter(FragmentManager fm)
    {
        super(fm);
    }
    public void addFragment(Fragment fragment,String title)
    {
        mFragmentList.add(fragment);
        mFragmentTtitleList.add(title);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);

    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {


        return mFragmentTtitleList.get(position);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
