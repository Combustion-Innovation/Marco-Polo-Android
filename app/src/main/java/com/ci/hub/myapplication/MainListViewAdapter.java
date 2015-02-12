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

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alex on 2/11/15.
 */
public class MainListViewAdapter extends BaseSwipeAdapter implements ListAdapter {
    public final static String TAG = "MainListViewAdapter";

    private Context context;
    private ArrayList<JSONObject> cellData;
    private int poloCount;
    private int marcoCount;
    private int friendCount;

    private char currentInitial; // this is used when iterating through friends

    public MainListViewAdapter(Context context, ArrayList<JSONObject> cellData) {
        this.context = context;
        this.cellData = cellData;

        int i = 0;
        JSONObject currentCell = cellData.get(0);;
        try {
            // get polo count
            while (currentCell.getString("type").equals("POLO")) {
                i++;
                currentCell = cellData.get(i);
            }
            poloCount = i;
            Log.d(TAG, "polo count " + poloCount);

            // get marco count
            while (currentCell.getString("type").equals("MARCO")) {
                i++;
                currentCell = cellData.get(i);
            }
            marcoCount = i;
            Log.d(TAG, "marco count " + marcoCount);

            // get friend count
            friendCount = getCount() - poloCount - marcoCount;
            Log.d(TAG, "friend count " + friendCount);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
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

        Log.d(TAG, "Filling values for position " + position + " out of " + (getCount() - 1));

        // get the corresponding cell data
        JSONObject data = cellData.get(position);
        // determine what type the cell is from the position
        String type;
        try {
            type = data.getString("type");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }

        if (type == "POLO") {
            // polos are added first
            fillPolo(position, convertView, data);
        } else if (type == "MARCO") {
            int effectivePosition = position - poloCount;
            fillMarco(effectivePosition, convertView, data);
        } else {
            int effectivePosition = position - poloCount - marcoCount;
            fillFriend(effectivePosition, convertView, data);
        }

        block.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Sending Mr. " + Math.abs(convertView.hashCode()) + " a polo.");
                        return false;
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void fillPolo(int position, final View convertView, JSONObject data) {
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        final ImageButton block = (ImageButton) convertView.findViewById(R.id.marcopolo_block);
        final RelativeLayout top = (RelativeLayout) convertView.findViewById(R.id.top_wrapper);
        final TextView center = (TextView) convertView.findViewById(R.id.marcopolo_cell_center);
        final TextView bottomLeft = (TextView) convertView.findViewById(R.id.marcopolo_cell_bottom_left);
        final TextView bottomRight = (TextView) convertView.findViewById(R.id.marcopolo_cell_bottom_right);

        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);

        if (position % 2 == 0) {
            top.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            top.setBackgroundColor(context.getResources().getColor(R.color.dark_red));
        }

        try {
            center.setText(data.getString("username"));
            bottomRight.setText("POLO");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    private void fillMarco(int position, final View convertView, JSONObject data) {
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        final ImageButton block = (ImageButton) convertView.findViewById(R.id.marcopolo_block);
        final RelativeLayout top = (RelativeLayout) convertView.findViewById(R.id.top_wrapper);
        final TextView center = (TextView) convertView.findViewById(R.id.marcopolo_cell_center);
        final TextView bottomLeft = (TextView) convertView.findViewById(R.id.marcopolo_cell_bottom_left);
        final TextView bottomRight = (TextView) convertView.findViewById(R.id.marcopolo_cell_bottom_right);

        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);

        if (position % 2 == 0) {
            top.setBackgroundColor(context.getResources().getColor(R.color.blue));
        } else {
            top.setBackgroundColor(context.getResources().getColor(R.color.dark_blue));
        }

        try {
            center.setText(data.getString("username"));
            bottomLeft.setText(data.getString("distance"));
            bottomRight.setText("MARCO");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    private void fillFriend(int position, final View convertView, JSONObject data) {
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        final ImageButton block = (ImageButton) convertView.findViewById(R.id.marcopolo_block);
        final RelativeLayout top = (RelativeLayout) convertView.findViewById(R.id.top_wrapper);
        final TextView center = (TextView) convertView.findViewById(R.id.marcopolo_cell_center);
        final TextView bottomLeft = (TextView) convertView.findViewById(R.id.marcopolo_cell_bottom_left);
        final TextView bottomRight = (TextView) convertView.findViewById(R.id.marcopolo_cell_bottom_right);
        final TextView topLeft = (TextView) convertView.findViewById(R.id.marcopolo_cell_top_left);

        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Left);

        if (position % 2 == 0) {
            top.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            top.setBackgroundColor(context.getResources().getColor(R.color.dark_green));
        }

        try {
            char initial = data.getString("username").charAt(0);
            if (currentInitial != initial) {
                currentInitial = initial;
                topLeft.setText(Character.toString(currentInitial));
            }
            center.setText(data.getString("username"));
            bottomRight.setText("MARCO"); // TODO this seems to be completely random right now
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
    }

    @Override
    public int getCount() {
        return cellData.size();
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
