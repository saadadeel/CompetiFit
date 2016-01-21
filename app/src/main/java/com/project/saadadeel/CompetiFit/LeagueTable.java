package com.project.saadadeel.CompetiFit;

        import android.content.Context;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import org.w3c.dom.Text;

public class LeagueTable extends Fragment {

    TextView textLong;
    TextView textLat;
    TextView textIlford;
    Location newLocation = new Location(LocationManager.GPS_PROVIDER);
    Location oldLocation = new Location(LocationManager.GPS_PROVIDER);
    float distanceTravelled;
    Boolean turn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.league_table, container, false);

//        Ilford.setLongitude(0.0330);
//        Ilford.setLatitude(51.5250);

        textLong = (TextView) v.findViewById(R.id.longitude);
        textLat = (TextView) v.findViewById(R.id.latitude);
        textIlford = (TextView) v.findViewById(R.id.ilford);
        runTracker();
        return v;

    }

    public void runTracker() {
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                textLong.setText(Double.toString(pLong));

                distanceTravelled+=newLocation.distanceTo(oldLocation);
//                float distance = location.distanceTo(Ilford);

                textIlford.setText(Double.toString(distanceTravelled));
                oldLocation.set(newLocation);
            }

            else{
                newLocation.set(location);
                oldLocation.set(newLocation);

                distanceTravelled+=newLocation.distanceTo(oldLocation);

                textIlford.setText(Double.toString(distanceTravelled));

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

