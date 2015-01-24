package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Alex on 1/23/15.
 */
public class EditInfoActivity extends Activity {
    public static final String TAG = "EditInfoActivity";

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnTouchListener usernameOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition(transitionTime * 2);
                Intent intent = new Intent(getApplicationContext(), EditUsernameActivity.class);
                startActivity(intent);
            }
            return true;
        }
    };

    private View.OnTouchListener phoneOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition(transitionTime * 2);
                Intent intent = new Intent(getApplicationContext(), EditPhoneActivity.class);
                startActivity(intent);
            }
            return true;
        }
    };

    private View.OnTouchListener passwordOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition(transitionTime * 2);
                Intent intent = new Intent(getApplicationContext(), EditPasswordActivity.class);
                startActivity(intent);
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.edit_info_username_button).setOnTouchListener(usernameOTL);
        findViewById(R.id.edit_info_phone_button).setOnTouchListener(phoneOTL);
        findViewById(R.id.edit_info_password_button).setOnTouchListener(passwordOTL);
    }
}
