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
public class PhoneLoginTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public static final String TAG = "PhoneLoginTask";

    private static final String PHONE_LOGIN_URL = "http://combustionlaboratory.com/marco/php/phoneLogin.php";

    private Activity activity;

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        JSONObject result;
        try {
            result = ServerTaskUtility.sendData(PHONE_LOGIN_URL, params[0]);
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
            Toast.makeText(activity, "The username / password combination is incorrect.", Toast.LENGTH_LONG).show();
        } else if (status.equals("one")) {
            Toast.makeText(activity, "Logged in!", Toast.LENGTH_SHORT).show();
            ((Communicator)activity).gotResponse(result);
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
