package com.ci.hub.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/24/15.
 */
public class GetListOfMarcoPolosTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public final static String TAG = "GetListOfMarcoPolosTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/getListOfMarcoPolos.php?";

    private Communicator communicator;

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... params) {
        HashMap<String, String> data = params[0];
        final String FULL_URL = URL + "user_id=" + data.get("user_id")
                + "&lat=" + data.get("lat")
                + "&lng=" + data.get("lng");
        JSONObject result;
        try {
            result = ServerTaskUtility.sendData(FULL_URL, data);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        JSONObject results;
        try {
            results = result.getJSONArray("results").getJSONObject(0);
            Log.d(TAG, results.toString(4));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
        communicator.gotResponse(results, MainActivity.GET_MARCO_POLO_DATA);
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
