package com.ci.marcopolo;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Alex on 3/11/15.
 */
public class AutoPoloActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public final static String TAG = "AutoPoloActivity";

    // callback codes
    private final static int RECORD_AUDIO = 0;
    private final static int TAKE_PICTURE = 1;

    // the current user's account info
    private String user_data;

    // Google API client
    private GoogleApiClient googleApiClient;

    // last location for auto-polo
    private Location lastLocation;

    // layout objects
    private TextView backButton;
    private TextView audioButton;
    private TextView cameraButton;
    private TextView placeButton;

    // OCLs
    private View.OnClickListener backOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener audioButtonOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Setting an audio Auto-Polo");
            Intent intent = new Intent(getApplicationContext(), RecordAudioActivity.class);
            intent.putExtra("user_data", user_data);
            startActivityForResult(intent, RECORD_AUDIO);
        }
    };

    private View.OnClickListener cameraButtonOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Setting an image Auto-Polo");
            Intent intent = new Intent(getApplicationContext(), TakePictureActivity.class);
            intent.putExtra("user_data", user_data);
            startActivityForResult(intent, TAKE_PICTURE);
        }
    };

    private View.OnClickListener placeButtonOCL = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "Setting a place Auto-Polo");

            if (lastLocation != null) {
                Log.d(TAG, "Longitude is " + lastLocation.getLongitude());
                Log.d(TAG, "Latitude is " + lastLocation.getLatitude());

                Toast.makeText(AutoPoloActivity.this, "Longitude is " + lastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                Toast.makeText(AutoPoloActivity.this, "Latitude is " + lastLocation.getLatitude(), Toast.LENGTH_SHORT).show();

            } else {
                Log.d(TAG, "The location hasn't been determined yet");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autopolo);

        user_data = getIntent().getStringExtra("user_data");

        // setup the google API client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        // get layout objects
        backButton = (TextView) findViewById(R.id.back);
        audioButton = (TextView) findViewById(R.id.autopolo_audio);
        cameraButton = (TextView) findViewById(R.id.autopolo_camera);
        placeButton = (TextView) findViewById(R.id.autopolo_place);

        // setup layout objects
        backButton.setOnClickListener(backOCL);
        audioButton.setOnClickListener(audioButtonOCL);
        cameraButton.setOnClickListener(cameraButtonOCL);
        placeButton.setOnClickListener(placeButtonOCL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RECORD_AUDIO) {
                String autopoloAudio = data.getStringExtra("autopolo_audio");
                Log.d(TAG, "Audio saved to " + autopoloAudio);
            } else if (requestCode == TAKE_PICTURE) {
                String autopoloImage = data.getStringExtra("autopolo_image");
                Log.d(TAG, "Image saved to " + autopoloImage);
            }
        } else {
            Log.d(TAG, "An intent was cancelled");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {
            Log.d(TAG, "Longitude is " + lastLocation.getLongitude());
            Log.d(TAG, "Latitude is " + lastLocation.getLatitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection to the Google API has been interrupted");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection to the Google API failed");
        Log.d(TAG, connectionResult.toString());
        Log.d(TAG, "Error code: " + connectionResult.getErrorCode());
    }
}
