package com.ci.generalclasses.loginmanagers;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 12/20/14.
 */
public class ProprietaryFragment extends Fragment {
    private final String TAG = "ProprietaryFragment";
    private Activity activity;
    private String URL;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.proprietary_login_layout, container, false);
        Button loginButton = (Button) view.findViewById(R.id.proprietary_fragment_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "logging in...");
                final String username = ((EditText) view.findViewById(R.id.username_edittext)).getText().toString();
                final String password = ((EditText) view.findViewById(R.id.password_edittext)).getText().toString();
                Map<String, String> data = new HashMap<String, String>();
                data.put("username", username);
                data.put("password", password);
                proprietaryLogin(data, activity);
                getActivity().getFragmentManager().popBackStack();
            }
        });
        Button backButton = (Button) view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void proprietaryLogin(Map<String, String> data, Activity activity) {
        ProprietaryLoginTask loginTask = new ProprietaryLoginTask();
        loginTask.setActivity(activity);
        data.put("_url", URL);
        loginTask.execute(data);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setLoginURL(String URL) {
        this.URL = URL;
    }
}
