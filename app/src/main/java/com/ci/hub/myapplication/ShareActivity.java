package com.ci.hub.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Alex on 1/24/15.
 */
public class ShareActivity extends Activity {
    public final static String TAG = "ShareActivity";

    private String user_data;

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        user_data = getIntent().getExtras().getString("user_data");

        // TODO add people from user's contact list

        findViewById(R.id.back).setOnClickListener(backOCL);
    }
}
