package com.example.hwhong.twitter.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.hwhong.twitter.MainActivity;
import com.example.hwhong.twitter.R;
import com.mopub.volley.VolleyError;
import com.mopub.volley.toolbox.ImageLoader;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;
import com.twitter.sdk.android.core.services.StatusesService;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TwitterLogin" ;
    //Twitter Login Button
    TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create a new TwitterAuthConfig Object
        // or else gives error "Must initialize Twitter before using getInstance()"
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(
                        getResources().getString(R.string.KEY),
                        getResources().getString(R.string.SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        setContentView(R.layout.activity_login);

        //Initialize twitter login button
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLoginButton);
        //Add callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Adding the login result back to the button
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    //The login function accepting the result object
    public void login(Result<TwitterSession> result) {
        //Creating a twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String userName = session.getUserName();
        //userNameTextView = (TextView)findViewById(R.id.userTextView);
        //userImageView = (ImageView) findViewById(R.id.userImageView);
        //userNameTextView.setText("@" + userName);

        //TwitterSession   session = result.data;
        //Twitter          twitter = Twitter.getInstance();
        //TwitterApiClient api     = twitter.core.getApiClient(session);
        //AccountService service = api.getAccountService();
        //Call<User>       user    = service.verifyCredentials(true, true, true);

        //Getting the account service of the user logged in
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        //StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
        call.enqueue(new Callback<User>() {
            @Override
            public void failure(TwitterException e) {
                Log.d("TwitterKit", "Verify Credentials Failure");
            }
            @Override
            public void success(Result<User> userResult) {
                //If it succeeds creating a User object from userResult.data
                User user = userResult.data;
                //setProfilePic(user.profileImageUrl.replace("_normal", ""));
                twitterLoginButton.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

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
                    //userImageView.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

}
