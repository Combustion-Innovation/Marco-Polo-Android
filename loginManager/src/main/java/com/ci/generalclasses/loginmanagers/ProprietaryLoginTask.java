package com.ci.generalclasses.loginmanagers;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 12/20/14.
 */
public class ProprietaryLoginTask extends AsyncTask<Map<String, String>, String, String> {
    private Activity activity;
    private final String TAG = "ProprietaryLoginTask";
    @Override
    protected String doInBackground(Map<String, String>... params) {
        HashMap<String, String> data = (HashMap) params[0];
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(data.get("_url"));

        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Log.d(TAG, (String) pair.getKey() + ": " + (String) pair.getValue());
            if (pair.getKey() != "_url") {
                pairs.add(new BasicNameValuePair((String) pair.getKey(), (String) pair.getValue()));
            }
        }
        HttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs));
            response = client.execute(post);
            result = EntityUtils.toString(response.getEntity());
        } catch(Exception e) {
            e.printStackTrace();
        }

        return response != null ? result : "Failed";
    }

    protected void onProgressUpdate(String... progress) {
        for (int i = 0; i < progress.length; i++) {
            Log.d(TAG, progress[i]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Log.d("MyAsyncTask", result);
        try {
            ((Communicator) activity).gotResponse(new JSONObject(result));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
