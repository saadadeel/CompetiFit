package com.project.saadadeel.CompetiFit.RunTracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.R;
//import com.project.saadadeel.CompetiFit.ScrollingPageAdapter.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.RunTracker.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.SlidingTabLayout;
import com.project.saadadeel.CompetiFit.connection.Runs;
import com.project.saadadeel.CompetiFit.connection.User;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by saadadeel on 21/01/2016.
 */
public class Pop extends AppCompatActivity{

    TextView textLong;
    TextView textLat;
    TextView textDistance;
    TextView timePassed;
    int distanceTravelled;
    int time = 0;
    boolean isPlaying = false;

    Location newLocation = new Location(LocationManager.GPS_PROVIDER);
    Location oldLocation = new Location(LocationManager.GPS_PROVIDER);
    LocationManager lm;
    LocationListener ll;

    Timer timer = new Timer();

    Boolean turn = false;
    String username;
    User usr;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_running);
        textLat = (TextView) findViewById(R.id.DistanceText);
        textDistance = (TextView) findViewById(R.id.dist);
        timePassed = (TextView) findViewById(R.id.timmer);

        Intent intent = getIntent();
        User user= intent.getExtras().getParcelable("user");
        this.usr = (user);

        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isPlaying) {
                    isPlaying = false;
                    stopTracker();
                }else{
                    isPlaying = true;
                    runTracker();
                }
            }
        });
    }

    public void runTracker() {
        System.out.println(usr.getUsername() + "/////");
        button.setBackgroundResource(android.R.drawable.radiobutton_off_background);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener();

        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, ll);
        } catch (SecurityException e) {
            System.out.println("Get Permission Please");
        }

        TimerTask secondCounter = new TimerTask() {
            @Override
            public void run() {
                time++;
                System.out.println("Hi!");
                mHandler.obtainMessage(1).sendToTarget();
            }
        };
        timer.scheduleAtFixedRate(secondCounter, 1000, 1000);
//        timePassed.setText(Double.toString(time) + "sec");
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            timePassed.setText(Double.toString(time) + "sec"); //this is the textview
        }
    };

    public void stopTracker() {
        timer.cancel();
        button.setBackgroundResource(android.R.drawable.ic_media_play);
        System.out.println("Stop tracker");

        textLat.setText("hello");
        textDistance.setText("4 pts.");
        timePassed.setText("Run completed");

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

                textDistance.setText(Double.toString(Math.round(distanceTravelled)) + "m...");
                oldLocation.set(newLocation);
            }

            else{
                if(isPlaying = true) {
                    newLocation.set(location);
                    oldLocation.set(newLocation);
                    distanceTravelled += newLocation.distanceTo(oldLocation);
                    textDistance.setText(Double.toString(Math.round(distanceTravelled)) + "m...");
                    turn = true;
                }
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
