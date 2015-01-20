package com.ci.generalclasses.loginmanagers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Alex on 12/5/14.
 */
public class FBFragment extends Fragment {
    private static final String TAG = "FBFragment";
    private boolean sentData = false;
    private UiLifecycleHelper uiHelper;
    private List permissions;
    private Request.GraphUserCallback graphUserCallback;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            if (!sentData) {
                Request.newMeRequest(session, graphUserCallback).executeAsync();
                sentData = true;
            }
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

    public void setCallback(Request.GraphUserCallback graphUserCallback) {
        this.graphUserCallback = graphUserCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.fb_login_button);
        authButton.setFragment(this);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d(TAG, "click the login button!\t" + sentData);
                sentData = false;
            }
        });
        if (permissions != null) {
            authButton.setReadPermissions(permissions);
        } else {
            authButton.setReadPermissions(Arrays.asList("email"));
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
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
            Log.d(TAG, "onResume() launching onSessionStateChange\t" + sentData);
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
