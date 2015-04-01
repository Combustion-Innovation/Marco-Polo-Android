package com.ci.marcopolo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Alex on 3/23/15.
 */
public class EditPictureActivity extends Activity {
    public final static String TAG = "EditPictureActivity";

    // callback codes
    private final static String SERIALIZE_LINE_LIST = "SERIALIZE_LINE_LIST";

    // image data
    private Bitmap bitmap;

    // layout objects
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);

        // get bitmap data
        String bitmapPath = getIntent().getStringExtra("autopolo_image");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = BitmapFactory.decodeFile(bitmapPath, options);

        // get layout objects
        drawer = (Drawer) findViewById(R.id.edit_picture_drawer);

        // setup drawer
        drawer.setBackground(new BitmapDrawable(bitmap));
        if (savedInstanceState != null) {
            ArrayList recreatedLineList = recreateLineList(savedInstanceState.getString(SERIALIZE_LINE_LIST));
            if (recreatedLineList != null) {
                drawer.setLineList(recreatedLineList);
            }
        }
    }

    // TODO this is super slow, do something better
    private String serializeLineList(ArrayList<ArrayList<PointF>> lineList) {
        JSONArray lineListSerialized = new JSONArray();
        try {
            for (int i = 0; i < lineList.size(); i++) {
                ArrayList<PointF> currentLine = lineList.get(i);
                JSONArray currentLineSerialized = new JSONArray();
                for (int j = 0; j < currentLine.size(); j++) {
                    PointF currentPoint = currentLine.get(j);
                    currentLineSerialized.put(new JSONObject("{x:" + currentPoint.x + ",y:" + currentPoint.y + "}"));
                }
                lineListSerialized.put(currentLineSerialized);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return lineListSerialized.toString();
    }

    private ArrayList<ArrayList<PointF>> recreateLineList(String serial) {
        JSONArray serial_json;
        try {
            serial_json = new JSONArray(serial);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        ArrayList<ArrayList<PointF>> lineList = new ArrayList<>();
        try {
            for (int i = 0; i < serial_json.length(); i++) {
                JSONArray currentLineSerialized = serial_json.getJSONArray(i);
                ArrayList<PointF> currentLine = new ArrayList<>();
                for (int j = 0; j < currentLineSerialized.length(); j++) {
                    JSONObject currentPointSerialized = currentLineSerialized.getJSONObject(j);
                    PointF currentPoint = new PointF();
                    currentPoint.set((float)currentPointSerialized.getDouble("x"), (float)currentPointSerialized.getDouble("y"));
                    currentLine.add(currentPoint);
                }
                lineList.add(currentLine);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
        return lineList;
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        ArrayList<ArrayList<PointF>> lineList = drawer.getLineList();
        String serializedLineList = serializeLineList(lineList);
        outState.putString(SERIALIZE_LINE_LIST, serializedLineList);
    }
}
