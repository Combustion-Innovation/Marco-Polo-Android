package com.ci.hub.contactmanager;

import org.json.JSONObject;

/**
 * Created by Alex on 1/7/15.
 */
public interface CICallback {
    public void onStart();
    public void onProgress();
    public void onEnd(JSONObject resultData);
}
