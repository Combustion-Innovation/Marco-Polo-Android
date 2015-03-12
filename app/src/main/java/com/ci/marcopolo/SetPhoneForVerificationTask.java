package com.ci.marcopolo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/21/15.
 */
public class SetPhoneForVerificationTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public static final String TAG = "PhoneVerificationTask";

    private static final String URL = "http://combustionlaboratory.com/marco/php/setPhoneForVerification.php";

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
        communicator.gotResponse(result, SignUpActivity.VERIFY_PHONE);
    }
}
