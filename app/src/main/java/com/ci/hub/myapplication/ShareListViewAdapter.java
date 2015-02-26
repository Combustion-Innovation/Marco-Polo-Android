package com.ci.hub.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ci.hub.contactmanager.Contact;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 2/4/15.
 */
public class ShareListViewAdapter extends BaseSwipeAdapter implements ListAdapter {
    public final static String TAG = "ListViewAdapter";

    private ShareActivity activity;
    private Context context;
    private List<Contact> contacts;

    public ShareListViewAdapter(ShareActivity activity, List<Contact> contacts) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.contacts = contacts;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.marcopolo_swipe_layout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_marcopolo_swipe_cell, null);
    }

    @Override
    public void fillValues(int position, final View convertView) {
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        final ImageButton block = (ImageButton) convertView.findViewById(R.id.marcopolo_block);
        final RelativeLayout top = (RelativeLayout) convertView.findViewById(R.id.top_wrapper);
        final TextView center = (TextView) convertView.findViewById(R.id.marcopolo_cell_center);
        final TextView bottomLeft = (TextView) convertView.findViewById(R.id.marcopolo_cell_bottom_left);
        final Contact currentContact = contacts.get(position);

        if (position % 2 != 0) {
            top.setBackgroundColor(context.getResources().getColor(R.color.dark_green));
        }
        center.setText(currentContact.getName());
        bottomLeft.setText(currentContact.getPhone());
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);
        block.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        inviteUser(convertView, currentContact);
                        return false;
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void inviteUser(View v, Contact contact) {
        Log.d(TAG, "Clicked on the cancel button for " + contact.getName());
    }
}
