package com.ci.marcopolo;

import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/23/15.
 */
public class EditPhoneActivity extends FragmentActivity implements VerificationCodeCallback {
    public static final String TAG = "EditPhoneActivity";

    private String new_phone_number;
    private String phone_number;
    private String user_id;

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnTouchListener enterOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition(transitionTime * 2);
                EditText phoneField = (EditText) findViewById(R.id.edit_phone_phone_field);
                new_phone_number = phoneField.getText().toString();
                openVerificationDialog();
                sendVerificationCode();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone);

        try {
            Log.d(TAG, getIntent().getExtras().getString("user_data"));
            JSONObject user_data = new JSONObject(getIntent().getExtras().getString("user_data"));
            phone_number = user_data.getString("phone_number");
            user_id = user_data.getString("user_id");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // set the phone field to the user's phone number
        EditText phoneField = (EditText) findViewById(R.id.edit_phone_phone_field);
        phoneField.setText(phone_number);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.edit_phone_enter_button).setOnTouchListener(enterOTL);
    }

    private void openVerificationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        VerificationCodeDialogFragment verificationCodeDialog = new VerificationCodeDialogFragment();
        verificationCodeDialog.show(fm, TAG);
    }

    private void sendVerificationCode() {
        AsyncTask<Void, Void, JSONObject> userPhoneChangeTask;
        final String URL = "http://combustionlaboratory.com/marco/php/sendVerificationNewNumber.php";
        final HashMap<String, String> data = new HashMap<String, String>();

        data.put("user_id", "50");
        data.put("phone", "6502136475");

        userPhoneChangeTask = new AsyncTask<Void, Void, JSONObject>() {
            private static final String TAG = "SendVerificationNewNumberTask";

            @Override
            protected JSONObject doInBackground(Void... params) {
                JSONObject result;
                try {
                    result = ServerTaskUtility.sendData(URL, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);

                String status;
                try {
                    Log.d(TAG, result.toString(4));
                    status = result.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                Log.d(TAG, "THE RESULT: " + status);


                if (status.equals("five")) {
                    Log.d(TAG, "A required parameter was missing.");
                } else if (status.equals("three")) {
                    Toast.makeText(EditPhoneActivity.this, "The code does not match the one sent to your phone.", Toast.LENGTH_LONG).show();
                } else if (status.equals("two")) {
                    Toast.makeText(EditPhoneActivity.this, "This phone number already exists.", Toast.LENGTH_LONG).show();
                } else if (status.equals("one")) {
                    Toast.makeText(EditPhoneActivity.this, "A confirmation code has been sent to the number.", Toast.LENGTH_LONG).show();
                }
            }
        };
        userPhoneChangeTask.execute();
    }

    @Override
    public void onVerification(String code) {
        AsyncTask<Void, Void, JSONObject> sendVerificationNewNumberTask;
        final String URL = "http://combustionlaboratory.com/marco/php/userPhoneChange.php";
        final HashMap<String, String> data = new HashMap<String, String>();

        data.put("user_id", user_id);
        data.put("phone", new_phone_number);
        data.put("code", code);

        sendVerificationNewNumberTask = new AsyncTask<Void, Void, JSONObject>() {
            private static final String TAG = "UserPhoneChangeTask";

            @Override
            protected JSONObject doInBackground(Void... params) {
                JSONObject result;
                try {
                    result = ServerTaskUtility.sendData(URL, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);

                String status;
                try {
                    Log.d(TAG, result.toString(4));
                    status = result.getString("status");
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                Log.d(TAG, "THE RESULT: " + status);

                if (status.equals("four")) {
                    Log.d(TAG, "A required parameter was missing.");
                } else if (status.equals("two")) {
                    Toast.makeText(EditPhoneActivity.this, "This phone number already exists.", Toast.LENGTH_LONG).show();
                } else if (status.equals("one")) {
                    Toast.makeText(EditPhoneActivity.this, "Your number has successfully changed.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        sendVerificationNewNumberTask.execute();
    }
}
