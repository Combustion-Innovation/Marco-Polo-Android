package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alex on 3/23/15.
 */
public class EditPictureActivity extends Activity {
    public final static String TAG = "EditPictureActivity";

    // image data
    private Bitmap bitmap;
    private static ArrayList<ArrayList<PointF>> lineListBackup;

    // layout objects
    private Drawer drawer;
    private Button mustacheButton;
    private Button paintbrushButton;
    private Button retakeButton;
    private Button downloadButton;
    private Button checkmarkButton;

    // OCLs
    private View.OnClickListener mustacheButtonOCL = new View.OnClickListener() {
        private boolean active = false;

        @Override
        public void onClick(View view) {
            if (!active) {
                Log.d(TAG, "Opening mustache menu...");
            } else {
                Log.d(TAG, "Closing mustache menu...");
            }
            active = !active;
        }
    };

    private View.OnClickListener paintbrushButtonOCL = new View.OnClickListener() {
        private boolean active = false;

        @Override
        public void onClick(View view) {
            if (!active) {
                Log.d(TAG, "Opening paintbrush menu...");
            } else {
                Log.d(TAG, "Closing paintbrush menu...");
            }
            active = !active;
        }
    };

    private View.OnClickListener retakeButtonOCL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };


    private View.OnClickListener downloadButtonOCL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Downloading image...");
        }
    };

    private View.OnClickListener checkmarkButtonOCL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = getIntent();
            setResult(RESULT_OK, intent);
            saveUserDrawingToBitmap();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);

        // get layout objects
        drawer = (Drawer) findViewById(R.id.edit_picture_drawer);
        mustacheButton = (Button) findViewById(R.id.mustache_button);
        paintbrushButton = (Button) findViewById(R.id.paintbrush_button);
        retakeButton = (Button) findViewById(R.id.retake_button);
        downloadButton = (Button) findViewById(R.id.download_button);
        checkmarkButton = (Button) findViewById(R.id.checkmark_button);

        // setup layout objects
        mustacheButton.setOnClickListener(mustacheButtonOCL);
        paintbrushButton.setOnClickListener(paintbrushButtonOCL);
        retakeButton.setOnClickListener(retakeButtonOCL);
        downloadButton.setOnClickListener(downloadButtonOCL);
        checkmarkButton.setOnClickListener(checkmarkButtonOCL);

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
            Uri bitmapUri = intent.getParcelableExtra(TakePictureActivity.AUTOPOLO_IMAGE_URI);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), bitmapUri);
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return;
            }
        } else {
            Log.d(TAG, "Error: expected intent data was not received");
        }

        // setup drawer
        if (bitmap != null) {
            drawer.setBackground(new BitmapDrawable(bitmap));
        }
        if (savedInstanceState != null && lineListBackup != null) {
            drawer.setLineList(lineListBackup);
            drawer.invalidate();
        } else if (savedInstanceState == null) {
            lineListBackup = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        lineListBackup = drawer.getLineList();
    }

    private void saveUserDrawingToBitmap() {
        //Canvas canvas = drawer.getCanvasOverlay();
        Bitmap bmpBase = drawer.getCanvasOverlayBitmap();
        // Save Bitmap to File
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(TakePictureActivity.AUTOPOLO_IMAGE_FILENAME);
            bmpBase.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();
            fos = null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
