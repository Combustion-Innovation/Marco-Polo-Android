package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ci.generalclasses.loginmanagers.Communicator;

import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.OnClickListener;
import static android.view.View.OnTouchListener;

/**
 * Created by Alex on 1/18/15.
 */
public class LogInActivity extends Activity implements Communicator {
    public static final String TAG = "LogInActivity";

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

    private OnClickListener forgotPasswordOCL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.log_in_enter_button).setOnTouchListener(enterOTL);
        findViewById(R.id.forgot_password).setOnClickListener(forgotPasswordOCL);
    }

    private void logIn() {
        // log in
        PhoneLoginTask phoneLoginTask = new PhoneLoginTask();
        HashMap<String, String> data = new HashMap<String, String>();

        phoneLoginTask.setActivity(this);
        data.put("username", getUsername());
        data.put("password", getPassword());

        phoneLoginTask.execute(data);
    }

    private String getUsername() {
        return ((EditText) findViewById(R.id.log_in_username_field)).getText().toString();
    }

    private String getPassword() {
        return ((EditText) findViewById(R.id.log_in_password_field)).getText().toString();
    }

    @Override
    public void gotResponse(JSONObject result) {
        Intent intent = getIntent();
        intent.putExtra("user_data", result.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
