package com.project.saadadeel.CompetiFit.ViewGenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;
import com.project.saadadeel.CompetiFit.RunTracker.Pop;
import com.project.saadadeel.CompetiFit.UserMain;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by saadadeel on 13/03/2016.
 */
public class ViewGenerator implements DBResponse{

    View view;
    Context context;
    TableRow row1;
    TableRow row2;
    TableRow row3;
    TextView text;
    ArrayList<TableRow> rows = new ArrayList<TableRow>();
    User user;
    public SharedPreferences sharedPreferences;
    String token = " ";
    DBConnect db;

    public ViewGenerator(View v, Context c, User u){
        this.view = v;
        this.context = c;
        text = getSimpleTextView(1);
        this.user = u;
        this.sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        this.token= this.sharedPreferences.getString("TOKEN", "");
    }

    public TableLayout populateRaceView(TableLayout table, final User u, final Races race){
        TextView text = getSimpleTextView(1);
        TextView text1 = getSimpleTextView(1);
        TextView text2 = getSimpleTextView(1);
        TextView text3 = getSimpleTextView(1);
        TextView text4 = getSimpleTextView(2);

        TableRow row1 = new TableRow(this.context);
        TableRow row2 = new TableRow(this.context);
        TableRow row3 = new TableRow(this.context);
        TableRow row4 = new TableRow(this.context);

        TableLayout.LayoutParams lp =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 0, 30, 0);

        TableLayout.LayoutParams lp1 =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(30, 0, 30, 30);

        text.setText(race.competitorUsername);//race.getCUsername());
        row1.addView(text);
        text1.setText("lvl. " + race.getCompLevel());//+ race.getCompLevel());
        row1.addView(text1);

        text2.setText(race.getKMChallengedMiles() + "km @ " + race.getKMChallengedSpeed() + "km/hr");
        text2.setTextSize(10);
        row2.addView(text2);
        text3.setText(race.points + "pts");//race.points);
        row2.addView(text3);

        table.addView(row1, lp);
        table.addView(row2, lp);

        if(race.status.equals("pending")){
            Button button1 = getCancelButton(u,2,race);
            button1.setText("Cancel");
            row3.addView(button1);
            row3.setPadding(0, 0, 0, 20);
            table.addView(row3, lp1);
        } else if (race.status.equals("recieved")){
            Button button  = getRaceButton(u,1, race);
            Button button1 = getCancelButton(u,1,race);
            button1.setText("Decline");
            row3.addView(button1);
            row3.addView(button);
            row3.setPadding(0, 0, 0, 20);
            table.addView(row3, lp1);
        }else if (race.status.equals("active")){
            Button button  = getRaceButton(u,2, race);
            row3.addView(button);
            row3.setPadding(0, 0, 0, 20);
            table.addView(row3, lp1);
        }

        if (race.status.equals("complete")){
            row2.removeView(text3);
            table.removeView(row2);

            text4.setText(race.result + " ");
            text4.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            text4.setGravity(Gravity.CENTER);

            row4.addView(text3);
            row4.addView(text4);
            table.addView(row4, lp1);
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

    public Button getRaceButton(final User u, int span, final Races race){
        Button btn = new Button(context);
        btn.setText("Start");
        btn.setId(43 + 2);
        btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        btn.setTextColor(context.getResources().getColor(R.color.colorForeground));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        layoutParams.span = span;
        btn.setLayoutParams(layoutParams);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isInternetAvailable()) {
                    Intent intent = new Intent(context, Pop.class);
                    intent.putExtra("isRace", true);
                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(race);
                    prefsEditor.putString("compRace", json);
                    prefsEditor.commit();
                    System.out.println("RACer name    " + race.getCUsername());
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "No Internet Connection Available",
                        Toast.LENGTH_LONG).show();
                }
            }
        });
        return btn;
    }

    public Button getCancelButton(final User u, int span, final Races race){
        Button btn = new Button(context);
        btn.setText("Decline");
        btn.setId(43 + 2);
        btn.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        btn.setTextColor(context.getResources().getColor(R.color.colorForeground));
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        layoutParams.span = span;
        btn.setLayoutParams(layoutParams);

        db = new DBConnect(token);
        db.delegate = this;

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            JSONObject object = new JSONObject();
            try {
                object.put("compUsername", race.getCUsername());
                object.put("username", user.getUsername());
                object.put("id", race.getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            db.post("/user/" + user.getUsername() + "/declineRace", object);
            }
        });
        return btn;
    }
    private boolean isInternetAvailable() {
        ConnectivityManager conManager =
                (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    @Override
    public void processFinish(String data) {
        if(data.trim().equals("error")){
            Toast.makeText(context, "Error occured",
                    Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(context, UserMain.class);
            context.startActivity(intent);
        }
    }
}
