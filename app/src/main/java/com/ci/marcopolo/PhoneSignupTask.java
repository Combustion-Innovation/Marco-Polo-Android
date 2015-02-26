package com.ci.marcopolo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/23/15.
 */
public class PhoneSignupTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public static final String TAG = "PhoneSignupTask";

    private static final String URL = "http://combustionlaboratory.com/marco/php/phoneSignup.php";

    private Communicator communicator;

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }

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
            String data = result.getString("status");
            Log.d(TAG, "THE RESULT: " + data);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        communicator.gotResponse(result, SignUpActivity.SIGN_UP);
    }
}
