package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private ArrayList<JSONObject> flattenedCellData;   // polos + marcos + flattened friends all together

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
        flattenedCellData = new ArrayList<JSONObject>();

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

    private void sendPolo(JSONObject currentUser, JSONObject clickedUser) {
        PoloWasClickedTask poloWasClickedTask = new PoloWasClickedTask();
        HashMap<String, String> data = new HashMap<String, String>();
        String  user_id,
                sendee_id,
                marco_id,
                lat,
                lng;
        try {
            user_id = currentUser.getString("user_id");
            sendee_id = clickedUser.getString("user_id");
            marco_id = "69";    // TODO not sure where to find this value
            lat = "147"; // TODO get the real value of lat
            lng = "123"; // TODO get the real value of lng
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        poloWasClickedTask.setCommunicator(MainActivity.this);
        data.put("user_id", user_id);
        data.put("sendee_id", sendee_id);
        data.put("marco_id", marco_id);
        data.put("lat", lat);
        data.put("lng", lng);
        poloWasClickedTask.execute(data);
    }

    private void sendMarco(JSONObject currentUser, JSONObject clickedUser) {
        MarcoWasClickedTask marcoWasClickedTask = new MarcoWasClickedTask();
        HashMap<String, String> data = new HashMap<String, String>();
        String  user_id,
                sendee_id;
        try {
            JSONObject user_data_json = new JSONObject(user_data);
            user_id = user_data_json.getString("user_id");
            sendee_id = clickedUser.getString("user_id");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        marcoWasClickedTask.setCommunicator(MainActivity.this);
        data.put("user_id", user_id);
        data.put("sendee_id", sendee_id);
        marcoWasClickedTask.execute(data);
    }

    private void populateView() {
        // TODO update this to use a list view adapter
        // TODO add an onItemClickListener to send marcos/polos

        try {
            Log.d(TAG, new JSONObject(user_data).toString(4));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final MainListViewAdapter adapter = new MainListViewAdapter(getApplicationContext(), flattenedCellData);
        final ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // determine what type the cell is from the position
                String type, name;
                JSONObject currentUser, clickedUser;
                if (position < polos.length()) {
                    // polos are added first
                    type = "POLO";
                } else if (position < polos.length() + marcos.length()) {
                    // if the position is greater than the # of polos, it becomes a marco
                    type = "MARCO";
                } else {
                    // if the position is greater than the # of polos and marcos, it becomes a friend
                    type = ((TextView) view.findViewById(R.id.marcopolo_cell_bottom_right)).getText().toString();
                }

                try {
                    currentUser = new JSONObject(user_data);
                    clickedUser = flattenedCellData.get(position);
                    name = clickedUser.getString("username");
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    return;
                }

                if (type == "POLO") {
                    sendPolo(currentUser, clickedUser);
                    Toast.makeText(listView.getContext(), "Poloing " + name + "!", Toast.LENGTH_SHORT).show();

                } else { //if (type == "MARCO") {
                    sendMarco(currentUser, clickedUser);
                    Toast.makeText(listView.getContext(), "Marcoing " + name + "!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listView.setAdapter(adapter);
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

            // fill cell_data_array
            for (int i = 0; i < polos.length(); i++) {
                JSONObject polo = polos.getJSONObject(i);
                polo.put("type", "POLO");
                flattenedCellData.add(polo);
            }
            for (int i = 0; i < marcos.length(); i++) {
                JSONObject marco = marcos.getJSONObject(i);
                marco.put("type", "MARCO");
                flattenedCellData.add(marco);
            }
            for (char initial = 'A'; initial <= 'Z'; initial++) {
                JSONArray initialGroup = friends.getJSONArray(initial + "");
                for (int i = 0; i < initialGroup.length(); i++) {
                    JSONObject friend = initialGroup.getJSONObject(i);
                    friend.put("type", "FRIEND");
                    flattenedCellData.add(friend);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        populateView();
    }
}
