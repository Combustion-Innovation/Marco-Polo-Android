package com.ci.marcopolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.OnClickListener;

/**
 * Created by Alex on 1/18/15.
 */
public class SignUpActivity extends FragmentActivity implements Communicator, VerificationCodeCallback {
    public static final String TAG = "SignUpActivity";

    // callback codes
    public final static int SIGN_UP = 0;

    private String submittedUsername;
    private String submittedPhone;
    private String submittedPassword;

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
                Log.d(TAG, "Valid entries! Verifying phone number...");
                submittedUsername = username;
                submittedPhone = phone;
                submittedPassword = password;

                openVerificationDialog();
                sendVerificationCode();
            }
        }
    };

    private void setupDebug() {
        EditText username = ((EditText) findViewById(R.id.sign_up_username_field));
        EditText phone = ((EditText) findViewById(R.id.sign_up_phone_field));
        EditText password = ((EditText) findViewById(R.id.sign_up_password_field));

        username.setText("test.username");
        phone.setText("6502136474");
        password.setText("test.password");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.sign_up_enter_button).setOnClickListener(enterOCL);

        setupDebug();
    }

    public void sendVerificationCode() {
        SetPhoneForVerificationTask phoneTask = new SetPhoneForVerificationTask();
        HashMap<String, String> data = new HashMap<String, String>();

        phoneTask.setActivity(this);
        data.put("username", submittedUsername);
        data.put("phone", submittedPhone);

        phoneTask.execute(data);
    }

    private void openVerificationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        VerificationCodeDialogFragment verifyPhoneDialog = new VerificationCodeDialogFragment();
        verifyPhoneDialog.show(fm, TAG);
    }

    @Override
    public void gotResponse(JSONObject s, int code) {
        if (code == SIGN_UP) {
            String data;
            JSONObject form;
            try {
                data = s.getString("status");
                Log.d(TAG, s.toString(4));
                form = (JSONObject) s.get("form");

                // in case something goes wrong with the result, return to InitActivity (this shouldn't happen)
                Intent intent = getIntent();
                intent.putExtra("user_data", form.toString());
                setResult(RESULT_CANCELED, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            // https://files.slack.com/files-pri/T02G10T18-F03EVFSUU/url_documentation.txt
            if (data.equals("five")) {
                Log.d(TAG, "A parameter was missing when communicating with the server.");
            } else if (data.equals("four")) {
                Toast.makeText(this, "The code did not match the code sent to your phone.", Toast.LENGTH_LONG).show();
            } else if (data.equals("three")) {
                Toast.makeText(this, "An account already exists with this phone number.", Toast.LENGTH_LONG).show();
            } else if (data.equals("two")) {
                Toast.makeText(this, "An account already exists with this username.", Toast.LENGTH_LONG).show();
            } else if (data.equals("one")) {
                Toast.makeText(this, "Welcome to MarcoPolo!", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                intent.putExtra("user_data", form.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onVerification(String code) {
        PhoneSignupTask phoneSignupTask = new PhoneSignupTask();
        HashMap<String, String> data = new HashMap<String, String>();

        phoneSignupTask.setCommunicator(this);
        data.put("username", submittedUsername);    // entered username
        data.put("phone", submittedPhone);          // entered phone number
        data.put("f_name", "DUMMY_FIRST_NAME");     // this isn't used atm
        data.put("l_name", "DUMMY_LAST_NAME");      // same
        data.put("password", submittedPassword);    // entered password
        data.put("device", "Android");              // the type of the user's device
        data.put("code", code);                     // the verification code sent to the user's phone

        phoneSignupTask.execute(data);
    }
}
