package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ci.contactmanager.CICallback;
import com.ci.contactmanager.Contact;
import com.ci.contactmanager.ContactManager;
import com.ci.facebookmanager.FBCallback;
import com.ci.facebookmanager.FBUtility;
import com.daimajia.swipe.SwipeLayout;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alex on 1/24/15.
 */
public class ShareActivity extends Activity implements Communicator, GestureDetector.OnGestureListener {
    public final static String TAG = "ShareActivity";

    // callback codes
    public final static int ADD_CONTACTS = 0;

    // data
    private String user_data;

    // layout objects
    private ListView listView;
    private SwipeLayout socialLayout;
    private ImageButton facebookButton;
    private ImageButton twitterButton;
    private ImageButton ellipsisButton;

    // fling detector
    private GestureDetector gestureDetector;

    // FB sharing objects
    private FBUtility fbUtility;
    private UiLifecycleHelper uiHelper;

    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnTouchListener socialOTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int TRANSITION_TIME = 80;

            ImageButton button = (ImageButton) v;
            TransitionDrawable drawable = (TransitionDrawable) button.getDrawable();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // do drawable transition stuff
                drawable.startTransition(TRANSITION_TIME);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // drawable transition stuff
                drawable.reverseTransition(TRANSITION_TIME);
                if (v.getId() == R.id.share_facebook_button) {
                    facebookShare();
                } else if (v.getId() == R.id.share_twitter_button) {
                    twitterShare();
                } else {
                    openMoreOptions();
                }
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
        listView = (ListView) findViewById(R.id.share_list_view);
        socialLayout = (SwipeLayout) findViewById(R.id.share_social_root);
        facebookButton = (ImageButton) findViewById(R.id.share_facebook_button);
        twitterButton = (ImageButton) findViewById(R.id.share_twitter_button);
        ellipsisButton = (ImageButton) findViewById(R.id.share_ellipsis_button);
        gestureDetector = new GestureDetector(this, this);
        uiHelper = new UiLifecycleHelper(this, null);
        fbUtility = new FBUtility(this);

        // add OTLs for touch gestures
        findViewById(R.id.back).setOnClickListener(backOCL);
        facebookButton.setOnTouchListener(socialOTL);
        twitterButton.setOnTouchListener(socialOTL);
        ellipsisButton.setOnTouchListener(socialOTL);

        // setup the social layout
        listView.setFastScrollEnabled(true);
        socialLayout.setDragEdge(SwipeLayout.DragEdge.Left);
        socialLayout.setDragDistance(90);

        // setup the facebook classes
        uiHelper.onCreate(savedInstanceState);

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
                    List<Contact> contacts = (List<Contact>) o;
                    Iterator<Contact> i = contacts.iterator();
                    while (i.hasNext()) {
                        Contact contact = i.next();
                        //Log.d(TAG, "name: " + contact.getName());
                        //Log.d(TAG, "phone: " + contact.getPhone());
                    }
                    populateView(contacts);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                String errorMsg = String.format("Error: %s", error.toString());
                Toast.makeText(ShareActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                Log.d(TAG, errorMsg);
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Toast.makeText(ShareActivity.this, "Success!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Success!");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    private void populateView(List<Contact> contacts) {
        try {
            final ShareListViewAdapter adapter = new ShareListViewAdapter(this, contacts);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SwipeLayout cell = (SwipeLayout) view;
                    Contact contact = (Contact) adapter.getItem(position);

                    Log.d(TAG, "Drag distance: " + cell.getDragDistance());
                    cell.setDragDistance(100);
                    inviteUser(cell, contact);
                }
            });
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private String formatPhoneNumber(String phone) {
        return phone.replaceAll("[^0-9]", "");
    }

    public void inviteUser(View v, Contact contact) {
        GetFriendsPhoneNumbersTask getFriendsPhoneNumbersTask = new GetFriendsPhoneNumbersTask();
        HashMap<String, String> data = new HashMap<>();

        getFriendsPhoneNumbersTask.setCommunicator(this);
        String phone, user_id;
        try {
            JSONObject jsonData = new JSONObject(user_data);
            phone = formatPhoneNumber(contact.getPhone());
            user_id = jsonData.getString("user_id");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
        Log.d(TAG, "phones: " + phone);
        Log.d(TAG, "user_id: " + user_id);
        data.put("phones", phone);
        data.put("user_id", user_id);
        getFriendsPhoneNumbersTask.execute(data);
    }

    private void facebookShare() {
        Log.d(TAG, "Sharing MarcoPolo on Facebook!");

        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            // Publish the post using the Share Dialog
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                    .setName("Hello World!")
                    .setCaption("test caption")
                    .setDescription("test description")
                    .setLink("http://www.randomwebsite.com/")
                    .setPicture("http://www.randomwebsite.com/images/head.jpg")
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());

        } else {
            // Fallback. For example, publish the post using the Feed Dialog
            Log.d(TAG, "You do not have the Facebook app installed.");

            if (Session.getActiveSession().isOpened()) {
                Log.d(TAG, "A session already exists");
                publishFeedDialog();
            } else {
                Log.d(TAG, "A session does not already exist");
                fbUtility.login(new FBCallback() {
                    @Override
                    public void onSuccess(GraphUser a, Response b) {
                        publishFeedDialog();
                    }
                });
            }
        }
    }

    private void publishFeedDialog() {
        Bundle params = new Bundle();
        params.putString("name", "MarcoPolo for Android");
        params.putString("caption", "Test caption.");
        params.putString("description", "MarcoPolo for Android makes it easier and faster to stay connected to your friends.");
        params.putString("link", "http://combustioninnovation.com/about/");
        params.putString("picture", "https://lh3.googleusercontent.com/v58twOOlPxklXpfY7YhQdESt-FVCOaaLCg7-WDq7tJG3cb9e5pk2jMXQ_UYuwhO70vQ8jX9rpwM=w1416-h580");

        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(
                        ShareActivity.this,
                        Session.getActiveSession(),
                        params
                )
        ).setOnCompleteListener(
                new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Log.d(TAG, "Posted story, id: " + postId);
                            } else {
                                // User clicked the Cancel button
                               Log.d(TAG, "Publish cancelled");
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Log.d(TAG, "Publish cancelled");
                        } else {
                            // Generic, ex: network error
                            Log.d(TAG, "Error posting story");
                        }
                        fbUtility.logout(getApplicationContext());
                    }
                }).build();
        feedDialog.show();
    }

    private void twitterShare() {
        Log.d(TAG, "Sharing MarcoPolo on Twitter!");
    }

    private void openMoreOptions() {
        Log.d(TAG, "Opening more sharing options...");
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
            } else if (status.equals("one")) {
                Toast.makeText(this, "Your contacts were invited to join MarcoPolo!", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "ERROR: Unexpected status code");
            }
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
        if (e2.getX() > e1.getX() + MIN_DISTANCE) {
            socialLayout.open();
        } else if (e1.getX() > e2.getX() + MIN_DISTANCE) {
            socialLayout.close();
        }
        return true;
    }
}
