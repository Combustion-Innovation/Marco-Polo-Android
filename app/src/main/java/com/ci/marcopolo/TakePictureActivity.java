package com.ci.marcopolo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Alex on 3/16/15.
 */
public class TakePictureActivity extends Activity {
    public final static String TAG = "TakePictureActivity";

    // layout objects
    private TextView backButton;
    private Button captureButton;
    private TextureView cameraView;

    // OCLs
    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = getIntent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };

    private View.OnClickListener captureButtonOCL = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(TAG, "Capturing a photo...");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        // get layout objects
        backButton = (TextView) findViewById(R.id.back);
        captureButton = (Button) findViewById(R.id.take_picture_capture_button);
        cameraView = (TextureView) findViewById(R.id.take_picture_camera_view);

        // setup layout objects
        backButton.setOnClickListener(backOCL);
        captureButton.setOnClickListener(captureButtonOCL);

        // setup camera
        CameraManager cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
    }
}
