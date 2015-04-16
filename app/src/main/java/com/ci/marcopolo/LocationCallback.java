package com.ci.marcopolo;

import android.location.Location;

/**
 * Created by Alex on 4/14/15.
 */
public interface LocationCallback {
    void onSuccess(Location location);
    void onFail(String error);
}
