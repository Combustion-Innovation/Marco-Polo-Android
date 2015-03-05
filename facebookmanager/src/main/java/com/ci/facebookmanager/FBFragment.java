package com.ci.facebookmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.List;

/**
 * Created by Alex on 12/5/14.
 */
public class FBFragment extends Fragment {
    public static final String TAG = "FBFragment";

    private int id;
    private List permissions;
    private boolean sentData = false;
    private LoginButton loginButton;
    private UiLifecycleHelper uiHelper;
    private FBCallback callback;

    // onAttach tasks
    private boolean onAttachLogin = false;
    private FBCallback loginCallback;

    private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (permissions == null) {
            Log.e(TAG, "FBFragment needs permissions.");
            return;
        } else {
            Log.d(TAG, permissions.toString());
            loginButton.setReadPermissions(permissions);
        }

        if (state.isOpened()) {
            //Log.i(TAG, "Logging in...");
            if (!sentData) {
                Request.newMeRequest(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        callback.onSuccess(user, response);
                    }
                }).executeAsync();
                sentData = true;
            }
        } else if (state.isClosed()) {
            //Log.i(TAG, "Logging out...");
        }
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

    public void setViewId(int id) {
        this.id = id;
    }

    public void setCallback(FBCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        loginButton = (LoginButton) view.findViewById(id);
        loginButton.setFragment(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentData = false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (onAttachLogin) {
            login(loginCallback);
            onAttachLogin = false;
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
        if (isAdded()) {
            Log.d(TAG, "Logging in!");

            Session.openActiveSession(getActivity(), this, true, new Session.StatusCallback() {
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
        } else {
            Log.d(TAG, "This fragment hasn't been attached yet.");
            onAttachLogin = true;
            loginCallback = callback;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), sessionCallback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) && !sentData) {
            //Log.d(TAG, "onResume() launching onSessionStateChange\t");
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}
