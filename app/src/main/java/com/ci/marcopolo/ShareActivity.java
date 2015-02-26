package com.ci.marcopolo;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ci.contactmanager.CICallback;
import com.ci.contactmanager.Contact;
import com.ci.contactmanager.ContactManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alex on 1/24/15.
 */
public class ShareActivity extends Activity implements GestureDetector.OnGestureListener, Communicator {
    public final static String TAG = "ShareActivity";

    // callback codes
    public final static int ADD_CONTACTS = 0;

    // data
    private String user_data;
    private List<Contact> contacts;

    private boolean shareButtonActive = true;

    // layout objects
    private GestureDetector gestureDetector;
    private Button shareButton;
    private LinearLayout socialLayout;
    private ImageButton facebookButton;
    private ImageButton twitterButton;
    private ImageButton ellipsisButton;
    private ListView listView;

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnTouchListener shareOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
    };

    private View.OnTouchListener socialOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TransitionDrawable transition = (TransitionDrawable) v.getBackground();
            Log.d(TAG, v.toString());
            Log.d(TAG, transition == null ? "NULL" : "NOT NULL");
            final int TRANSITION_TIME = 80;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // do drawable transition stuff
                transition.startTransition(TRANSITION_TIME);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // drawable transition stuff
                transition.reverseTransition(TRANSITION_TIME);
                onSocialButtonPressed(v);
            }
            gestureDetector.onTouchEvent(event);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        user_data = getIntent().getExtras().getString("user_data");
        gestureDetector = new GestureDetector(this, this);
        shareButton = (Button) findViewById(R.id.share_share_button);
        socialLayout = (LinearLayout) findViewById(R.id.share_social_root);
        facebookButton = (ImageButton) findViewById(R.id.share_facebook_button);
        twitterButton = (ImageButton) findViewById(R.id.share_twitter_button);
        ellipsisButton = (ImageButton) findViewById(R.id.share_ellipsis_button);
        listView = (ListView) findViewById(R.id.share_list_view);

        // add OTLs for touch gestures
        findViewById(R.id.back).setOnClickListener(backOCL);
        shareButton.setOnTouchListener(shareOTL);
        facebookButton.setOnTouchListener(socialOTL);
        twitterButton.setOnTouchListener(socialOTL);
        ellipsisButton.setOnTouchListener(socialOTL);

        listView.setFastScrollEnabled(true);

        ContactManager contactManager = new ContactManager(this);
        try {
            contactManager.getContacts(new CICallback() {
                @Override
                public void onStart(Object o) {
                    Log.d(TAG, "Start iterating through contacts.");
                }

                @Override
                public void onProgress(Object o) {

                }

                @Override
                public void onEnd(Object o) {
                    Log.d(TAG, "Stop iterating through contacts.");
                    List contacts = (List) o;
                    Iterator<Contact> i = contacts.iterator();
                    while (i.hasNext()) {
                        Contact contact = i.next();
                        Log.d(TAG, "name: " + contact.getName());
                        Log.d(TAG, "phone: " + contact.getPhone());
                    }
                    ShareActivity.this.contacts = contacts;
                    populateView();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateView() {
        // TODO get contact information and fill the list view with it
        try {
            final ShareListViewAdapter adapter = new ShareListViewAdapter(this, contacts);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact contact = (Contact) adapter.getItem(position);
                    inviteUser(view, contact);
                }
            });
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void inviteUser(View v, Contact contact) {
        GetFriendsPhoneNumbersTask getFriendsPhoneNumbersTask = new GetFriendsPhoneNumbersTask();
        HashMap<String, String> data = new HashMap<>();

        getFriendsPhoneNumbersTask.setCommunicator(this);
        data.put("phone", contact.getPhone());
        try {
            data.put("user_id", new JSONObject(user_data).getString("user_id"));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
        getFriendsPhoneNumbersTask.execute(data);
    }

    private void toggleSocialButtons() {
        Log.d(TAG, "Toggling social buttons!");
        if (shareButtonActive) {
            shareButton.setVisibility(View.INVISIBLE);
            socialLayout.setVisibility(View.VISIBLE);
        } else {
            shareButton.setVisibility(View.VISIBLE);
            socialLayout.setVisibility(View.INVISIBLE);
        }
        shareButtonActive = !shareButtonActive;
    }

    private void onSocialButtonPressed(View v) {
        int id = v.getId();
        if (id == R.id.share_facebook_button) {
            // share on fb
            Log.d(TAG, "Sharing on FB.");
        } else if (id == R.id.share_twitter_button) {
            // share on twitter
            Log.d(TAG, "Sharing on Twitter.");
        } else if (id == R.id.share_ellipsis_button) {
            // open more share options
            Log.d(TAG, "Opening more sharing options.");
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //Log.d(TAG, e.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Log.d(TAG, e.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (shareButtonActive) {
            toggleSocialButtons();
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Log.d(TAG, e1.toString());
        //Log.d(TAG, e2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //Log.d(TAG, e.toString());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        final int MIN_DISTANCE = 20;
        if (shareButtonActive && e2.getX() > e1.getX() + MIN_DISTANCE) {
            toggleSocialButtons();
        } else if (!shareButtonActive && e1.getX() > e2.getX() + MIN_DISTANCE) {
            toggleSocialButtons();
        }
        return true;
    }

    @Override
    public void gotResponse(JSONObject r, int code) {
        if (code == ADD_CONTACTS) {
            String status;
            try {
                status = r.getString("status");
                Log.d(TAG, "status: " + status);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }

            if (status.equals("two")) {
                Log.d(TAG, "One of the parameters was not sent correctly.");
            } else {
                Toast.makeText(this, "Your contacts were invited to join MarcoPolo!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
