package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.OnClickListener;
import static android.view.View.OnTouchListener;

/**
 * Created by Alex on 1/18/15.
 */
public class LogInActivity extends Activity {
    public static final String TAG = "LogInActivity";

    private EditText usernameField;
    private EditText passwordField;

    private OnClickListener backOCL = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };

    private OnTouchListener enterOTL = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            int transitionTime = 80;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition((int) (transitionTime * 2));
                logIn();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        usernameField = (EditText) findViewById(R.id.log_in_username_field);
        passwordField = (EditText) findViewById(R.id.log_in_password_field);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.log_in_enter_button).setOnTouchListener(enterOTL);
    }

    private void logIn() {
        // log in
        Intent intent = getIntent();
        intent.putExtra("user_id", 100);
        intent.putExtra("username", usernameField.getText().toString());
        intent.putExtra("password", passwordField.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
