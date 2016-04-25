package com.project.saadadeel.CompetiFit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewGenerator;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.connection.DBGetter;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by saadadeel on 15/03/2016.
 */
public class search extends AppCompatActivity implements DBResponse{

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private User u;

    public SharedPreferences sharedPreferences;
    Context context = this;
    String token = " ";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = this.sharedPreferences.getString("user", "");
        this.u = gson.fromJson(json, User.class);
        this.token= this.sharedPreferences.getString("TOKEN", "");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search users");
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("wow");
                getUsers(query);
                return false;
            }

        });
        return true;
    }

    public void getUsers(String query){
        if(isInternetAvailable()) {
            DBGetter dbGetter = new DBGetter("/minimalUser/" + query, token);
            dbGetter.delegate = this;
            dbGetter.execute();
        }else{
            Toast.makeText(context, "No internet connection available",
                    Toast.LENGTH_LONG).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        search.this.finish();
    }

        @Override
        public void processFinish(String data) {
            System.out.println("YOOOOO INSIDE SEARCH PROCESS FINISH" + data);
            TextView t1 = (TextView) findViewById(R.id.search_name);
            TextView t2 = (TextView) findViewById(R.id.search_level);
            TextView t3 = (TextView) findViewById(R.id.search_average);
            TextView t4 = (TextView) findViewById(R.id.search_points);
            Button btn = (Button) findViewById(R.id.search_raceButton);
            CardView foundUser = (CardView) findViewById(R.id.foundUser);

            if(!data.trim().equals("error")){
                final minimalUser user = new Gson().fromJson(data, minimalUser.class);
                Races race = new Races();
                foundUser.setVisibility(View.VISIBLE);

                t1.setText(user.username + " ");
                t2.setText(user.userLevel + " ");
                t3.setText(user.getAverageDist() + "Km@" + user.getAverageSpeed() + "Km/hr");
                t4.setText(user.userScore + "pts.");
                btn.setText("Race");
                final String raceId = UUID.randomUUID().toString();

                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        JSONObject object = new JSONObject();
                        boolean raceSent = false;
                        try {
                            object.put("compUsername", user.username);
                            object.put("username", u.getUsername());
                            object.put("id", raceId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(Races races : u.getRaces()){
                            if(races.getCUsername().equals(user.username) && !races.isComplete){
                                raceSent = true;
                            }
                        }
                        if(raceSent){
                            Toast.makeText(context, "Race already set with " + user.username,
                                    Toast.LENGTH_LONG).show();
                        }else{
                            DBConnect db = new DBConnect(token);
                            db.post("/user/"+u.getUsername()+"/acceptRace",object);
                            Toast.makeText(context, "Race request sent to " + user.username,
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context,UserMain.class);
                            startActivity(intent);
                        }
                    }
                });
            }else{
                foundUser.setVisibility(View.GONE);
                Toast.makeText(context, "No user with that username found",
                        Toast.LENGTH_LONG).show();
            }
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
}

