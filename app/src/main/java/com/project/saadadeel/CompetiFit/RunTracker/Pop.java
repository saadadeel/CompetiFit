
package com.project.saadadeel.CompetiFit.RunTracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;
//import com.project.saadadeel.CompetiFit.ScrollingPageAdapter.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.UserMain;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_running);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        textDistance = (TextView) findViewById(R.id.dist);
        timePassed = (TextView) findViewById(R.id.time);
        speed = (TextView) findViewById(R.id.speed);

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
    }

//    public void runTracker() {
//        button.setText("Waiting for connection.....");
//        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        ll = new LocationListener();
//        try {
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, ll);
//        } catch (SecurityException e) {
//            System.out.println("Get Permission Please");
//        }
//    }

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

//        stopLocationListener();
        stopLocationUpdates();
        this.usr.setRuns(this.runs);
//        Runs run = new Runs(this.distanceTravelled, this.speedTravelling, this.username);
        Runs run = new Runs(10000.00, 3.778, this.username);
        run.setDate(date1);

        String text = run.getScore() + "pts.";
        textDistance.setText(text);
        timePassed.setText("Run completed");
        speed.setText("....");
        button.setText("back to home");


        this.intent = new Intent(this, UserMain.class);
        intent.putExtra("username", this.username);

        if(isNetworkAvailable()){
            if(this.isRace){
//               this.race.setComplete(this.distanceTravelled, this.speedTravelling);
               this.race.setComplete(6000.0, 4.0);
                this.usr.updateRaces(this.race);
                DBConnect db = new DBConnect(race);
                db.post("/activity/completeRace");
            }else{
                run.setIsSynced(0);
                this.usr.addRun(run);
                DBConnect db = new DBConnect(run);
                db.post("/activity/Run");
            }

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

//    private void stopLocationListener() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        lm.removeUpdates(ll);
//        return;
//    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    class LocationListener implements android.location.LocationListener {
//        @Override
//        public void onLocationChanged(Location location) {
//            if (location != null && turn) {
//                newLocation.set(location);
//                Double temp = Double.valueOf(newLocation.distanceTo(oldLocation));
//
//                distanceTravelled+=temp;
//                textDistance.setText(this.RoundTo2Decimals(distanceTravelled / 1000) + "km");
//
//                oldLocation.set(newLocation);
//
//                speedTravelling = (distanceTravelled/time)*3.6;
//                speed.setText(this.RoundTo2Decimals(speedTravelling) + "km/hr");
//            }
//            else{
//                if(isPlaying = true) {
//                    button.setText("Stop");
//                    newLocation.set(location);
//                    oldLocation.set(newLocation);
//
//                    Double temp = Double.valueOf(newLocation.distanceTo(oldLocation));
//                    distanceTravelled+=temp;
//
//                    textDistance.setText(this.RoundTo2Decimals(distanceTravelled) + "km");
//                    speedTravelling = distanceTravelled/time;
//
//                    speed.setText((Double.toString(Math.round(speedTravelling)) + "km/hr"));
//                    turn = true;
//
//                    TimerTask secondCounter = new TimerTask() {
//                        @Override
//                        public void run() {
//                            time++;
//                            mHandler.obtainMessage(1).sendToTarget();
//                        }
//                    };
//                    timer.scheduleAtFixedRate(secondCounter, 1000, 1000);
//                }
//            }
//        }
//
//        double RoundTo2Decimals(double val) {
//            if(val<1000) {
//                System.out.println(val);
//                DecimalFormat df2 = new DecimalFormat("#.00");
//                System.out.println("here is the new format" + df2.format(val));
//
//                return Double.valueOf(df2.format(val));
//            }
//            return 0.0;
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    }

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
        Intent i = new Intent(this, ActivityRecognitionIntentService.class);
        final PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                mGoogleApiClient,
                1000 /* detection interval */,
                pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && turn) {
            newLocation.set(location);
            Double temp = Double.valueOf(newLocation.distanceTo(oldLocation));
            if(temp>3){
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
            System.out.println("HEEEELLLOOOO    "  + temp);
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
    class ActivityRecognitionIntentService extends IntentService {
        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public ActivityRecognitionIntentService(String name) {
            super(name);
        }
        //..
        /**
         * Called when a new activity detection update is available.
         */
        @Override
        protected void onHandleIntent(Intent intent) {
            //...
            // If the intent contains an update
            if (ActivityRecognitionResult.hasResult(intent)) {
                // Get the update
                ActivityRecognitionResult result =
                        ActivityRecognitionResult.extractResult(intent);

                DetectedActivity mostProbableActivity
                        = result.getMostProbableActivity();

                // Get the confidence % (probability)
                int confidence = mostProbableActivity.getConfidence();

                // Get the type
                int activityType = mostProbableActivity.getType();
           /* types:
            * DetectedActivity.IN_VEHICLE
            * DetectedActivity.ON_BICYCLE
            * DetectedActivity.ON_FOOT
            * DetectedActivity.STILL
            * DetectedActivity.UNKNOWN
            * DetectedActivity.TILTING
            */
                // process
//                if(activityType == DetectedActivity.STILL ){
//                   System.out.println("YOOOOOOO");
//                }
                System.out.println("YOOOOOOO     " + activityType);
            }
        }
    }
}