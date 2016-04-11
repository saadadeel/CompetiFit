package com.project.saadadeel.CompetiFit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
        DBGetter dbGetter = new DBGetter("/user/minimal/" + query, token);
        dbGetter.delegate = this;
        dbGetter.execute();
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
        final minimalUser user = new Gson().fromJson(data, minimalUser.class);
        if (user != null) {
            Races race = new Races();
            TableLayout table = (TableLayout) findViewById(R.id.userFound);
            table.setVisibility(View.VISIBLE);

            TextView t1 = (TextView) findViewById(R.id.name);
            t1.setText(user.username + " ");

            TextView t2 = (TextView) findViewById(R.id.level);
            t2.setText(user.userLevel + " ");

            TextView t3 = (TextView) findViewById(R.id.average);
            t2.setText("13KM/hr ");

            Button btn = new Button(this);
            btn.setText("Race");
            btn.setId(43 + 2);
            btn.setBackgroundColor(this.getResources().getColor(R.color.colorPrimary));
            btn.setTextColor(this.getResources().getColor(R.color.colorForeground));
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            layoutParams.span = 1;
            btn.setLayoutParams(layoutParams);
            final String raceId = UUID.randomUUID().toString();

            TableRow tableRow = (TableRow) findViewById(R.id.row3);
            tableRow.addView(btn);

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
                        DBConnect db = new DBConnect(object, token);
                        db.post("/activity/acceptRace");
                        Toast.makeText(context, "Race request sent to " + user.username,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            TableLayout table = (TableLayout) findViewById(R.id.userFound);
            table.setVisibility(View.VISIBLE);

            TextView t1 = (TextView) findViewById(R.id.position1);
            t1.setText("no user found");
        }
    }
}
