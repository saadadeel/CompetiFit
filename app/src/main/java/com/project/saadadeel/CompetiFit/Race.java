package com.project.saadadeel.CompetiFit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewGenerator;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Race extends Fragment {
    User u;
    ArrayList<Races> userRaces;
    boolean isPending = false;
    boolean isRecieved = false;
    boolean isActive = false;
    boolean isComplete = false;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.race, container, false);
        this.sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String data = this.sharedPreferences.getString("user", "");
        this.u = new Gson().fromJson(data, User.class);
        this.userRaces = u.getRaces();
        if(isInternetAvailable()) {
            try {
                populate(v, this.userRaces);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            CardView messageCard = (CardView) v.findViewById(R.id.noInternetCard);
            messageCard.setVisibility(View.VISIBLE);
            RelativeLayout raceLayout = (RelativeLayout) v.findViewById(R.id.raceLayout);
            raceLayout.setVisibility(View.GONE);
        }
        return v;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void populate(View v, final ArrayList<Races> r) throws JSONException {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.u = bundle.getParcelable("User");
        }
        ViewGenerator generator = new ViewGenerator(v, getActivity(),this.u);
        for(final Races races : r){
            if(races.status.equals("pending")){
                generator.populateRaceView((TableLayout) v.findViewById(R.id.racesSent),this.u, races);
                this.isPending = true;
            } else if (races.status.equals("recieved")){
                generator.populateRaceView((TableLayout) v.findViewById(R.id.racesRecieved),this.u, races);
                this.isRecieved = true;
            }else if (races.status.equals("active")){
                generator.populateRaceView((TableLayout) v.findViewById(R.id.racesDue),this.u, races);
                this.isActive = true;
            }else if (races.status.equals("complete")){
                generator.populateRaceView((TableLayout) v.findViewById(R.id.racesCompleted),this.u, races);
                this.isComplete = true;
            }
        }

        if(isPending){
            TextView text = (TextView) v.findViewById(R.id.noSentChallenges);
            text.setVisibility(View.GONE);
        }
        if(isRecieved){
            TextView text = (TextView) v.findViewById(R.id.noChallenges);
            text.setVisibility(View.GONE);
        }
        if(isActive){
            TextView text = (TextView) v.findViewById(R.id.noRaces);
            text.setVisibility(View.GONE);
        }
        if(isComplete){
            TextView text = (TextView) v.findViewById(R.id.noCompletedChallenges);
            text.setVisibility(View.GONE);
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager conManager =
                (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }
}