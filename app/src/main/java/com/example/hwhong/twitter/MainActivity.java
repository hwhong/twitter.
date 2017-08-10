package com.example.hwhong.twitter;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hwhong.twitter.Home.HomeFragment;
import com.example.hwhong.twitter.LogIn.AppSingleton;
import com.example.hwhong.twitter.Messages.MessagesFragment;
import com.example.hwhong.twitter.Notifications.NotificationsFragment;
import com.example.hwhong.twitter.Profile.ProfileActivity;
import com.example.hwhong.twitter.Search.SearchFragment;
import com.example.hwhong.twitter.Utils.CustomLayout;
import com.example.hwhong.twitter.Utils.PagerAdapter;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.ImageLoader;
import com.percolate.caffeine.ToastUtils;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.models.Tweet;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    // Intent strings
    private static final String PROFILE_IMAGE = "PROFILE_IMAGE_URL";
    private static final String NAME = "NAME";
    private static final String HANDLE = "HANDLE";
    private static final String BACKGROUND = "BACKGROUND";
    private static final String TAG = "main";

    // View bindings
    @BindView(R.id.viewPager)               ViewPager viewPager;
    @BindView(R.id.tabLayout)               TabLayout tabLayout;
    @BindView(R.id.toolbar)                 Toolbar toolbar;
    @BindView(R.id.drawer_layout)           DrawerLayout drawerLayout;
    @BindView(R.id.nav_drawer)              NavigationView navigationView;

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
    private CircleImageView dp;

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
        initNavigationDrawerItems();
        initNavDrawerIntents();
    }

    private void initNavDrawerIntents() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        navigationView.getMenu().getItem(0).setChecked(false);
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    default:
                        ToastUtils.quickToast(getApplicationContext(), "Have to implement");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void initNavigationDrawerItems() {
        //View v = navigationView.inflateHeaderView(R.layout.nav_header_main);
        // above code generates a duplicate
        View v = navigationView.getHeaderView(0);
        // might need to fix this part for butterknife
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView handle = (TextView) v.findViewById(R.id.handle);
        dp = (CircleImageView) v.findViewById(R.id.profile_image);
        CustomLayout nav_header = (CustomLayout) v.findViewById(R.id.nav_linear_layout);

        name.setText(getIntent().getStringExtra(NAME));
        handle.setText("@" + getIntent().getStringExtra(HANDLE));
        setProfilePic(getIntent().getStringExtra(PROFILE_IMAGE).replace("_normal", ""));
        Picasso.with(this).load(Uri.parse(getIntent().getStringExtra(BACKGROUND))).into(nav_header);
    }

    private void initHamburgMenu() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

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
        pagerAdapter.addFragment(new HomeFragment());
        pagerAdapter.addFragment(new SearchFragment());
        pagerAdapter.addFragment(new NotificationsFragment());
        pagerAdapter.addFragment(new MessagesFragment());

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

    // for image loading, from singleton class
    private void setProfilePic(String url){
        ImageLoader imageLoader = AppSingleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    dp.setImageBitmap(response.getBitmap());
                }
            }
        });
    }
}
