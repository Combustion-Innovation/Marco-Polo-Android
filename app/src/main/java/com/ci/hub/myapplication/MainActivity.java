package com.ci.hub.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

/**
 * Created by Alex on 1/18/15.
 */
public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
