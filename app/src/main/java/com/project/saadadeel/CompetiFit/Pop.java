package com.project.saadadeel.CompetiFit;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

/**
 * Created by saadadeel on 21/01/2016.
 */
public class Pop extends Activity{

    TextView textLong;
    TextView textLat;
    TextView textDistance;
    Location newLocation = new Location(LocationManager.GPS_PROVIDER);
    Location oldLocation = new Location(LocationManager.GPS_PROVIDER);
    float distanceTravelled;
    Boolean turn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();

//      textLong = (TextView) getWindow().findViewById(R.id.longitude);
        textLat = (TextView) getWindow().findViewById(R.id.DistanceText);
        textDistance = (TextView) getWindow().findViewById(R.id.dist);
        runTracker();

    }

    public void setView(){
        setContentView(R.layout.pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));
    }

    public void runTracker() {
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new LocationListener();

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, ll);
        } catch (SecurityException e) {
            System.out.println("Get Permission Please");
        }
    }


    class LocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null && turn) {
                newLocation.set(location);

                double pLong = location.getLongitude();
                double pLat = location.getLatitude();

                textLat.setText(Double.toString(pLat));
               // textLong.setText(Double.toString(pLong));

                distanceTravelled+=newLocation.distanceTo(oldLocation);
//                float distance = location.distanceTo(Ilford);

                textDistance.setText(Double.toString(distanceTravelled));
                oldLocation.set(newLocation);
            }

            else{
                newLocation.set(location);
                oldLocation.set(newLocation);

                distanceTravelled+=newLocation.distanceTo(oldLocation);

                textDistance.setText(Double.toString(distanceTravelled));

                turn = true;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
