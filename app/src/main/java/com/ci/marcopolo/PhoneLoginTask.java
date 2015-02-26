package com.ci.marcopolo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/23/15.
 */
public class PhoneLoginTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public static final String TAG = "PhoneLoginTask";

    private static final String URL = "http://combustionlaboratory.com/marco/php/phoneLogin.php";

    private Communicator communicator;

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        JSONObject result;
        try {
            result = ServerTaskUtility.sendData(URL, params[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        try {
            Log.d(TAG, result.toString(4));
            Log.d(TAG, "THE RESULT: " + result.getString("status"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        communicator.gotResponse(result, LogInActivity.LOGIN);

    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
