package com.project.saadadeel.CompetiFit;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.ViewGenerator.ViewGenerator;
import com.project.saadadeel.CompetiFit.ViewGenerator.leagueRVA;
import com.project.saadadeel.CompetiFit.ViewGenerator.runRVA;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Performance extends Fragment {
    User user;
    ArrayList<Runs> userRuns;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.performance,container,false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.user = bundle.getParcelable("User");
            if (bundle != null) {this.userRuns = bundle.getParcelableArrayList("userRuns");}
        }
//        populate(v);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rvRun);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        runRVA adapter = new runRVA(this.userRuns, user);
        rv.setAdapter(adapter);
        return v;
    }

//    public void populate(View view){
//        TextView score = (TextView) view.findViewById(R.id.score);
//        TextView level = (TextView) view.findViewById(R.id.level);
//        TextView average = (TextView) view.findViewById(R.id.KmAndSpeed);
//        score.setText(String.valueOf(this.user.getUserScore()) + "pts");
//        level.setText("level " + this.user.getUserLevel());
//        average.setText(this.user.getAverageDist() + " km @ " + this.user.getAverageSpeed() + "km/hr");
//
////        setRunTable(view, this.userRuns);
//    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public void setRunTable(View v, ArrayList<Runs> r){
//        TableLayout table = (TableLayout) v.findViewById(R.id.runs);
//
//        for (Runs runs: r) {
//            ViewGenerator vg = new ViewGenerator(v,getActivity(),this.user);
//            ArrayList<TableRow> rows = vg.populateRunView(runs);
//
//            table.addView(rows.get(0));
//            table.addView(rows.get(1));
//            table.addView(rows.get(2));
//        }
//    }
}