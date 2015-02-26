package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alex on 1/23/15.
 */
public class SettingsActivity extends Activity {
    public static final String TAG = "SettingsActivity";

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private String user_data;

    private View.OnTouchListener editInfoOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition((int) (transitionTime * 2));
                Intent intent = new Intent(getApplicationContext(), EditInfoActivity.class);
                intent.putExtra("user_data", user_data);
                startActivity(intent);
            }
            return true;
        }
    };

    private View.OnTouchListener logoutOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition((int) (transitionTime * 2));
                logout();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user_data = getIntent().getExtras().getString("user_data");

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.settings_edit_info_button).setOnTouchListener(editInfoOTL);
        findViewById(R.id.settings_logout_button).setOnTouchListener(logoutOTL);
    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), InitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}