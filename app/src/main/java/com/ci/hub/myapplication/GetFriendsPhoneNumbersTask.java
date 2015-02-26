package com.ci.hub.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 2/25/15.
 */
public class GetFriendsPhoneNumbersTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
    public final static String TAG = "GetFriendsPhone#sTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/getFriendsPhoneNumbers.php";

    private Communicator communicator;

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        JSONObject result = null;
        try {
            result = ServerTaskUtility.sendData(URL, params[0]);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        communicator.gotResponse(result, ShareActivity.ADD_CONTACTS);
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
