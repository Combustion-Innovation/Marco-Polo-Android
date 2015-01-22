package com.ci.hub.myapplication;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class InitActivity extends FragmentActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "jXkwG4Jbd9OZP01TLXnkMqs6F";
    private static final String TWITTER_SECRET = "xpqx0c8GSKuImdguIYAOt4oRl4i86m1OF7tshnGbvI4eT0I9Op";
    public static final String TAG = "InitActivity";

    private static final int LOG_IN = 0;
    private static final int SIGN_UP = 1;

    private Button logInButton;
    private Button signUpButton;

    private OnTouchListener buttonOTL = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            int transitionTime = 80;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(TAG, "sign up was pressed!");
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "sign up is no longer pressed!");
                transition.reverseTransition((int) (transitionTime * 2));

                respondToClick(v);
            }
            return true;
        }
    };

    private void respondToClick(View v) {
        if (v.getId() == R.id.init_log_in_button) {
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivityForResult(intent, LOG_IN);
        } else {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivityForResult(intent, SIGN_UP);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // twitter stuff
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_init);
        //setContentView(R.layout.activity_invite);

        //*
        logInButton = (Button) findViewById(R.id.init_log_in_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);

        logInButton.setOnTouchListener(buttonOTL);
        signUpButton.setOnTouchListener(buttonOTL);
        //*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == LOG_IN) {
                Log.d(TAG, "The user's id is " + data.getExtras().getInt("user_id"));
                Log.d(TAG, "username: " + data.getExtras().getString("username"));
                Log.d(TAG, "password: " + data.getExtras().getString("password"));
            } else if (requestCode == SIGN_UP) {
                Log.d(TAG, "username: " + data.getExtras().getString("username"));
                Log.d(TAG, "phone: " + data.getExtras().getString("phone"));
                Log.d(TAG, "password: " + data.getExtras().getString("password"));
            }
        } else {
            Log.d(TAG, "There was an error with the intent");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
