package com.ci.hub.myapplication;

import org.json.JSONObject;

/**
 * Created by Alex on 1/26/15.
 */
public interface Communicator {
    public void gotResponse(JSONObject r, int code);
}
