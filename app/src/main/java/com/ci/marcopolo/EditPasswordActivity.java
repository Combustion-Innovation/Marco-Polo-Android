package com.ci.marcopolo;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class EditPasswordActivity extends Activity {
    public static final String TAG = "EditPasswordActivity";

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
                String old_password = ((EditText) findViewById(R.id.edit_password_old_password_field)).getText().toString();
                String new_password = ((EditText) findViewById(R.id.edit_password_new_password_field)).getText().toString();
                String confirm_password = ((EditText) findViewById(R.id.edit_password_confirm_password_field)).getText().toString();

                if (new_password.equals(confirm_password)) {
                    changePassword(old_password, new_password);
                } else {
                    Toast.makeText(EditPasswordActivity.this, "The new password and confirm password fields must match.", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        try {
            Log.d(TAG, getIntent().getExtras().getString("user_data"));
            JSONObject user_data = new JSONObject(getIntent().getExtras().getString("user_data"));
            phone_number = user_data.getString("phone_number");
            user_id = user_data.getString("user_id");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.edit_password_enter_button).setOnTouchListener(enterOTL);
    }

    private void changePassword(String old_pass, String new_pass) {
        AsyncTask<Void, Void, JSONObject> changePasswordTask;
        final String CHANGE_PASSWORD_URL = "http://combustionlaboratory.com/marco/php/changePassword.php";
        final HashMap<String, String> data = new HashMap<String, String>();

        data.put("user_id", user_id);
        data.put("oldpassword", old_pass);
        data.put("newpassword", new_pass);

        changePasswordTask = new AsyncTask<Void, Void, JSONObject>() {
            private static final String TAG = "ChangePasswordTask";

            @Override
            protected JSONObject doInBackground(Void... params) {
                JSONObject result;
                try {
                    result = ServerTaskUtility.sendData(CHANGE_PASSWORD_URL, data);
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

                if (status.equals("two")) {
                    Log.d(TAG, "A required parameter was incorrect.");
                } else if (status.equals("one")) {
                    Toast.makeText(EditPasswordActivity.this, "Your password has been changed!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        changePasswordTask.execute();
    }
}
