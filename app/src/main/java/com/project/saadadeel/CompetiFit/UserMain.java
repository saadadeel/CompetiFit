package com.project.saadadeel.CompetiFit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.connection.DBGetter;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserMain extends AppCompatActivity implements DBResponse {
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"League", "Activity", "Races"};
    int Numboftabs = 3;
    Timer timer = new Timer();

    String username;
    User usr;
    boolean isRefresher = false;
    boolean isDataSynced = true;

    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_main);

        Intent intent = getIntent();
//        String username = intent.getExtras().getString("username");
//        setUsername(username);

        this.sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME",null);
        setUsername(username);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (intent.getExtras().containsKey("user")) {
            System.out.println("int contains user");
            this.usr = intent.getExtras().getParcelable("user");
            this.usr.setUserLeague(intent.getExtras().<minimalUser>getParcelableArrayList("league"));
            this.usr.setRaces(intent.getExtras().<Races>getParcelableArrayList("race"));
            this.usr.setRuns(intent.getExtras().<Runs>getParcelableArrayList("runs"));
            this.isDataSynced = false;
            setAdapter();
        } else {
            DBGetter dbGetter = new DBGetter("/user/details/" + getUsername());
            dbGetter.delegate = this;
            dbGetter.execute();
        }
        initiateRefresher();
    }


    public void goToSearch(View view) {
        Intent intent = new Intent(this, search.class);
        startActivity(intent);
    }

    public void initiateRefresher() {
        final DBGetter dbGetter = new DBGetter("/user/details/" + getUsername());
        dbGetter.delegate = this;
        TimerTask secondCounter = new TimerTask() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {
                    isRefresher = true;
                    System.out.println("Here is the sync status " + usr.getSynced());
                    if (!isDataSynced) {
                        System.out.println("data is being synced");
                        syncRuns();
                        isDataSynced = true;
                        usr.setSynced(0);
                    } else {
                        dbGetter.execute();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(secondCounter, 3 * 60 * 1000, 3 * 60 * 1000);
    }

    public void refresh(View view) {
        final DBGetter dbGetter = new DBGetter("/user/details/" + getUsername());
        dbGetter.delegate = this;
        System.out.println("The user sync is : " + usr.getSynced());

        if (isNetworkAvailable()) {
            if (!isDataSynced) {
                System.out.println("update /////////////////// yo");
                this.syncRuns();
                usr.setSynced(0);
            } else {
                dbGetter.execute();
            }
        } else {
            Toast.makeText(this, "No Internet Connection Available",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void syncRuns() {
        for (Runs run : this.usr.getRuns()) {
            System.out.println("Sync status for this run: " + run.getIsSynced());
            if (run.getIsSynced() == 1) {
                run.setIsSynced(0);
                Runs r = new Runs(run.getDistance(), run.getSpeed(), usr.getUsername());
                System.out.println("Run synced");
                DBConnect db = new DBConnect(r);
                db.post("/activity/Run");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                // About option clicked.
                return true;
            case R.id.action_changePassword:
                // Exit option clicked.
                return true;
            case R.id.action_about:
                // Settings option clicked.
                return true;
            case R.id.action_tutorial:
                // Settings option clicked.
                return true;
            case R.id.action_logout:
                SharedPreferences.Editor editor = this.sharedPreferences.edit();
                editor.clear();
                editor.commit();
                System.out.println("logout");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUser(User u) {
        this.usr = u;
    }

    public void setUsername(String un) {
        this.username = un;
    }

    public String getUsername() {
        return this.username;
    }

    public void setAdapter() {

        if (isRefresher) {
            adapter.setArgs(usr);
        } else {
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, usr);

            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);

            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
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

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserMain.this, Pop.class);
                    intent.putExtra("user", usr);
                    intent.putExtra("runs", usr.getRuns());
                    intent.putExtra("race", usr.getRaces());
                    intent.putExtra("league", usr.getUserLeague());
                    intent.putExtra("isRace", false);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void processFinish(User u) {
        this.setUser(u);
        setAdapter();
    }
}