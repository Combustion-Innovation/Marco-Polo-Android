package com.ci.hub.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/24/15.
 */
public class UpdateUsernameTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {

    public static final String TAG = "UpdateUsernameTask";

    private static final String UPDATE_USERNAME_URL = "http://combustionlaboratory.com/marco/php/updateUsername.php";

    private Activity activity;

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        JSONObject result;
        try {
            result = ServerTaskUtility.sendData(UPDATE_USERNAME_URL, params[0]);
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
            Log.d(TAG, result.toString(4));
            data = result.getString("status");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Log.d(TAG, "THE RESULT: " + data);

        if (data.equals("four")){
            Log.d(TAG, "A parameter was missing when communicating with the server.");
        } else if (data.equals("three")) {
            Toast.makeText(activity, "The username has illegal characters.", Toast.LENGTH_LONG).show();
        } else if (data.equals("two")) {
            Toast.makeText(activity, "This is the same username.", Toast.LENGTH_LONG).show();
        } else if (data.equals("one")) {
            Toast.makeText(activity, "Your username has successfully been changed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
