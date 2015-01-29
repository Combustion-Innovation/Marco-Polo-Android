package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

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

    // TODO add event listeners to the MarcoPoloBoxes and add sending marco and polo functionality
    private void populateView() {
        LinearLayout root = (LinearLayout) findViewById(R.id.main_marcopolo_box_root);
        int root_index_offset = 0;

        // add marcos to view
        for (int i = 0; i < marcos.length(); i++) {
            View marcoPoloBox;
            try {
                marcoPoloBox = createMarcoBox(root, marcos.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            // every other marco is a darker blue
            if (i % 2 == 0) {
                marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.dark_blue));
            }
            root.addView(marcoPoloBox, root_index_offset + i);
        }
        root_index_offset += marcos.length();

        // add polos to view
        for (int i = 0; i < polos.length(); i++) {
            View marcoPoloBox;
            try {
                marcoPoloBox = createPoloBox(root, polos.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            // every other polo is a darker blue
            if (i % 2 == 0) {
                marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.dark_blue));
            }
            root.addView(marcoPoloBox, root_index_offset + i);
        }

        // add friends to view
        int friend_count = 0;   // this is needed for the view offset
        for (char initial = 'A'; initial < 'Z'; initial++) {    // loop through initials
            JSONArray initialGroup;
            try {
                initialGroup = friends.getJSONArray(initial + "");
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }
            for (int i = 0; i < initialGroup.length(); i++, friend_count++) {
                View marcoPoloBox;
                try {
                    marcoPoloBox = createFriendBox(root, initialGroup.getJSONObject(i));
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    return;
                }
                if (i == 0) {   // the first friend of the initialGroup gets the letter
                    TextView letter = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_letter);
                    letter.setText(initial + "");
                }

                // every other friend is a lighter green
                if (friend_count % 2 == 0) {
                    marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.dark_green));
                } else {
                    marcoPoloBox.setBackgroundColor(getResources().getColor(R.color.green));
                }
                root.addView(marcoPoloBox, root_index_offset + friend_count);
            }
        }
        root_index_offset += friend_count;
    }

    private View createMarcoBox(LinearLayout root, JSONObject marcoData) {
        // NE 420 km : 69d ago
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marcoBox = inflater.inflate(R.layout.layout_marcopolo_box, root, false);
        Button button = (Button) marcoBox.findViewById(R.id.marcopolo_box_button);
        TextView letter = (TextView) marcoBox.findViewById(R.id.marcopolo_box_letter);
        TextView info = (TextView) marcoBox.findViewById(R.id.marcopolo_box_info);
        TextView type = (TextView) marcoBox.findViewById(R.id.marcopolo_box_type);

        try {
            button.setText(marcoData.getString("username"));
            String infoText = marcoData.getString("direction") + " "
                    + marcoData.getString("distance") + " "
                    + marcoData.getString("created");
            info.setText(infoText);
            type.setText("MARCO");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return marcoBox;
    }

    private View createPoloBox(LinearLayout root, JSONObject poloData) {
        // NE 420 km : 69d ago
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marcoPoloBox = inflater.inflate(R.layout.layout_marcopolo_box, root, false);
        Button button = (Button) marcoPoloBox.findViewById(R.id.marcopolo_box_button);
        TextView letter = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_letter);
        TextView info = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_info);
        TextView type = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_type);

        try {
            ;
            button.setText(poloData.getString("username"));
            String infoText = poloData.getString("direction") + " "
                    + poloData.getString("distance") + " "
                    + poloData.getString("created");
            info.setText(infoText);
            type.setText("POLO");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return marcoPoloBox;
    }

    private View createFriendBox(LinearLayout root, JSONObject friend) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marcoPoloBox = inflater.inflate(R.layout.layout_marcopolo_box, root, false);
        Button button = (Button) marcoPoloBox.findViewById(R.id.marcopolo_box_button);
        TextView letter = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_letter);
        TextView info = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_info);
        TextView type = (TextView) marcoPoloBox.findViewById(R.id.marcopolo_box_type);

        try {
            button.setText(friend.getString("username"));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return marcoPoloBox;
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
