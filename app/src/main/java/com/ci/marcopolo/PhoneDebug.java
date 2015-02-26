package com.ci.marcopolo;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Alex on 2/20/15.
 */
public class PhoneDebug {
    public static void log(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }
}
