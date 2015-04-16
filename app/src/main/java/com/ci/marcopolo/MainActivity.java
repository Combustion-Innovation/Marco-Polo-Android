package com.ci.marcopolo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public final static int SENT_POLO = 3;
    public final static int SENT_MARCO = 4;
    public final static int CHANGE_SETTINGS = 5;

    private String user_data;
    private MarcoPoloData marcoPoloData;

    // layout objects
    private ImageButton shareButton;
    private ImageButton settingsButton;
    private ListView listView;
    private RelativeLayout autoPoloLayout;
    private Button autoPoloButton;
    private ImageView autoPoloImage;

    private int autoPoloLayoutStartHeight;

    // these are for debugging purposes only
    private boolean unblocked_users = false;

    // gesture listeners
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
            startActivityForResult(intent, CHANGE_SETTINGS);
        }
    };

    private View.OnClickListener autoPoloOCL = new View.OnClickListener() {
        private boolean expanded = false;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), AutoPoloActivity.class);
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

        // get layout objects
        shareButton = (ImageButton) findViewById(R.id.main_share_button);
        settingsButton = (ImageButton) findViewById(R.id.main_settings_button);
        listView = (ListView) findViewById(R.id.main_list_view);
        autoPoloLayout = (RelativeLayout) findViewById(R.id.main_autopolo_layout);
        autoPoloButton = (Button) findViewById(R.id.main_autopolo_button);
        autoPoloImage = (ImageView) findViewById(R.id.main_autopolo_image);

        // setup layout objects
        shareButton.setOnClickListener(shareOCL);
        settingsButton.setOnClickListener(settingsOCL);
        autoPoloButton.setOnClickListener(autoPoloOCL);
        autoPoloLayout.bringToFront();  // prevents the bottom buttons from overlapping this
        autoPoloLayoutStartHeight = autoPoloLayout.getLayoutParams().height;

        // location services
        // TODO work on turning this into a service
        Intent locationService = new Intent(getApplicationContext(), LocationService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(locationService, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CHANGE_SETTINGS) {
                user_data = data.getStringExtra("new_user_data");
            }
        } else {
            Log.d(TAG, "There was an error with an intent");
        }
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
        data.put("lat", "1");   // TODO these are random
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
            Log.d(TAG, clickedUser.toString(4));
            user_id = currentUser.getString("user_id");
            sendee_id = clickedUser.getString("user_id");
            marco_id = clickedUser.getString("marco_id");
            lat = "147";        // TODO get the real value of lat
            lng = "123";        // TODO get the real value of lng
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
        String user_id, sendee_id;
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

        final MainListViewAdapter adapter = new MainListViewAdapter(this, marcoPoloData);
        final ListView listView = (ListView) findViewById(R.id.main_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // determine what type the cell is from the position
                String type, name;
                JSONObject currentUser, clickedUser;
                int polosLength = marcoPoloData.getPolos().length(),
                    marcosLength = marcoPoloData.getMarcos().length();
                if (position < polosLength) {
                    // polos are added first
                    type = "POLO";
                } else if (position < polosLength + marcosLength) {
                    // if the position is greater than the # of polos, it becomes a marco
                    type = "MARCO";
                } else {
                    // if the position is greater than the # of polos and marcos, it becomes a friend
                    type = ((TextView) view.findViewById(R.id.marcopolo_cell_bottom_right)).getText().toString();
                }

                try {
                    currentUser = new JSONObject(user_data);
                    String clickedUsername = marcoPoloData.get(position).getString("username");
                    if (type.equals("POLO")) {
                        clickedUser = marcoPoloData.getPolo(clickedUsername);
                    } else {
                        clickedUser = marcoPoloData.getMarco(clickedUsername);
                    }
                    if (clickedUser == null) {
                        // this only happens when the contact has just been added
                        // and no marcos or polos have been sent to them
                        Log.d(TAG, "Marcoing a new contact");
                        clickedUser = marcoPoloData.getContact(clickedUsername);
                    }
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
        // CALLBACK: after getting the marco/polo data from the server
        if (code == GET_MARCO_POLO_DATA) {
            Log.d(TAG, "Got the marco/polo data.");
            marcoPoloData = new MarcoPoloData(s);

            Log.d(TAG, "unblocked_users is " + unblocked_users);
            if (!unblocked_users) { // TODO this is for debugging only!
                Log.d(TAG, "Unblocking users.");
                unblockAllUsers();
                unblocked_users = true;
                getMarcoPoloData();
            } else {
                populateView();
            }
        // CALLBACK: after blocking a user
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
        // CALLBACK: after unblocking a user
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
        } else if (code == SENT_POLO) {
            String status;
            try {
                // the server response here is weirdly formatted
                JSONArray results = s.getJSONArray("results");
                status = results.getJSONObject(0).getString("status");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Log.d(TAG, "THE RESULT: " + status);

            if (status.equals("two")) {
                Log.d(TAG, "A parameter was incorrect.");
            } else if (status.equals("one")) {
                Toast.makeText(this, "Sent a polo!", Toast.LENGTH_SHORT).show();
            }
        } else if (code == SENT_MARCO) {
            String data;
            try {
                data = s.getString("status");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Log.d(TAG, "THE RESULT: " + data);

            if (data.equals("two")) {
                Log.d(TAG, "A parameter was incorrect.");
            } else if (data.equals("one")) {
                Toast.makeText(this, "Sent a marco!", Toast.LENGTH_SHORT).show();
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
        JSONArray blocked = marcoPoloData.getBlocked();
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
