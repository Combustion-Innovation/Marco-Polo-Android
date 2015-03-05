package com.ci.marcopolo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 2/11/15.
 */
public class PoloWasClickedTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
    public final static String TAG = "PoloWasClickedTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/poloWasClicked.php";

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
        communicator.gotResponse(result, MainActivity.SENT_POLO);
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
