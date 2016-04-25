
package com.project.saadadeel.CompetiFit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewPagerAdapter;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.connection.DBGetter;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

import java.util.Timer;
import java.util.TimerTask;


public class UserMain extends AppCompatActivity implements DBResponse {
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"League", "Activity", "Races"};
    int Numboftabs = 3;
    Timer timer = new Timer();

    String username;
    User usr;
    boolean isDataSynced = true;

    Context context;

    public SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    String token = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_user_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        this.sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        this.token= this.sharedPreferences.getString("TOKEN", "");
        this.username = sharedPreferences.getString("USERNAME", "");

        if(isInternetAvailable()) {
            DBGetter dbGetter = new DBGetter("/user/" + getUsername(), this.token);
            dbGetter.delegate = this;
            dbGetter.execute();
        }else{
            String json = this.sharedPreferences.getString("user", "");
            this.usr = gson.fromJson(json, User.class);
            this.isDataSynced = false;
            setAdapter();
        }
        initiateRefresher();
    }

    public void goToSearch(View view) {
        Intent intent = new Intent(this, search.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void initiateRefresher() {
        TimerTask secondCounter = new TimerTask() {
            @Override
            public void run() {
            if (isInternetAvailable()) {
                DBGetter dbGetter = new DBGetter("/user/" + getUsername());
                dbGetter.delegate = UserMain.this;
                dbGetter.execute();
            }
            }
        };
        timer.scheduleAtFixedRate(secondCounter, 30 * 60 * 1000, 30 * 60 * 1000);
    }

    public void refresh() {
        final DBGetter dbGetter = new DBGetter("/user/" + getUsername(),this.token);
        dbGetter.delegate = this;
        System.out.println("The user sync is : " + usr.getSynced());

        if (isInternetAvailable()) {
            if (!isDataSynced) {
                System.out.println("update /////////////////// yo");
                this.syncRuns();
                usr.setSynced(0);
            }
                dbGetter.execute();

        } else {
            Toast.makeText(this, "No Internet Connection Available",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        System.out.println("onResume");
    }

    public void syncRuns() {
        for (Runs run : this.usr.getRuns()) {
            System.out.println("Sync status for this run: " + run.getIsSynced());
            if (run.getIsSynced() == 1) {
                run.setIsSynced(0);
                Runs r = new Runs(run.getDistance(), run.getSpeed(), usr.getUsername());
                System.out.println("Run synced");
                DBConnect db = new DBConnect(token);
                db.post("/user/"+this.usr.getUsername()+"/run",r);
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
                Intent i = new Intent(this, profile.class);
                startActivity(i);
                return true;
            case R.id.action_tutorial:
                Intent tutorialIntent = new Intent(this, Tutorial.class);
                startActivity(tutorialIntent);
                return true;
            case R.id.action_logout:
                SharedPreferences.Editor editor = this.sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(this, login.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                this.refresh();
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
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);

            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true);
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.colorForeground);
                }
            });
            tabs.setViewPager(pager);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserMain.this, Pop.class);
                    intent.putExtra("isRace", false);
                    UserMain.this.finish();
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
    public void processFinish(String data) {
        User u = new Gson().fromJson(data, User.class);
        this.setUser(u);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        String json = gson.toJson(u);
        prefsEditor.putString("user", json);
        prefsEditor.commit();
        setAdapter();
    }
}