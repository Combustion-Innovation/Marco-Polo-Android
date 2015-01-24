package com.ci.hub.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/23/15.
 */
public class PhoneSignupTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public static final String TAG = "PhoneSignupTask";

    private static final String PHONE_SIGNUP_URL = "http://combustionlaboratory.com/marco/php/phoneSignup.php";

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        JSONObject result;
        try {
            result = ServerTaskUtility.sendData(PHONE_SIGNUP_URL, params[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        String data;
        try {
            data = result.getString("status");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Log.d(TAG, "THE RESULT: " + data);

        if (data.equals("five")) {
            Log.d(TAG, "A parameter was missing when communicating with the server.");
        } else if (data.equals("four")){
            Toast.makeText(activity, "The code did not match the code sent to your phone.", Toast.LENGTH_LONG).show();
        } else if (data.equals("three")) {
            Toast.makeText(activity, "An account already exists with this phone number.", Toast.LENGTH_LONG).show();
        } else if (data.equals("two")) {
            Toast.makeText(activity, "An account already exists with this username.", Toast.LENGTH_LONG).show();
        } else if (data.equals("one")) {
            Toast.makeText(activity, "Welcome to MarcoPolo!", Toast.LENGTH_SHORT).show();
        }
    }
}
