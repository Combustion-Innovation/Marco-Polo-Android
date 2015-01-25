package com.ci.hub.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.ci.generalclasses.loginmanagers.Communicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/24/15.
 */
public class GetListOfMarcoPolosTask extends AsyncTask<HashMap<String, String>, String, JSONObject> {
    public final static String TAG = "GetListOfMarcoPolosTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/getListOfMarcoPolos.php?";

    private Activity activity;

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
            Log.d(TAG, result.toString(4));
            results = result.getJSONArray("results").getJSONObject(0);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // TODO do something with the polos, marcos, and friends
        ((Communicator) activity).gotResponse(results);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
