package com.ci.marcopolo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex on 3/23/15.
 */
public class EditPictureActivity extends Activity {
    public final static String TAG = "EditPictureActivity";

    // image data
    private Bitmap bitmap;
    private ArrayList<ArrayList<PointF>> drawerLineList;

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
    }

    // TODO fix this so the lineList is preserved
    @Override
    public void onResume() {
        super.onResume();

        Toast.makeText(this, TAG + " onResume", Toast.LENGTH_SHORT).show();
        if (drawerLineList != null) {
            drawer.setLineList(drawerLineList);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Toast.makeText(this, TAG + " onPause", Toast.LENGTH_SHORT).show();
        drawerLineList = drawer.getLineList();
    }

    @Override
    public void onDestroy() {
        super.onPause();

        Toast.makeText(this, TAG + " onDestroy", Toast.LENGTH_SHORT).show();
        drawerLineList = drawer.getLineList();
    }
}
