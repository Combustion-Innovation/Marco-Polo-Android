package com.ci.marcopolo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

/**
 * Created by Alex on 3/23/15.
 */
public class EditPictureActivity extends Activity {
    public final static String TAG = "EditPictureActivity";

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
    }
}
