package com.learntodroid.materialdatepicker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PickerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new PickerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        setSupportActionBar(findViewById(R.id.toolbar));
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i=0; i < adapter.getCount(); i++) {
            tabLayout.getTabAt(i).setText(adapter.getTitle(i));
        }
    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 1;
        Fragment datePickerFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            datePickerFragment = new DatePickerFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                default:
                    return datePickerFragment;
            }
        }

        int getTitle(int position) {
            switch(position) {
                case 0:
                default:
                    return R.string.tab_title_date;
            }
        }
    }
}
