package com.example.hwhong.twitter.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hwhong.twitter.LogIn.AppSingleton;
import com.example.hwhong.twitter.R;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.ImageLoader;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    // Intent strings
    private static final String PROFILE_IMAGE = "PROFILE_IMAGE_URL";
    private static final String NAME = "NAME";
    private static final String HANDLE = "HANDLE";
    private static final String FOLLOWERS = "FOLLOWERS";
    private static final String FOLLOWINGS = "FOLLOWINGS";
    private static final String BIO = "BIO";

    @BindView(R.id.profile_page_dp) CircleImageView dp;
    @BindView(R.id.profile_name)    TextView name;
    @BindView(R.id.profile_handle)  TextView handle;
    @BindView(R.id.profile_bio)     TextView bio;
    @BindView(R.id.followers)       TextView followers;
    @BindView(R.id.following)       TextView following;
    @BindView(R.id.profile_listview)ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initProfileItems();
    }


    private void initProfileItems() {
        name.setText(getIntent().getStringExtra(NAME));
        handle.setText(getIntent().getStringExtra(HANDLE));
        bio.setText(getIntent().getStringExtra(BIO));
        followers.setText(getIntent().getIntExtra(FOLLOWERS, 0) + " Followers");
        following.setText(getIntent().getIntExtra(FOLLOWINGS, 0) + " Following");

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("hwhong1129")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getApplicationContext())
                .setTimeline(userTimeline)
                .build();
        listView.setAdapter(adapter);
    }

    // for image loading, from singleton class
    private void setProfilePic(String url){
        ImageLoader imageLoader = AppSingleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Image Load Error: " + error.getMessage());
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
