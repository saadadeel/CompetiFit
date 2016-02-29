package com.project.saadadeel.CompetiFit;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.connection.Races;
import com.project.saadadeel.CompetiFit.connection.Runs;
import com.project.saadadeel.CompetiFit.connection.User;

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
        populate(v);
        return v;
    }

    public void populate(View view){
        TextView score = (TextView) view.findViewById(R.id.score);
        score.setText(String.valueOf(this.user.getUserScore()) + "pts");

        setRunTable(view, this.userRuns);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setRunTable(View v, ArrayList<Runs> r){
        TableLayout table = (TableLayout) v.findViewById(R.id.runs);

        for (Runs runs: r) {
            TableRow tr = new TableRow(getActivity());
            TableRow tr1 = new TableRow(getActivity());

            TextView competitor = new TextView(getActivity());
            TextView secondText = new TextView(getActivity());
            TextView thirdText = new TextView(getActivity());
            TextView fourthText = new TextView(getActivity());

            competitor.setText(String.valueOf(runs.getDistance()));
            competitor.setId(3 + 34);
            competitor.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            competitor.setTextSize(15);
            competitor.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            competitor.setGravity(Gravity.CENTER);
            competitor.setPadding(4, 30, 4, 30);

            secondText.setText(String.valueOf(runs.getDate()));
            secondText.setId(34 + 3);
            secondText.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            secondText.setTextSize(15);
            secondText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            secondText.setGravity(Gravity.CENTER);
            secondText.setPadding(4, 30, 4, 30);

            tr.addView(competitor);
            tr.addView(secondText);

            thirdText.setText(String.valueOf(runs.getSpeed()));
            thirdText.setId(34 + 9);
            thirdText.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            thirdText.setTextSize(20);
            thirdText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            thirdText.setGravity(Gravity.CENTER);
            thirdText.setPadding(4, 30, 4, 30);

            fourthText.setText(String.valueOf(runs.getScore()));
            fourthText.setId(34 + 39);
            fourthText.setTextColor(getResources().getColor(R.color.colorPrimary));
            fourthText.setTextSize(20);
            fourthText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            fourthText.setGravity(Gravity.CENTER);
            fourthText.setPadding(4, 30, 4, 30);

            tr1.setElevation(20);
            LinearLayout.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT);
            layoutParams.setMargins(60,60,60,60);
            tr1.setLayoutParams(layoutParams);
            tr1.addView(thirdText);
            tr1.addView(fourthText);

            table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            table.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}