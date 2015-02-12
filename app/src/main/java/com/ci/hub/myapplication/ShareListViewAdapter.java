package com.ci.hub.myapplication;

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

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

/**
 * Created by Alex on 2/4/15.
 */
public class ShareListViewAdapter extends BaseSwipeAdapter implements ListAdapter {
    public final static String TAG = "ListViewAdapter";

    private Context context;
    //private JSONArray contacts;

    public ShareListViewAdapter(Context context) {
        this.context = context;
        //this.contacts = contacts;
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

        if (position % 2 != 0) {
            top.setBackgroundColor(context.getResources().getColor(R.color.dark_green));
        }
        center.setText("Mr. " + Math.abs(convertView.hashCode()));
        bottomLeft.setText("" + position);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);
        block.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Blocking Mr. " + Math.abs(convertView.hashCode()) + ".");
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
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
