package com.ci.generalclasses.loginmanagers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Alex on 12/3/14.
 * LoginManager is a login utility class that makes it easy to reuse login code.
 * It allows a proprietary login. a facebook login, and a twitter login.
 */
public class LoginManager {
    private static final String TAG = "LoginManager";
    private static String proprietaryLoginURL = "http://httpbin.org/post";
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static String TWITTER_KEY;
    private static String TWITTER_SECRET;

    /**
     * Before using this API: https://github.com/Combustion-Innovation/general-classes/wiki/Facebook-Login-Requirements
     * API info: https://github.com/Combustion-Innovation/general-classes/wiki/Facebook-Login-API
     * 
     * @return
     */
    public static FBFragment getFBLogin() {
        return new FBFragment();
    }

    /**
     * https://github.com/Combustion-Innovation/general-classes/wiki/Proprietary-Login-requirements
     * https://github.com/Combustion-Innovation/general-classes/wiki/Proprietary-Login-API
     *
     * @param activity
     * @return
     */
    public static ProprietaryFragment initProprietaryLogin(final Activity activity) {
        final ProprietaryFragment proprietaryFragment = new ProprietaryFragment();
        proprietaryFragment.setActivity(activity);
        proprietaryFragment.setLoginURL(proprietaryLoginURL);
        Button button = (Button) activity.findViewById(R.id.proprietary_login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container, (Fragment) proprietaryFragment);
                //ft.setTransition(transition);
                ft.addToBackStack(null);
                ft.commit();
                Log.d(TAG, "Proprietary login!");
            }
        });
        return proprietaryFragment;
    }

    public static void proprietaryLogin(Activity activity, Map<String, String> data) {
        ProprietaryLoginTask loginTask = new ProprietaryLoginTask();
        loginTask.setActivity(activity);
        data.put("_url", proprietaryLoginURL);
        loginTask.execute(data);
    }

    /**
     * https://github.com/Combustion-Innovation/general-classes/wiki/Twitter-Login-requirements
     * https://github.com/Combustion-Innovation/general-classes/wiki/Facebook-Login-API
     *
     * @param key consumer key
     * @param secret secret key
     */
    public static void setTwitterKeys(String key, String secret) {
        TWITTER_KEY = key;
        TWITTER_SECRET = secret;
    }

    public static TwitterLoginButton initTwitterLogin(final Activity activity, Callback<TwitterSession> callback) {
        TwitterLoginButton twitterLoginButton = (TwitterLoginButton) activity.findViewById(R.id.twitter_login_button);
        final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        Fabric.with(activity, new Twitter(authConfig));
        twitterLoginButton.setCallback(callback);

        return twitterLoginButton;
    }
}
