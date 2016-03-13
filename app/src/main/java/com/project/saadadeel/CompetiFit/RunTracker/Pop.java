package com.project.saadadeel.CompetiFit.RunTracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.R;
//import com.project.saadadeel.CompetiFit.ScrollingPageAdapter.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;

import java.util.ArrayList;
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
    TextView speed;
    int distanceTravelled;
    int time = 0;
    Double speedTravelling;
    boolean isPlaying = false;

    Location newLocation = new Location(LocationManager.GPS_PROVIDER);
    Location oldLocation = new Location(LocationManager.GPS_PROVIDER);
    LocationManager lm;
    LocationListener ll;

    Timer timer = new Timer();

    Boolean turn = false;
    String username;
    User usr;
    ArrayList<Runs> runs;
    Button button;

    Races race;
    Boolean isRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_running);
//        textLat = (TextView) findViewById(R.id.DistanceText);
        textDistance = (TextView) findViewById(R.id.dist);
        timePassed = (TextView) findViewById(R.id.timmer);
        speed = (TextView) findViewById(R.id.speed);

        Intent intent = getIntent();
        this.isRace = intent.getBooleanExtra("isRace", false);

        System.out.println(intent.getBooleanExtra("isRace", true) + "    fhlaiusfhiueshiue");
        this.usr= intent.getExtras().getParcelable("user");

        if(this.isRace){
            this.race = intent.getExtras().getParcelable("race");
            System.out.println("yoooooo");
        }
        else{
            this.runs = intent.getExtras().getParcelableArrayList("runs");
        }

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
        button.setText("Stop");
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
        button.setText("Stop");
        System.out.println("Stop tracker");

        textDistance.setText("4 pts.");
        timePassed.setText("Run completed");
        if(isRace){
            DBConnect db = new DBConnect(race);
            this.race.setComplete(distanceTravelled, speedTravelling);
            this.usr.updateRaces(this.race);
            db.post("/activity/completeRace");
        }else {
            DBConnect db = new DBConnect(this.usr);
            this.usr.setRuns(this.runs);
            this.usr.addRun(new Runs(18, 16, this.username));
            db.post("/activity/Run");
        }
    }

    class LocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            if(isRace=true){
                speed.setText("9km/hr");
            }

            if (location != null && turn) {
                newLocation.set(location);

                double pLong = location.getLongitude();
                double pLat = location.getLatitude();

                distanceTravelled+=newLocation.distanceTo(oldLocation);
                textDistance.setText(Double.toString(Math.round(distanceTravelled)) + "m");
                oldLocation.set(newLocation);
            }

            else{
                if(isPlaying = true) {
                    newLocation.set(location);
                    oldLocation.set(newLocation);
                    distanceTravelled += newLocation.distanceTo(oldLocation);
                    textDistance.setText(Double.toString(Math.round(distanceTravelled)) + "m");
                    speedTravelling = (double)distanceTravelled/time;
                    speed.setText((Double.toString(Math.round(speedTravelling)) + "m/s"));
                    turn = true;
                }
            }
        }

        public void setUpRace(){
            speed.setText("9km/hr");
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
