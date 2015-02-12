package com.ci.hub.myapplication;

import android.app.Activity;
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

import org.json.JSONObject;


public class InitActivity extends FragmentActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "jXkwG4Jbd9OZP01TLXnkMqs6F";
    private static final String TWITTER_SECRET = "xpqx0c8GSKuImdguIYAOt4oRl4i86m1OF7tshnGbvI4eT0I9Op";
    public static final String TAG = "InitActivity";

    private static final int LOG_IN = 0;
    private static final int SIGN_UP = 1;

    private OnTouchListener buttonOTL = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            int transitionTime = 80;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
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
        setContentView(R.layout.activity_init);

        // FOR DEBUGGING ONLY
        String user_data;
        try {
            JSONObject user_data_json = new JSONObject();
            user_data_json.put("user_id", "50");
            user_data_json.put("username", "alex.heritier");
            user_data_json.put("f_name", "");
            user_data_json.put("l_name", "");
            user_data_json.put("email", "");
            user_data_json.put("phone_number", "6502136474");
            user_data_json.put("first_login", "2015-01-14 22:48:19");
            user_data_json.put("device", "iPhone");
            user_data_json.put("is_logged_in", "");
            user_data_json.put("picture", "");
            user_data_json.put("push_notification", "1");
            user_data_json.put("unit", "1");
            user_data_json.put("push_key", "yes");
            user_data = user_data_json.toString();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
            return;
        }

        //*
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("user_data", user_data);
        startActivity(intent);
        //*/

        //*
        Button logInButton = (Button) findViewById(R.id.init_log_in_button);
        Button signUpButton = (Button) findViewById(R.id.sign_up_button);

        logInButton.setOnTouchListener(buttonOTL);
        signUpButton.setOnTouchListener(buttonOTL);
        //*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == LOG_IN) {
                String user_data = data.getExtras().getString("user_data");
                Log.d(TAG, user_data);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_data", user_data);
                startActivity(intent);
            } else if (requestCode == SIGN_UP) {
                String user_data = data.getExtras().getString("user_data");
                Log.d(TAG, user_data);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_data", user_data);
                startActivity(intent);
            }
        } else {
            Log.d(TAG, "There was an error with the intent.");
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
