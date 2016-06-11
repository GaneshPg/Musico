package com.example.dell.musico;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

    }

    private class CustomPagerAdapter extends FragmentPagerAdapter {
        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return (position == 0)? PlayerFragment.createInstance() : AllTracksFragment.createInstance();
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return 2;
        }
    }
}
