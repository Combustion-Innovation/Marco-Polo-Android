package com.ci.hub.myapplication;

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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alex on 1/24/15.
 */
public class ShareActivity extends Activity implements GestureDetector.OnGestureListener {
    public final static String TAG = "ShareActivity";

    private String user_data;
    private boolean shareButtonActive = true;
    private GestureDetector gestureDetector;
    private Button shareButton;
    private LinearLayout socialLayout;
    private ImageButton facebookButton;
    private ImageButton twitterButton;
    private ImageButton ellipsisButton;

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

        // add OTLs for touch gestures
        findViewById(R.id.back).setOnClickListener(backOCL);
        shareButton.setOnTouchListener(shareOTL);
        facebookButton.setOnTouchListener(socialOTL);
        twitterButton.setOnTouchListener(socialOTL);
        ellipsisButton.setOnTouchListener(socialOTL);

        populateView();

        // this contact code crashes on a real phone
        /*
        ContactManager contactManager = new ContactManager(this);
        Iterator<String> iterator = contactManager.getContactsNames().iterator();
        Log.d(TAG, "Start iterating through contacts.");
        while (iterator.hasNext()) {
            Log.d(TAG, iterator.next());
        }
        Log.d(TAG, "Stop iterating through contacts.");
        */
    }

    private void populateView() {
        // TODO get contact information and fill the list view with it
        ShareListViewAdapter adapter = new ShareListViewAdapter(getApplicationContext());
        final ListView listView = (ListView) findViewById(R.id.share_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view.findViewById(R.id.marcopolo_cell_center)).getText().toString();
                Toast.makeText(listView.getContext(), "Inviting " + name + "!", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(adapter);
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
}
