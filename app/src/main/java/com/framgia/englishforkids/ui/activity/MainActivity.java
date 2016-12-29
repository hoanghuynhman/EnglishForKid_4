package com.framgia.englishforkids.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.framgia.englishforkids.R;
import com.framgia.englishforkids.ui.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity
    implements ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener,
    Toolbar.OnMenuItemClickListener {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private TabLayout.Tab mTabSong, mTabStory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupTabLayout();
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tb_top);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //creating adapter and setting that adapter to the viewPager
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabSong = mTabLayout.newTab();
        mTabStory = mTabLayout.newTab();
        mTabSong.setIcon(R.drawable.ic_song);
        mTabStory.setIcon(R.drawable.ic_story);
        mTabLayout.addTab(mTabSong, 0);
        mTabLayout.addTab(mTabStory, 1);
        mTabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.color_white));
        mTabLayout
            .setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.color_yellow_dark));
        //view pager setting
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_change_style) {
            changeStyle();
        }
        return false;
    }

    private void changeStyle() {
        mAdapter.changeViewMode();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mTabSong.setIcon(R.drawable.ic_song_selected);
                mTabStory.setIcon(R.drawable.ic_story);
                break;
            case 1:
                mTabStory.setIcon(R.drawable.ic_story_selected);
                mTabSong.setIcon(R.drawable.ic_song);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
