package com.ci.marcopolo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/26/15.
 */
public class ForgotPWTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public static final String TAG = "ForgotPWTask";

    private static final String URL = "http://combustionlaboratory.com/marco/php/forgotPW.php";

    private Activity activity;

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        JSONObject result;
        try {
            result = ServerTaskUtility.sendData(URL, params[0]);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        String status;
        try {
            status = result.getString("status");
            Log.d(TAG, result.toString(4));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
        if (status.equals("one")) {
            Toast.makeText(activity, "Your new password has been sent to your phone.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
