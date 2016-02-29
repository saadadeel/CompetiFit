package com.project.saadadeel.CompetiFit.RunTracker;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by saadadeel on 20/01/2016.
 */
public class runningDistance {

    long runLat;
    long runLong;
    Context mContext;

    public runningDistance(Context mContext){

        this.mContext = mContext;
        LocationManager lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

    }



}
