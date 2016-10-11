package com.example.diaz.alejandro.nicolas.safefriends.geofencing;

import com.google.android.gms.location.Geofence;

/**
 * Created by Lenwe on 10/10/2016.
 */

public class SimpleGeofence {
    private final String mId;   //request id
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private long mExpirationDuration;
    private int mTransitionType;

    public SimpleGeofence(String geofenceId, double latitude, double longitude, float radius,
                          long expiration, int transition) {
        // Set the instance fields from the constructor.
        this.mId = geofenceId;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRadius = radius;
        this.mExpirationDuration = expiration;
        this.mTransitionType = transition;
    }

    // Instance field getters.
    public String getId() {
        return mId;
    }
    public double getLatitude() {
        return mLatitude;
    }
    public double getLongitude() {
        return mLongitude;
    }
    public float getRadius() {
        return mRadius;
    }
    public long getExpirationDuration() {
        return mExpirationDuration;
    }
    public int getTransitionType() {
        return mTransitionType;
    }

    public Geofence toGeofence(){
        return new Geofence.Builder()
                .setRequestId(mId)
                .setTransitionTypes(mTransitionType)
                .setCircularRegion(mLatitude,mLongitude,mRadius)
                .setExpirationDuration(mExpirationDuration)
                .build();
    }
}
