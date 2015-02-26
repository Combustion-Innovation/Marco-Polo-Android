package com.ci.marcopolo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 2/11/15.
 */
public class MarcoWasClickedTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
    public final static String TAG = "MarcoWasClickedTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/marcoWasClicked.php";

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
        //communicator.gotResponse(result);

        String data;
        try {
            data = result.getString("status");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Log.d(TAG, "THE RESULT: " + data);

        if (data.equals("two")) {
            Log.d(TAG, "A parameter was incorrect.");
        } else if (data.equals("one")) {
            Log.d(TAG, "Marco was sent!");
        }
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
