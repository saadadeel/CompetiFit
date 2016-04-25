
package com.project.saadadeel.CompetiFit.RunTracker;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.R;
//import com.project.saadadeel.CompetiFit.ScrollingPageAdapter.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.SlidingTabLayout;
import com.project.saadadeel.CompetiFit.UserMain;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.connection.DBResponse;
import com.project.saadadeel.CompetiFit.services.ActivityRecognitionService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by saadadeel on 21/01/2016.
 */
public class Pop extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, DBResponse {

    TextView textDistance;
    TextView timePassed;
    TextView speed;
    Button button;

    Double distanceTravelled = 0.00;
    int time = 0;
    Double speedTravelling;
    Timer timer = new Timer();

    boolean isPlaying = false;
    boolean turn = false;
    boolean isMoving = true;
    Boolean isRace;

    String username;
    User usr;
    ArrayList<Runs> runs;
    Races race;

    Intent intent;
    public SharedPreferences sharedPreferences;
    public String myPref = "myPref";

    private GoogleApiClient apiClient;
    private LocationRequest locationRequest;
    Location newLocation = new Location("");
    Location oldLocation;

    Gson gson = new Gson();
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_running);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textDistance = (TextView) findViewById(R.id.dist);
        timePassed = (TextView) findViewById(R.id.time);
        speed = (TextView) findViewById(R.id.speed);

        try {
            if (map == null) {
                map = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.setMyLocationEnabled(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                map.moveCamera(center);
                map.animateCamera(zoom);
            }
        });


        Intent intent = getIntent();
        this.isRace = intent.getBooleanExtra("isRace", false);

        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        this.sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = this.sharedPreferences.getString("user", "");
        this.usr = gson.fromJson(json, User.class);
        this.username = this.usr.getUsername();
        this.runs = this.usr.getRuns();

        if(this.isRace){
            TableRow row = (TableRow)findViewById(R.id.compRow);
            row.setVisibility(View.VISIBLE);

            String race = this.sharedPreferences.getString("compRace", "");
            this.race = gson.fromJson(race, Races.class);

            TextView comp = (TextView)findViewById(R.id.title);
            comp.setText("Target: \n" + this.race.getKMChallengedMiles() + " Km @ " + this.race.getKMChallengedSpeed() + " km/hr");
        }


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isPlaying) {
                    isPlaying = false;
                    stopTracker();
                } else {
                    isPlaying = true;
                    apiClient.connect();
                }
            }
        });

        buildGoogleApiClient();
        IntentFilter filter = new IntentFilter("action");
        this.registerReceiver(new Receiver(), filter);
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

        stopLocationUpdates();
        this.usr.setRuns(this.runs);
        Runs run = new Runs(this.distanceTravelled, this.speedTravelling/3.6, this.username);

        this.intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);
        String token= this.sharedPreferences.getString("TOKEN", "");

        if(isInternetAvailable()){
            if(this.isRace){
               this.race.setComplete(this.distanceTravelled, this.speedTravelling);
                this.usr.updateRaces(this.race);

                DBConnect db = new DBConnect(token);
                db.delegate = this;
                db.post("/user/"+username+"/completeRace", race);
            }else{
                DBConnect db = new DBConnect(token);
                db.delegate = this;
                db.post("/user/"+ this.usr.getUsername()+"/run",run);
            }

        }else{
            this.usr.setSynced(1);
            run.setIsSynced(1);
            this.usr.addRun(run);

            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            String json = gson.toJson(this.usr);
            prefsEditor.putString("user", json);
            prefsEditor.commit();
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    private boolean isInternetAvailable() {
        ConnectivityManager conManager =
                (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Pop.this.finish();
        Intent intent = new Intent(this, UserMain.class);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10000);

        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
        oldLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        if (oldLocation != null) {
            System.out.println("LOCATION: " + oldLocation);
            button.setText("Stop");
            TimerTask secondCounter = new TimerTask() {
                @Override
                public void run() {
                    time++;
                    mHandler.obtainMessage(1).sendToTarget();
                }
            };
            timer.scheduleAtFixedRate(secondCounter, 1000, 1000);

            Intent intent = new Intent( this, ActivityRecognitionService.class );
            PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(apiClient, 3000, pendingIntent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            newLocation.set(location);
            Double distInMeters = Double.valueOf(newLocation.distanceTo(oldLocation));
            oldLocation.set(newLocation);
            if(isMoving){
                distanceTravelled+=distInMeters;
            }
            textDistance.setText(this.RoundTo2Decimals(distanceTravelled / 1000) + "km");

            LatLng current = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
            LatLng old = new LatLng(oldLocation.getLatitude(), oldLocation.getLongitude());
            map.addPolyline((new PolylineOptions())
                    .add(old, current)
                    .width(6)
                    .color(Color.RED)
                    .visible(true));

            speedTravelling = (distanceTravelled/time)*3.6;
            if(speedTravelling<0.5 || !isMoving){
                speed.setText("--");
            }else{
                speed.setText(this.RoundTo2Decimals(speedTravelling) + "km/hr");
            }
        }
    }

    double RoundTo2Decimals(double val) {
        if(val<1000) {
            DecimalFormat df2 = new DecimalFormat("#.00");

            return Double.valueOf(df2.format(val));
        }
        return 0.0;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("The connection has failed");
        buildGoogleApiClient();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        apiClient.disconnect();
    }

    synchronized void buildGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);
    }

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            String movement = arg1.getExtras().getString("motion");
            System.out.println("RECIEVED   " + movement);
            if(movement.equals("STILL")){
                isMoving = false;
            }else{
                isMoving = true;
            }
        }
    }

    private void setPostRunDisplay(int score){
        String text = score + "pts.";
        textDistance.setText(text);
        timePassed.setText("Run completed");
        speed.setVisibility(View.GONE);
        button.setText("Back To Home");
    }

    @Override
    public void processFinish(String data) {
        if(this.isRace){
            textDistance.setText("Race completed");
            timePassed.setVisibility(View.GONE);
            speed.setVisibility(View.GONE);
            button.setText("Back To Home");
        }else{
            Runs run = new Gson().fromJson(data, Runs.class);
            setPostRunDisplay(run.getScore());
        }
    }
}