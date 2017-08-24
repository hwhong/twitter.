package com.example.hwhong.twitter.Profile;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.hwhong.twitter.LogIn.AppSingleton;
import com.example.hwhong.twitter.MainActivity;
import com.example.hwhong.twitter.R;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.ImageLoader;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TweetComposition extends AppCompatActivity {

    private static final String PROFILE_IMAGE = "PROFILE_IMAGE_URL";
    private static final String TAG = "TWEETCOMPOSTION";

    @BindView(R.id.close_button)        ImageView close;
    @BindView(R.id.compose_tweet_dp)    CircleImageView dp;
    @BindView(R.id.content)             EditText contents;
    @BindView(R.id.tweet_button)        Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_composition);
        ButterKnife.bind(this);

        postTweet();
        // setting the dp picture on right top
        setProfilePic(getIntent().getStringExtra(PROFILE_IMAGE).replace("_normal", ""));
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

    @OnClick(R.id.tweet_button)
    public void postTweet() {

        if(!TextUtils.isEmpty(contents.getText().toString())) {
            final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                    .getActiveSession();
            final Intent intent = new ComposerActivity.Builder(TweetComposition.this)
                    .session(session)
                    .text(contents.getText().toString())
                    .hashtags("#twitter")
                    .createIntent();
            startActivity(intent);
        }
    }

    @OnClick(R.id.close_button)
    public void end() {
        finish();
    }
}
