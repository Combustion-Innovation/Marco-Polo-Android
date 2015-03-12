package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alex on 1/23/15.
 */
public class SettingsActivity extends Activity implements Communicator {
    public static final String TAG = "SettingsActivity";

    // callback codes
    public static final int SETTINGS_UPDATED = 0;

    private String user_data;
    private boolean settingsChanged = false;
    private String distance;
    private String user_id;
    private String searchable;
    private String push_notifications;
    private String location_services;

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (settingsChanged) {
                updateSettings();

                Intent intent = new Intent();
                intent.putExtra("new_user_data", user_data);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };

    private View.OnTouchListener editInfoOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition((int) (transitionTime * 2));
                Intent intent = new Intent(getApplicationContext(), EditInfoActivity.class);
                intent.putExtra("user_data", user_data);
                startActivity(intent);
            }
            return true;
        }
    };

    private View.OnTouchListener logoutOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            final int transitionTime = 0;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                transition.startTransition(transitionTime);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                transition.reverseTransition((int) (transitionTime * 2));
                logout();
            }
            return true;
        }
    };

    private CompoundButton.OnCheckedChangeListener searchableOCL = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            searchable = isChecked ? "1" : "0";
            settingsChanged = true;
        }
    };

    private CompoundButton.OnCheckedChangeListener pushNotificationsOCL = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            push_notifications = isChecked ? "1" : "0";
            settingsChanged = true;
        }
    };

    private CompoundButton.OnCheckedChangeListener locationServicesOCL = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            location_services = isChecked ? "1" : "0";
            settingsChanged = true;
        }
    };

    private View.OnClickListener distanceOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button kilometers = (Button) findViewById(R.id.distance_kilometers);
            Button miles = (Button) findViewById(R.id.distance_miles);
            Button button = (Button) v;
            String text = button.getText().toString();

            if (distance.equals(text)) {
                return;
            }

            if (text.equals(getResources().getString(R.string.distance_kilometer))) {
                Log.d(TAG, "switching to kilometers");
                kilometers.setBackgroundColor(getResources().getColor(R.color.dark_green));
                miles.setBackgroundColor(getResources().getColor(R.color.green));
                distance = "KM";
            } else {
                Log.d(TAG, "switching to miles");
                kilometers.setBackgroundColor(getResources().getColor(R.color.green));
                miles.setBackgroundColor(getResources().getColor(R.color.dark_green));
                distance = "M";
            }
            settingsChanged = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user_data = getIntent().getExtras().getString("user_data");

        // get layout objects
        Button back = (Button) findViewById(R.id.back);
        Button editInfo = (Button) findViewById(R.id.settings_edit_info_button);
        Button logoutButton = (Button) findViewById(R.id.settings_logout_button);
        Button distanceKilometers = (Button) findViewById(R.id.distance_kilometers);
        Button distanceMiles = (Button) findViewById(R.id.distance_miles);
        CompoundButton searchableSwitch = (CompoundButton) findViewById(R.id.searchable_switch);
        CompoundButton pushNotificationsSwitch = (CompoundButton) findViewById(R.id.push_notifications_switch);
        CompoundButton locationServicesSwitch = (CompoundButton) findViewById(R.id.location_services_switch);

        // initialize layout objects
        back.setOnClickListener(backOCL);
        editInfo.setOnTouchListener(editInfoOTL);
        logoutButton.setOnTouchListener(logoutOTL);
        distanceKilometers.setOnClickListener(distanceOCL);
        distanceMiles.setOnClickListener(distanceOCL);
        searchableSwitch.setOnCheckedChangeListener(searchableOCL);
        pushNotificationsSwitch.setOnCheckedChangeListener(pushNotificationsOCL);
        locationServicesSwitch.setOnCheckedChangeListener(locationServicesOCL);

        try {
            JSONObject user = new JSONObject(user_data);
            user_id = user.getString("user_id");
            distance = user.getString("unit");
            searchable = user.getString("search");
            push_notifications = user.getString("push_notification");
            location_services = "0";  // TODO dont know where to get this
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    private void updateSettings() {
        // update settings on server TODO this doesnt work yet
        UpdateSettingsTask updateSettingsTask = new UpdateSettingsTask();
        HashMap<String, String> data = new HashMap<>();

        updateSettingsTask.setCommunicator(this);
        data.put("push_notification", push_notifications);
        data.put("search", searchable);
        data.put("exact_location", location_services);
        data.put("unit", distance);
        data.put("user_id", user_id);

        updateSettingsTask.execute(data);

        // update settings locally
        try {
            JSONObject new_user_data = new JSONObject(user_data);
            new_user_data.put("unit", distance);
            new_user_data.put("search", searchable);
            new_user_data.put("push_notification", push_notifications);
            new_user_data.put("exact_location", location_services);
            user_data = new_user_data.toString();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    private void logout() {
        Intent intent = new Intent(getApplicationContext(), InitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void gotResponse(JSONObject r, int code) {
        if (code == SETTINGS_UPDATED) {
            Toast.makeText(this, "Your settings have been updated.", Toast.LENGTH_SHORT).show();
        }
    }
}
