package com.example.hwhong.twitter;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
    @BindView(R.id.toolbar)                 Toolbar toolbar;
    @BindView(R.id.drawer_layout)           DrawerLayout drawerLayout;

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
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initViewPagerAndTabs();
        initTabIcons();
        initHamburgMenu();
    }

    private void initHamburgMenu() {
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_hamburger);

        /*
        toolbar.setNavigationIcon(R.drawable.ic_hamburger);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_hamburger); //set your own

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        */
        mDrawerToggle.syncState();
    }

    //------------Three necessary methods to implement for navigation drawer to work -----
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //------------------------------------------------------------------------------------

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
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.twitter_blue));
    }

    // dynamically changing the icons on tab switches
    private void initTabIcons() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(selected_icons[tab.getPosition()]);
                //getSupportActionBar().setTitle(pageTitles[tab.getPosition()]);
                viewPager.setCurrentItem(tab.getPosition());
                getSupportActionBar().setTitle(pageTitles[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(unselected_icons[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
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
