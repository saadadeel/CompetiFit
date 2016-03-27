package com.project.saadadeel.CompetiFit.ViewGenerator;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;
import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.connection.DBConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by saadadeel on 13/03/2016.
 */
public class ViewGenerator {

    View view;
    Context context;
    TableRow row1;
    TableRow row2;
    TableRow row3;
    TextView text;
    ArrayList<TableRow> rows = new ArrayList<TableRow>();
    User user;

    public ViewGenerator(View v, Context c, User u){
        this.view = v;
        this.context = c;
        text = getSimpleTextView(1);
        this.user = u;
    }

    public ArrayList<TableRow> populateRunView(Runs run){

        TableLayout.LayoutParams lp =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        TableLayout.LayoutParams lp1 =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        TableLayout.LayoutParams lp2 =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30,30,30,0);
        lp1.setMargins(30, 0, 30, 0);
        lp2.setMargins(30,0,30,30);

        TextView text = getSimpleTextView(1);
        TextView text1 = getSimpleTextView(1);
        TextView text2 = getSimpleTextView(1);

        TableRow row1 = new TableRow(this.context);
        TableRow row2 = new TableRow(this.context);
        TableRow row3 = new TableRow(this.context);

        text.setText(run.getDate());
        text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        row1.addView(text);
        row1.setLayoutParams(lp);

        text1.setText(run.getKMDist() + "Km at " + run.getKMperHrSpeed() + "Km/Hr");//+ race.getCompLevel());
        row2.addView(text1);
        row2.setLayoutParams(lp1);

        text2.setText(run.getScore() + "pts.");
        text2.setTextSize(20);
        text2.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        row3.addView(text2);
        row3.setLayoutParams(lp2);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        return this.rows;
    }

    public ArrayList<TableRow> populateRaceView(final User u, final Races race){
        TextView text = getSimpleTextView(1);
        TextView text1 = getSimpleTextView(1);
        TextView text2 = getSimpleTextView(1);
        TextView text3 = getSimpleTextView(1);

        TableRow row1 = new TableRow(this.context);
        TableRow row2 = new TableRow(this.context);
        TableRow row3 = new TableRow(this.context);

        text.setText(race.competitorUsername);//race.getCUsername());
        row1.addView(text);
        text1.setText("lvl. " + race.getCompLevel());//+ race.getCompLevel());
        row1.addView(text1);

        text2.setText(race.challengedMiles + "km @ " + race.challengedSpeed + "km/hr");
        text2.setTextSize(10);
        //race.challengedMiles + " @ " + race.challengedSpeed);
        row2.addView(text2);
        text3.setText(race.points + "pts");//race.points);
        row2.addView(text3);

        if(race.status.equals("pending")){
            Button button1 = getCancelButton(u);
            row3.addView(button1);
            row3.setPadding(0,0,0,20);
        } else if (race.status.equals("recieved")){
            Button button  = getRaceButton(u,1, race.competitorUsername);
            Button button1 = getCancelButton(u);
            row3.addView(button1);
            row3.addView(button);
            row3.setPadding(0,0,0,20);
        }else if (race.status.equals("active")){
            Button button  = getRaceButton(u,2, race.competitorUsername);
            row3.addView(button);
            row3.setPadding(0, 0, 0, 20);
        }

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        return this.rows;
    }


    public TableLayout populateLeagueView(TableLayout table, ArrayList<minimalUser> minimalUsers){

        int position = 1;

        for(minimalUser mUser: minimalUsers) {
            if(mUser!=null) {

                TextView text = getSimpleTextView(1);
                TextView text1 = getSimpleTextView(1);
                TextView text2 = getSimpleTextView(1);
                TextView text3 = getSimpleTextView(2);

                TableRow row1 = new TableRow(this.context);
                TableRow row2 = new TableRow(this.context);
                TableRow row3 = new TableRow(this.context);

                text.setText(position + ".");//race.getCUsername());
                text.setPadding(1, 30, 1, 30);
                row1.addView(text);
                text1.setText(mUser.username);//+ race.getCompLevel());
                row1.addView(text1);
                text2.setText(String.valueOf(mUser.userScore) + "pts.");//race.points);
                text2.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                text.setPadding(4, 40, 4, 40);
                text2.setTextSize(30);
                row1.addView(text2);

                text3.setText("lvl." + mUser.userLevel + "\n" + mUser.averageDistance + "@" + mUser.averageSpeed);
                text3.setTextSize(15);
                text3.setGravity(Gravity.CENTER);
                text3.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                row2.addView(text3);

                Button button = getRaceButton(new User(), 1, mUser.username);
                button.setText("Race");
                final String comp = mUser.username;
                final String raceId = UUID.randomUUID().toString();

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        JSONObject object = new JSONObject();
                        try {
                            object.put("compUsername", comp);
                            object.put("username", user.getUsername());
                            object.put("id", raceId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DBConnect db = new DBConnect(object);
                        db.post("/activity/acceptRace");
                        Toast.makeText(context, "Race request sent to " + comp,
                                Toast.LENGTH_LONG).show();
                    }
                });
                row2.addView(button);
                row2.setPadding(0, 0, 0, 40);

                table.addView(row1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                table.addView(row2, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                position++;
            }
        }
        return table;
    }


    public TextView getSimpleTextView(int span){
        TextView text = new TextView(this.context);
        text.setId(34 + 34);
        text.setTextColor(context.getResources().getColor(R.color.primary_text_material_dark));
        text.setTextSize(20);
        text.setBackgroundColor(context.getResources().getColor(R.color.colorForeground));
        text.setGravity(Gravity.CENTER);
        text.setPadding(4, 30, 4, 30);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        layoutParams.span = span;
        text.setLayoutParams(layoutParams);
        return text;
    }

    public Button getRaceButton(final User u, int span, final String comp){
        Button btn = new Button(context);
        btn.setText("Start");
        btn.setId(43 + 2);
        btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        btn.setTextColor(context.getResources().getColor(R.color.colorForeground));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
//        TableRow.LayoutParams layoutParams = (TableRow.LayoutParams)btn.getLayoutParams();
//        layoutParams.span = span;
        layoutParams.span = span;
        btn.setLayoutParams(layoutParams);
        return btn;
    }

    public Button getCancelButton(final User u){
        Button btn = new Button(context);
        btn.setText("Cancel");
        btn.setId(43 + 2);
        btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        btn.setTextColor(context.getResources().getColor(R.color.colorForeground));
        LinearLayout.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        btn.setLayoutParams(layoutParams);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Cancel");
            }
        });
        return btn;
    }
}
