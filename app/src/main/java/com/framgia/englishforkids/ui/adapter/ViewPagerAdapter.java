package com.framgia.englishforkids.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.framgia.englishforkids.ui.fragment.VideosFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GIAKHANH on 12/29/2016.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<VideosFragment> mListFragment = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mListFragment.add(new VideosFragment());
        mListFragment.add(new VideosFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }
}
