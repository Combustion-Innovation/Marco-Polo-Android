package com.ci.facebookmanager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

/**
 * Created by Alex on 3/4/15.
 */
public class FBUtility {
    public final static String TAG = "FBUtility";

    private Activity activity;
    private boolean sentData = false;

    // onAttach tasks
    private boolean onAttachLogin = false;
    private FBCallback loginCallback;

    private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    public FBUtility(Activity activity) {
        this.activity = activity;
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            //Log.i(TAG, "Logging in...");
            if (!sentData) {
                /*
                Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        callback.onSuccess(user, response);
                    }
                }).executeAsync();
                */
                sentData = true;
                Log.wtf(TAG, "Session state is open!");
            }
        } else if (state.isClosed()) {
            //Log.i(TAG, "Logging out...");
        }
    }

    /**
     * Logout From Facebook
     */
    public void logout(Context context) {
        Session session = Session.getActiveSession();
        if (session != null) {
            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
                //clear your preferences if saved
            }
        } else {
            session = new Session(context);
            Session.setActiveSession(session);

            session.closeAndClearTokenInformation();
            //clear your preferences if saved
        }
    }

    /**
     * Login to Facebook programmatically
     */
    public void login(final FBCallback callback) {
        Log.d(TAG, "Logging in!");

        Session.openActiveSession(activity, true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {
                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                Log.d(TAG, user.getName() + "!");
                                callback.onSuccess(user, response);
                            }
                        }
                    }).executeAsync();
                }
            }
        });
    }
}
