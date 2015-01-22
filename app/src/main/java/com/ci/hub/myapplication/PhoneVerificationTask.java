package com.ci.hub.myapplication;

import android.os.AsyncTask;

import com.techventus.server.voice.Voice;

import java.util.HashMap;

/**
 * Created by Alex on 1/21/15.
 */
public class PhoneVerificationTask extends AsyncTask<HashMap<String, String>, String, String> {
    @Override
    protected String doInBackground(HashMap<String, String>... params) {
        HashMap<String, String> data = params[0];
        String userName = data.get("username");
        String pass = data.get("password");

        try {
            Voice voice = new Voice(userName, pass);
            voice.sendSMS(data.get("phone"), "Your MarcoPolo verification code: " + data.get("code"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
