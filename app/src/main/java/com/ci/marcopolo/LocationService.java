package com.ci.marcopolo;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Alex on 4/14/15.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public final static String TAG = "LocationServicesAct";

    // callback
    private LocationCallback callback;

    // intent
    private Intent intent;

    // Google API client
    private GoogleApiClient googleApiClient;

    // last location for auto-polo
    private Location lastLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // setup the google API client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onDestroy() {

    }

    public void setLocationCallback(LocationCallback callback) {
        this.callback = callback;
    }

    private void fail(String error) {
        callback.onFail(error);
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {
            Log.d(TAG, "Longitude is " + lastLocation.getLongitude());
            Log.d(TAG, "Latitude is " + lastLocation.getLatitude());

            callback.onSuccess(lastLocation);
        } else {
            fail("Location is null");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult.toString());
        fail(connectionResult.toString());
    }
}
