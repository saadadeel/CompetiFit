
package com.project.saadadeel.CompetiFit.RunTracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;
//import com.project.saadadeel.CompetiFit.ScrollingPageAdapter.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.SlidingTabLayout;
import com.project.saadadeel.CompetiFit.UserMain;
import com.project.saadadeel.CompetiFit.ViewGenerator.*;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;
import android.support.v4.app.Fragment;

import org.json.JSONException;
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
public class Pop extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView textLong;
    TextView textLat;
    TextView textDistance;
    TextView timePassed;
    TextView speed;
    Double distanceTravelled = 0.00;
    int time = 0;
    Double speedTravelling;
    boolean isPlaying = false;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Run", "Map"};
    int Numboftabs = 2;

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
    public SharedPreferences sharedPreferences;
    public String myPref = "myPref";

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public boolean isMoving = true;
    Gson gson = new Gson();
    private GoogleMap googleMap;

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
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setMyLocationEnabled(true);

//            setToCurrent();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
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
            System.out.println("YOOOOOOO this is a race    " + this.race.getCUsername());
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
//                    runTracker();
                    mGoogleApiClient.connect();
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

//        stopLocationListener();
        stopLocationUpdates();
        this.usr.setRuns(this.runs);
//        Runs run = new Runs(this.distanceTravelled, this.speedTravelling, this.username);
        Runs run = new Runs(7000.00, 4.778, this.username);
//        run.setDate(date1);

        String text = run.getScore() + "pts.";
        textDistance.setText(text);
        timePassed.setText("Run completed");
        speed.setText("....");
        button.setText("back to home");


        this.intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);
        String token= this.sharedPreferences.getString("TOKEN", "");

        if(isNetworkAvailable()){
            if(this.isRace){
//               this.race.setComplete(this.distanceTravelled, this.speedTravelling);
               this.race.setComplete(6000.0, 6.1);
                this.usr.updateRaces(this.race);
                DBConnect db = new DBConnect(race, token);
                db.post("/activity/completeRace");
            }else{
                run.setIsSynced(0);
//                this.usr.addRun(run);
                DBConnect db = new DBConnect(run,token);
                db.post("/activity/Run");
            }

        }else{
            this.usr.setSynced(1);
            run.setIsSynced(1);
            this.usr.addRun(run);
            System.out.println(this.usr.getSynced());

//            intent.putExtra("user", this.usr);
//            intent.putExtra("race", this.usr.getRaces());
//            intent.putExtra("runs", this.usr.getRuns());
//            intent.putExtra("league", this.usr.getleague());

            SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
            String json = gson.toJson(this.usr);
            prefsEditor.putString("user", json);
            prefsEditor.commit();
            System.out.println("Offline mode");
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                Pop.this.finish();

                Intent intent = new Intent(this, UserMain.class);
                intent.putExtra("username", this.username);
                startActivity(intent);

                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        moveTaskToBack(true);
        Pop.this.finish();
        Intent intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            System.out.println("LOCATION: " + mLastLocation);
        }
        Intent intent = new Intent( this, ActivityRecognitionService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mGoogleApiClient, 3000, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && turn) {
            newLocation.set(location);
            Double temp = Double.valueOf(newLocation.distanceTo(oldLocation));
            if(isMoving){
                distanceTravelled+=temp;
            }
            textDistance.setText(this.RoundTo2Decimals(distanceTravelled / 1000) + "km");
            oldLocation.set(newLocation);
            speedTravelling = (distanceTravelled/time)*3.6;
            if(speedTravelling<0.5){
                speed.setText("--");
            }else{
                speed.setText(this.RoundTo2Decimals(speedTravelling) + "km/hr");
            }
            System.out.println("HEEEELLLOOOO    "  + isMoving + "     " + temp);
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
        mGoogleApiClient.disconnect();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .build();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
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

    public void setAdapter() {
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, usr);

            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.pager1);
            pager.setAdapter(adapter);

            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) findViewById(R.id.tabs1);
            tabs.setDistributeEvenly(true);


            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.colorForeground);
                }
            });

            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
    }

//    public void setToCurrent(){
//        googleMap.setMyLocationEnabled(true);
//
//        Location location = googleMap.getMyLocation();
//
//        if (location != null) {
//            Location myLocation = new LatLng(location.getLatitude(),
//                    location.getLongitude());
//        }
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
//                Constants.MAP_ZOOM));
//    }
}