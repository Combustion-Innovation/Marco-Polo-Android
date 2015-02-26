package com.ci.hub.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 2/18/15.
 */
public class UnblockUserTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
    public final static String TAG = "UnblockUserTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/unblockUser.php";

    private Communicator communicator;

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
        communicator.gotResponse(result, MainActivity.UNBLOCK_USER);
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
