package com.project.saadadeel.CompetiFit.RunTracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.R;

public class Running extends Fragment {

    TextView textLong;
    TextView textLat;
    TextView textDistance;
    Location newLocation = new Location(LocationManager.GPS_PROVIDER);
    Location oldLocation = new Location(LocationManager.GPS_PROVIDER);
    float distanceTravelled;
    Boolean turn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_running, container, false);

        textDistance = (TextView) v.findViewById(R.id.dist);
        runTracker();
        return v;
    }

    public void runTracker() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                distanceTravelled+=newLocation.distanceTo(oldLocation);

                textDistance.setText(Double.toString(distanceTravelled));
                oldLocation.set(newLocation);
            }

            else{
                newLocation.set(location);
                oldLocation.set(newLocation);

                distanceTravelled+=newLocation.distanceTo(oldLocation);

                System.out.println(distanceTravelled);

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
