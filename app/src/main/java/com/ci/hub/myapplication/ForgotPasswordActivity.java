package com.ci.hub.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

/**
 * Created by Alex on 1/23/15.
 */

/*
 use with:

 FragmentManager fm = getSupportFragmentManager();
 ForgotPasswordDialogFragment forgotPasswordDialog = new ForgotPasswordDialogFragment();
 forgotPasswordDialog.show(fm, TAG);

 */

public class ForgotPasswordActivity extends Activity {

    public static final String TAG = "ForgotPasswordDialogFragment";

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // back
            finish();
        }
    };

    private View.OnTouchListener enterOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int TRANSITION_TIME = 80;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(TRANSITION_TIME);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition((int) (TRANSITION_TIME * 2));
                resetPassword();
            }
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.forgot_password_enter_button).setOnTouchListener(enterOTL);
    }

    private void resetPassword() {
        // TODO use a task to reset the password
    }
}
