package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ci.generalclasses.loginmanagers.Communicator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by Alex on 1/18/15.
 */
public class MainActivity extends Activity implements Communicator {
    public static final String TAG = "MainActivity";

    private String user_data;
    private JSONObject marcoPoloData;
    private JSONObject mp;
    private JSONArray blocked;
    private JSONArray marcos;
    private JSONArray polos;
    private JSONObject friends;
    private JSONObject contacts;

    private View.OnClickListener shareOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
            intent.putExtra("user_data", user_data);
            startActivity(intent);
        }
    };

    private View.OnClickListener settingsOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.putExtra("user_data", user_data);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_data = getIntent().getExtras().getString("user_data");

        getMarcoPoloData();
        // TODO do something with this data

        findViewById(R.id.main_share_button).setOnClickListener(shareOCL);
        findViewById(R.id.main_settings_button).setOnClickListener(settingsOCL);
    }

    private void getMarcoPoloData() {
        GetListOfMarcoPolosTask getListOfMarcoPolosTask = new GetListOfMarcoPolosTask();
        HashMap<String, String> data = new HashMap<String, String>();
        String user_id;
        try {
            JSONObject user_data_json = new JSONObject(user_data);
            user_id = user_data_json.getString("user_id");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        getListOfMarcoPolosTask.setActivity(this);
        data.put("user_id", user_id);
        data.put("lat", "1");
        data.put("lng", "1");

        getListOfMarcoPolosTask.execute(data);
    }

    private void onDataRetrieved() {
        populateView();
    }

    private void populateView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout root = (LinearLayout) findViewById(R.id.main_marcopolo_box_root);
        int marcos_count = marcos.length();
        int polos_count = polos.length();

        for (int i = 0; i < marcos_count; i++) {
            // NE 420 km : 69d ago
            View marcoPoloBox = inflater.inflate(R.layout.layout_marcopolo_box, root, false);
            Button button = (Button) marcoPoloBox.findViewById(R.id.marcopolo_box_button);
            TextView letter = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_letter);
            TextView info = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_info);
            TextView type = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_type);

            try {
                JSONObject marco = marcos.getJSONObject(i);
                button.setText(marco.getString("username"));
                String infoText = marco.getString("direction") + " "
                        + marco.getString("distance") + " "
                        + marco.getString("created");
                info.setText(infoText);
                type.setText("MARCO");
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            if (i % 2 != 0) {
                marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.dark_green));
            }
            root.addView(marcoPoloBox, i);
        }

        for (int i = 0; i < polos_count; i++) {
            // NE 420 km : 69d ago
            View marcoPoloBox = inflater.inflate(R.layout.layout_marcopolo_box, root, false);
            Button button = (Button) marcoPoloBox.findViewById(R.id.marcopolo_box_button);
            TextView letter = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_letter);
            TextView info = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_info);
            TextView type = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_type);

            try {
                JSONObject polo = polos.getJSONObject(i);
                button.setText(polo.getString("username"));
                String infoText = polo.getString("direction") + " "
                        + polo.getString("distance") + " "
                        + polo.getString("created");
                info.setText(infoText);
                type.setText("POLO");
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            if (i % 2 != 0) {
                marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.dark_green));
            }
            root.addView(marcoPoloBox, marcos_count + i);
        }

        // TODO add contacts
    }

    @Override
    public void gotResponse(JSONObject s) {
        marcoPoloData = s;
        try {
            mp = s.getJSONObject("mp");
            blocked = s.getJSONArray("blocked");
            marcos = s.getJSONArray("marcos");
            polos = s.getJSONArray("polos");
            friends = s.getJSONObject("friends");
            contacts = s.getJSONObject("contacts");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        onDataRetrieved();
    }
}
