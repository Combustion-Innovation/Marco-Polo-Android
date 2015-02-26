package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    // callback codes
    public final static int GET_MARCO_POLO_DATA = 0;
    public final static int BLOCK_USER = 1;
    public final static int UNBLOCK_USER = 2;

    private String user_data;
    private JSONObject marcoPoloData;
    private JSONObject mp;
    private JSONArray blocked;
    private JSONArray marcos;
    private JSONArray polos;
    private JSONObject friends;
    private JSONObject contacts;
    private ArrayList<JSONObject> flattenedCellData;   // polos + marcos + flattened friends all together

    // these are for debugging purposes only
    private boolean unblocked_users = false;

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

        getListOfMarcoPolosTask.setCommunicator(this);
        data.put("user_id", user_id);
        data.put("lat", "1");
        data.put("lng", "1");

        getListOfMarcoPolosTask.execute(data);
    }

    private void sendPolo(JSONObject currentUser, JSONObject clickedUser) {
        PoloWasClickedTask poloWasClickedTask = new PoloWasClickedTask();
        HashMap<String, String> data = new HashMap<String, String>();
        String user_id,
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
        String user_id,
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
        try {
            Log.d(TAG, new JSONObject(user_data).toString(4));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final MainListViewAdapter adapter = new MainListViewAdapter(this, flattenedCellData);
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

                getMarcoPoloData(); // refresh marco/polo data after sending
            }
        });
        listView.setAdapter(adapter);
    }

    @Override
    public void gotResponse(JSONObject s, int code) {
        if (code == GET_MARCO_POLO_DATA) {
            Log.d(TAG, "Got the marco/polo data.");
            marcoPoloData = s;
            try {
                mp = s.getJSONObject("mp");
                blocked = s.getJSONArray("blocked");
                marcos = s.getJSONArray("marcos");
                polos = s.getJSONArray("polos");
                friends = s.getJSONObject("friends");
                contacts = s.getJSONObject("contacts");

                flattenedCellData = new ArrayList<JSONObject>();
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
                if (!friends.has("empty")) {
                    for (char initial = 'A'; initial <= 'Z'; initial++) {
                        JSONArray initialGroup = friends.getJSONArray(initial + "");
                        for (int i = 0; i < initialGroup.length(); i++) {
                            JSONObject friend = initialGroup.getJSONObject(i);
                            friend.put("type", "FRIEND");
                            flattenedCellData.add(friend);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            Log.d(TAG, "unblocked_users is " + unblocked_users);
            if (!unblocked_users) { // TODO this is for debugging only
                Log.d(TAG, "Unblocking users.");
                unblockAllUsers();
                unblocked_users = true;
                getMarcoPoloData();
            } else {
                populateView();
            }
        } else if (code == BLOCK_USER) {
            String status;
            try {
                status = s.getString("status");
                Log.d(TAG, status);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            if (status.equals("two")) {
                Log.d(TAG, "One of the parameters was not sent correctly.");
            } else {
                Toast.makeText(this, "User was successfully blocked", Toast.LENGTH_SHORT).show();
                getMarcoPoloData();
            }
        } else if (code == UNBLOCK_USER) {
            String status;
            try {
                Log.d(TAG, s.toString(4));
                status = s.getString("status");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (status.equals("two")) {
                Toast.makeText(this, "One of the parameters was not sent correctly.", Toast.LENGTH_LONG).show();
            } else if (status.equals("one")) {
                Toast.makeText(this, "The user was successfully unblocked.", Toast.LENGTH_SHORT).show();
                getMarcoPoloData();
            }
        }
    }

    public void unblockUser(JSONObject unblockedUserData) {
        UnblockUserTask unblockUserTask = new UnblockUserTask();
        HashMap<String, String> data = new HashMap<String, String>();

        unblockUserTask.setCommunicator(this);
        try {
            data.put("user_id", new JSONObject(user_data).getString("user_id"));
            data.put("blocked", unblockedUserData.getString("user_id"));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
        unblockUserTask.execute(data);
    }

    private void unblockAllUsers() {
        for (int i = 0; i < blocked.length(); i++) {
            JSONObject unblockUser;
            try {
                unblockUser = blocked.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }
            unblockUser(unblockUser);
        }
    }

    public void blockUser(JSONObject blockedUserData) {
        BlockUserTask blockUserTask = new BlockUserTask();
        HashMap<String, String> data = new HashMap<String, String>();

        blockUserTask.setCommunicator(this);
        try {
            Log.d(TAG, "Blocking " + blockedUserData.getString("username") + ".");
            JSONObject userData = new JSONObject(user_data);
            data.put("user_id", userData.getString("user_id"));
            data.put("blocked", blockedUserData.getString("user_id"));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
        blockUserTask.execute(data);
    }
}
