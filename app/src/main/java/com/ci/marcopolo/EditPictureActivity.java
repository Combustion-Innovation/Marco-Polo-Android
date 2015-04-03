package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex on 3/23/15.
 */
public class EditPictureActivity extends Activity {
    public final static String TAG = "EditPictureActivity";

    // image data
    private Bitmap bitmap;

    // layout objects
    private Drawer drawer;
    private static ArrayList<ArrayList<PointF>> lineListBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);

        // get bitmap data
        Intent intent = getIntent();
        if (intent.hasExtra(TakePictureActivity.AUTOPOLO_IMAGE_STRING)) {
            Log.d(TAG, "Loading the bitmap from a String");
            String bitmapPath = intent.getStringExtra(TakePictureActivity.AUTOPOLO_IMAGE_STRING);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        } else if (intent.hasExtra(TakePictureActivity.AUTOPOLO_IMAGE_URI)) {
            Log.d(TAG, "Loading the bitmap from a Uri");
            Toast.makeText(this, "it got to this point", Toast.LENGTH_SHORT).show();
            // TODO the bug is somewhere here!
            try {
                Uri bitmapUri = intent.getParcelableExtra(TakePictureActivity.AUTOPOLO_IMAGE_URI);
                Toast.makeText(this, "bitmapUri " + bitmapUri.toString(), Toast.LENGTH_SHORT).show();
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bitmapUri);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Log.d(TAG, "Error: expected intent data was not received");
        }

        // get layout objects
        drawer = (Drawer) findViewById(R.id.edit_picture_drawer);

        // setup drawer
        //drawer.setBackground(new BitmapDrawable(bitmap));
        /*
        if (savedInstanceState != null && lineListBackup != null) {
            drawer.setLineList(lineListBackup);
            drawer.invalidate();
        } else if (savedInstanceState == null) {
            lineListBackup = null;
        }
        //*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Toast.makeText(this, "onSaveInstanceState", Toast.LENGTH_SHORT).show();
        lineListBackup = drawer.getLineList();
    }
}
