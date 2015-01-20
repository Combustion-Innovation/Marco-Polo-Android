package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ci.generalclasses.loginmanagers.Communicator;
import com.ci.generalclasses.loginmanagers.LoginManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.OnClickListener;

/**
 * Created by Alex on 1/18/15.
 */
public class SignUpActivity extends Activity implements Communicator {
    public static final String TAG = "SignUpActivity";

    private final OnClickListener backOCL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };

    private final OnClickListener enterOCL = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String username = ((EditText) findViewById(R.id.sign_up_username_field)).getText().toString();
            String phone = ((EditText) findViewById(R.id.sign_up_phone_field)).getText().toString();
            String password = ((EditText) findViewById(R.id.sign_up_password_field)).getText().toString();

            if (Validator.validateUsername(username) != Validator.OK
                    || Validator.validatePhoneNumber(phone) != Validator.OK
                    || Validator.validatePassword(password) != Validator.OK) {
                Log.d(TAG, "Invalid entries!");
                Toast.makeText(SignUpActivity.this, "An entry is invalid", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Valid entries! Starting sign up process...");
                signUp(username, phone, password);
            }
        }
    };

    private void setupDebug() {
        EditText username = ((EditText) findViewById(R.id.sign_up_username_field));
        EditText phone = ((EditText) findViewById(R.id.sign_up_phone_field));
        EditText password = ((EditText) findViewById(R.id.sign_up_password_field));

        username.setText("my username");
        phone.setText("(702) 696-9696");
        password.setText("my password");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.sign_up_enter_button).setOnClickListener(enterOCL);

        setupDebug();

        Log.d(TAG, "This is working!");
    }

    private void signUp(String username, String phone, String password) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("username", username);
        data.put("phone", phone);
        data.put("password", password);

        LoginManager.proprietaryLogin(this, data);
    }

    @Override
    public void gotResponse(JSONObject s) {
        String username, phone, password;
        Intent intent = getIntent();
        try {
            Log.d(TAG, s.toString(4));
            JSONObject form = (JSONObject) s.get("form");
            username = form.getString("username");
            phone = form.getString("phone");
            password = form.getString("password");
        } catch (Exception e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED, intent);
            finish();
            return;
        }
        intent.putExtra("username", username);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }
}
