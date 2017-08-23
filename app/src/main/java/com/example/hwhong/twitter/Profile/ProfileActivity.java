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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_image)   CircleImageView dp;
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
