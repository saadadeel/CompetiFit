package com.project.saadadeel.CompetiFit;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewGenerator;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.race, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userRaces = bundle.getParcelableArrayList("userRaces");
            this.u = bundle.getParcelable("User");
        }
        try {
            populate(v, this.userRaces);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void populate(View v, final ArrayList<Races> r) throws JSONException {
//        TableLayout table = new TableLayout(getActivity());
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
            TextView text = (TextView) v.findViewById(R.id.noCompletedChallenges);
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
    public void addRowsToTable(ArrayList<TableRow> rows, TableLayout table){
        for(TableRow row: rows){
            table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}