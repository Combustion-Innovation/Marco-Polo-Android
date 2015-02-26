package com.ci.marcopolo;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/23/15.
 */
public class EditUsernameActivity extends Activity {
    public static final String TAG = "EditUsernameActivity";

    private String username;
    private String user_id;

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnTouchListener enterOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition(transitionTime * 2);
                EditText usernameField = (EditText) findViewById(R.id.edit_username_username_field);
                changeUsername(usernameField.getText().toString());
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_username);

        try {
            JSONObject user_data = new JSONObject(getIntent().getExtras().getString("user_data"));
            username = user_data.getString("username");
            user_id = user_data.getString("user_id");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // set the username field to the user's username
        EditText usernameField = (EditText) findViewById(R.id.edit_username_username_field);
        usernameField.setText(username);

        findViewById(R.id.back).setOnClickListener(backOCL);
        findViewById(R.id.edit_username_enter_button).setOnTouchListener(enterOTL);
    }

    private void changeUsername(String username) {
        UpdateUsernameTask updateUsernameTask = new UpdateUsernameTask();
        HashMap<String, String> data = new HashMap<String, String>();

        updateUsernameTask.setActivity(this);
        data.put("username", username);
        data.put("user_id", user_id);

        updateUsernameTask.execute(data);
    }
}
