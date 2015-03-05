package com.ci.facebookmanager;

import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

/**
 * Created by Alex on 2/7/15.
 */
public interface FBCallback {
    public void onSuccess(GraphUser a, Response b);
}
