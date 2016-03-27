package com.project.saadadeel.CompetiFit.RunTracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;
//import com.project.saadadeel.CompetiFit.ScrollingPageAdapter.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.UserMain;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * Created by saadadeel on 21/01/2016.
 */
public class Pop extends AppCompatActivity {

    TextView textLong;
    TextView textLat;
    TextView textDistance;
    TextView timePassed;
    TextView speed;
    Double distanceTravelled = 0.00;
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

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_running);

        textDistance = (TextView) findViewById(R.id.dist);
        timePassed = (TextView) findViewById(R.id.time);
        speed = (TextView) findViewById(R.id.speed);

        Intent intent = getIntent();
        this.isRace = intent.getBooleanExtra("isRace", false);
        this.usr = intent.getExtras().getParcelable("user");
        this.username = this.usr.getUsername();
        this.usr.setUserLeague(intent.getExtras().<minimalUser>getParcelableArrayList("league"));
        this.usr.setRaces(intent.getExtras().<Races>getParcelableArrayList("race"));
        this.usr.setRuns(intent.getExtras().<Runs>getParcelableArrayList("runs"));
        this.runs = this.usr.getRuns();

        if(this.isRace){
            this.race = intent.getExtras().getParcelable("Race");
        }

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isPlaying) {
                    isPlaying = false;
                    stopTracker();
                } else {
                    isPlaying = true;
                    runTracker();
                }
            }
        });
    }

    public void runTracker() {
        button.setText("Waiting for connection.....");
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener();
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ll);
        } catch (SecurityException e) {
            System.out.println("Get Permission Please");
        }
    }

    public Handler mHandler = new Handler() {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public void handleMessage(Message msg) {
            int hr = time / 3600;
            int rem = time % 3600;
            int mn = rem / 60;
            int sec = rem % 60;
            String hrStr = (hr < 10 ? "0" : "") + hr;
            String mnStr = (mn < 10 ? "0" : "") + mn;
            String secStr = (sec < 10 ? "0" : "") + sec;

            timePassed.setText(hrStr + ":" + mnStr + ":" + secStr);
        }
    };

    public void stopTracker() {
        timer.cancel();
        button.setText("Stop");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String date1 = dateFormat.format(date);

        stopLocationListener();
        this.usr.setRuns(this.runs);
//        Runs run = new Runs(this.distanceTravelled, this.speedTravelling, this.username);
        Runs run = new Runs(14000.00, 6.778, this.username);
        run.setDate(date1);

        String text = run.getScore() + "pts.";
        textDistance.setText(text);
        timePassed.setText("Run completed");
        speed.setText("....");
        button.setText("back to home");


        this.intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);

        if(isNetworkAvailable()){
            run.setIsSynced(0);
            this.usr.addRun(run);
            DBConnect db = new DBConnect(run);
            db.post("/activity/Run");
        }else{
            this.usr.setSynced(1);
            run.setIsSynced(1);
            this.usr.addRun(run);
            System.out.println(this.usr.getSynced());

            intent.putExtra("user", this.usr);
            intent.putExtra("race", this.usr.getRaces());
            intent.putExtra("runs", this.usr.getRuns());
            intent.putExtra("league", this.usr.getleague());
            System.out.println("Offline mode");
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    private void stopLocationListener() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.removeUpdates(ll);
        return;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class LocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null && turn) {
                newLocation.set(location);
                Double temp = Double.valueOf(newLocation.distanceTo(oldLocation));

                distanceTravelled+=temp;
                textDistance.setText(this.RoundTo2Decimals(distanceTravelled / 1000) + "km");

                oldLocation.set(newLocation);

                speedTravelling = (distanceTravelled/time)*3.6;
                speed.setText(this.RoundTo2Decimals(speedTravelling) + "km/hr");
            }
            else{
                if(isPlaying = true) {
                    button.setText("Stop");
                    newLocation.set(location);
                    oldLocation.set(newLocation);

                    Double temp = Double.valueOf(newLocation.distanceTo(oldLocation));
                    distanceTravelled+=temp;

                    textDistance.setText(this.RoundTo2Decimals(distanceTravelled) + "km");
                    speedTravelling = distanceTravelled/time;

                    speed.setText((Double.toString(Math.round(speedTravelling)) + "km/hr"));
                    turn = true;

                    TimerTask secondCounter = new TimerTask() {
                        @Override
                        public void run() {
                            time++;
                            mHandler.obtainMessage(1).sendToTarget();
                        }
                    };
                    timer.scheduleAtFixedRate(secondCounter, 1000, 1000);
                }
            }
        }

        double RoundTo2Decimals(double val) {
            if(val<1000) {
                System.out.println(val);
                DecimalFormat df2 = new DecimalFormat("#.00");
                System.out.println("here is the new format" + df2.format(val));

                return Double.valueOf(df2.format(val));
            }
            return 0.0;
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
