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

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by saadadeel on 22/02/2016.
 */
public class Race extends Fragment {
    User u;
    ArrayList<Races> userRaces;

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
        TableLayout table = new TableLayout(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.u = bundle.getParcelable("User");
        }

        for (final Races race : r) {
            TableRow tr = new TableRow(getActivity());
            TableRow tr1 = new TableRow(getActivity());
            TableRow tr2 = new TableRow(getActivity());

            TextView competitor = new TextView(getActivity());
            TextView secondText = new TextView(getActivity());
            TextView thirdText = new TextView(getActivity());
            TextView fourthText = new TextView(getActivity());

            Button btn = new Button(getActivity());
            btn.setText("Start");
            btn.setId(43 + 2);
            btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn.setTextColor(getResources().getColor(R.color.colorForeground));
            LinearLayout.LayoutParams layoutParams4 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
            btn.setLayoutParams(layoutParams4);

            tr.setElevation(10);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            competitor.setText(race.getCUsername());
            competitor.setId(34 + 34);
            competitor.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            competitor.setTextSize(20);
            competitor.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            competitor.setGravity(Gravity.CENTER);
            competitor.setPadding(4, 30, 4, 30);
//            LinearLayout.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(4, 0, 4, 0);
//            competitor.setLayoutParams(layoutParams);

            secondText.setId(34 + 34);
            secondText.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            secondText.setTextSize(20);
            secondText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            secondText.setGravity(Gravity.TOP);
            secondText.setPadding(4, 30, 4, 30);

            thirdText.setId(34 + 34);
            thirdText.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            thirdText.setTextSize(20);
            thirdText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            thirdText.setGravity(Gravity.TOP);
            thirdText.setPadding(4, 30, 4, 30);

            fourthText.setId(34 + 34);
            fourthText.setTextColor(getResources().getColor(R.color.colorPrimary));
            fourthText.setTextSize(20);
            fourthText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            fourthText.setGravity(Gravity.TOP);
            fourthText.setPadding(4, 30, 4, 30);

            tr.addView(competitor);

            secondText.setText("10Km in 74 mins");
            secondText.setTextSize(15);
            secondText.setGravity(Gravity.CENTER);

            thirdText.setText("lvl 3");
            thirdText.setTextSize(15);
            thirdText.setGravity(Gravity.CENTER);

            fourthText.setText("30 pts");
            fourthText.setGravity(Gravity.CENTER);

            tr1.addView(secondText);
            tr1.addView(thirdText);
            tr2.addView(fourthText);


            if (race.getStatus().equals("active")){
                tr.addView(btn);
                table = (TableLayout) v.findViewById(R.id.racesDue);
            }
            if (race.getStatus().equals("recieved")) {
                tr.addView(btn);
                table = (TableLayout) v.findViewById(R.id.racesRecieved);
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
//                        JSONObject custom = new JSONObject();
//                        try {
//                            custom.put("username", u.getUsername());
//                            custom.put("compUsername", race.getCUsername());
//                            custom.put("id", race.getId());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        DBConnect db = new DBConnect(custom);
//                        db.post("/activity/acceptRace");

                        Intent intent;
                        intent = new Intent(getActivity(), Pop.class);
                        intent.putExtra("run", r);
                        intent.putExtra("isRace",true);
                        intent.putExtra("User",u);
                        startActivity(intent);
                    }
                });
            }
            if(race.getStatus().equals("pending")){
                btn.setText("Cancel");
                btn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tr.addView(btn);

                table = (TableLayout) v.findViewById(R.id.racesSent);

                tr1.removeAllViews();
                secondText.setText("Lvl 3");
                thirdText.setText("34 pts.");

                tr1.setBackgroundColor(getResources().getColor(R.color.colorBackground));
                tr1.addView(secondText);
                tr1.addView(thirdText);
            }
            if(race.getStatus().equals("completed")){
                tr.addView(fourthText);
                tr1.addView(secondText);
                thirdText.setText("W");
                tr2.addView(thirdText);
            }
            table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            table.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            if(race.getStatus().equals("recieved") || race.getStatus().equals("active")){
                table.addView(tr2, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }

    }
}