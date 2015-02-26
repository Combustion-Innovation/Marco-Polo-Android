package com.ci.marcopolo;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 2/18/15.
 */
public class BlockUserTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {
    public final static String TAG = "BlockUserTask";

    private final static String URL = "http://combustionlaboratory.com/marco/php/blockUser.php";

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

        try {
            Log.d(TAG, result.toString(4));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        communicator.gotResponse(result, MainActivity.BLOCK_USER);
    }

    public void setCommunicator(Communicator communicator) {
        this.communicator = communicator;
    }
}
