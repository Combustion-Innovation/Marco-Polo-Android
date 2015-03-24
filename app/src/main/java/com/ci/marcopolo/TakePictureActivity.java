package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Alex on 3/16/15.
 */
public class TakePictureActivity extends Activity {
    public final static String TAG = "TakePictureActivity";

    // callback codes
    public final static int EDIT_PICTURE = 0;

    // camera objects
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean cameraConfigured = false;
    private boolean inPreview = false;

    // layout objects
    private TextView backButton;
    private Button captureButton;

    // OCLs
    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };

    // OTLs
    private View.OnTouchListener captureButtonOTL = new View.OnTouchListener() {
        long pressedTime = 0;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                pressedTime = System.currentTimeMillis();
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                long clickTime = System.currentTimeMillis() - pressedTime;
                if (clickTime < 1000) {
                    capturePhotoForEditing();
                }
            }

            return false;
        }
    };

    // OLCLs
    private View.OnLongClickListener captureButtonOLCL = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            captureVideo();

            return true;
        }
    };

    private void capturePhotoForEditing() {
        Log.d(TAG, "Capturing a photo...");
        camera.takePicture(null,
                null,
                new Camera.PictureCallback() {   // jpeg
                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        Log.d(TAG, "onPictureTaken (jpeg)");
                        Bitmap bitmapPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp_autopolo_image.jpeg";
                        saveImage(bitmapPicture, imagePath);

                        Intent intent = new Intent(getApplicationContext(), EditPictureActivity.class);
                        intent.putExtra("autopolo_image", imagePath);
                        startActivityForResult(intent, EDIT_PICTURE);
                    }
                });
    }

    private void captureVideo() {
        Log.d(TAG, "Capturing video...");
    }

    private void saveImage(Bitmap bitmap, String path) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        // get layout objects
        backButton = (TextView) findViewById(R.id.back);
        captureButton = (Button) findViewById(R.id.take_picture_capture_button);

        // setup layout objects
        backButton.setOnClickListener(backOCL);
        captureButton.setOnTouchListener(captureButtonOTL);
        captureButton.setOnLongClickListener(captureButtonOLCL);

        // setup camera
        surfaceView = (SurfaceView) findViewById(R.id.take_picture_camera_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            camera = Camera.open();
            startPreview();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            Toast.makeText(TakePictureActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        if (inPreview) {
            camera.stopPreview();
        }

        camera.release();
        camera = null;
        inPreview = false;

        super.onPause();
    }

    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return (result);
    }

    private void startPreview() {
        try {
            if (cameraConfigured && camera != null) {
                camera.startPreview();
                inPreview = true;
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            Toast.makeText(TakePictureActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            // the preview is rotated in portrait without this
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                camera.setDisplayOrientation(90);
            } else {
                camera.setDisplayOrientation(0);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolderLocal, int format, int width, int height) {
            if (surfaceHolder.getSurface() != null) {
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    Toast.makeText(TakePictureActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                if (!cameraConfigured) {
                    Camera.Parameters parameters = camera.getParameters();
                    Camera.Size size = getBestPreviewSize(width, height, parameters);

                    if (size != null) {
                        parameters.setPreviewSize(size.width, size.height);
                        camera.setParameters(parameters);
                        cameraConfigured = true;
                    }
                }
            }

            startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    };
}
