package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.OnClickListener;
import static android.view.View.OnTouchListener;

/**
 * Created by Alex on 1/18/15.
 */
public class LogInActivity extends Activity {
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

    public void returnData(JSONObject result) {
        Intent intent = getIntent();
        try {
            intent.putExtra("login_data", result.getString("user_id"));
            intent.putExtra("username", result.getString("username"));
            intent.putExtra("f_name", result.getString("f_name"));
            intent.putExtra("l_name", result.getString("l_name"));
            intent.putExtra("email", result.getString("email"));
            intent.putExtra("phone_number", result.getString("phone_number"));
            intent.putExtra("first_login", result.getString("first_login"));
            intent.putExtra("device", result.getString("device"));
            intent.putExtra("is_logged_in", result.getString("is_logged_in"));
            intent.putExtra("picture", result.getString("picture"));
            intent.putExtra("push_notification", result.getString("push_notification"));
            intent.putExtra("unit", result.getString("unit"));
            intent.putExtra("push_key", result.getString("push_key"));

            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private String getUsername() {
        return ((EditText) findViewById(R.id.log_in_username_field)).getText().toString();
    }

    private String getPassword() {
        return ((EditText) findViewById(R.id.log_in_password_field)).getText().toString();
    }
}
