package com.ci.marcopolo;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 3/6/15.
 */
public class UpdateSettingsTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
    public final static String TAG = "UpdateSettingsTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/updateSettings.php";

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
        communicator.gotResponse(result, SettingsActivity.SETTINGS_UPDATED);
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
