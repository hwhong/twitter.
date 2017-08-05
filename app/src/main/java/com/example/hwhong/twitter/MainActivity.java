package com.example.hwhong.twitter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.hwhong.twitter.Search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // View bindings
    @BindView(R.id.viewPager)               ViewPager viewPager;
    @BindView(R.id.tabLayout)               TabLayout tabLayout;

    // Data elements
    private int[] unselected_icons = new int[]{
                                    R.drawable.ic_house,
                                    R.drawable.ic_search,
                                    R.drawable.ic_bell,
                                    R.drawable.ic_message
    };

    private int[] selected_icons = new int[] {
                                    R.drawable.ic_blue_house,
                                    R.drawable.ic_blue_search,
                                    R.drawable.ic_blue_bell,
                                    R.drawable.ic_blue_message
    };

    private String[] pageTitles = new String[] {"Home", "Search", "Notifications", "Messages"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initViewPagerAndTabs();
        initTabIcons();
    }

    private void initViewPagerAndTabs() {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(SearchFragment.createInstance(10), "Home");
        pagerAdapter.addFragment(SearchFragment.createInstance(10), "Search");
        pagerAdapter.addFragment(SearchFragment.createInstance(10), "Notifications");
        pagerAdapter.addFragment(SearchFragment.createInstance(10), "Messages");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        // So that icons are populated upon starting
        tabLayout.getTabAt(0).setIcon(selected_icons[0]);
        for(int i = 1; i < unselected_icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(unselected_icons[i]);
        }
    }

    // dynamically changing the icons on tab switches
    private void initTabIcons() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(selected_icons[tab.getPosition()]);
                //getSupportActionBar().setTitle(pageTitles[tab.getPosition()]);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(unselected_icons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        // so that only icons are returned
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
