package com.project.saadadeel.CompetiFit.ViewGenerator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.saadadeel.CompetiFit.Models.Races;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;
import com.project.saadadeel.CompetiFit.UserMain;
import com.project.saadadeel.CompetiFit.connection.DBConnect;
import com.project.saadadeel.CompetiFit.connection.DBResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by saadadeel on 11/04/2016.
 */
public class leagueRVA extends RecyclerView.Adapter<leagueRVA.LeagueViewHolder> implements DBResponse{

    ArrayList<minimalUser> league;
    User user;
    Context context;
    String token;
    DBConnect db = new DBConnect();

    public leagueRVA(ArrayList<minimalUser> league, User user, Context context, String token){
        this.league = league;
        this.user = user;
        this.context = context;
        this.token = token;
    }

    @Override
    public LeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_card, parent, false);
        LeagueViewHolder lvh = new LeagueViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(LeagueViewHolder holder, int position) {
        holder.leagueName.setText(league.get(position).username);
        holder.leagueLevel.setText("lvl. " + league.get(position).userLevel);
        holder.leaguePosition.setText(position + 1 + ".");
        holder.points.setText(league.get(position).userScore + "pts");
        holder.avg.setText(league.get(position).getAverageDist() + "Km @ " + league.get(position).getAverageSpeed() + "KM/hr");

        if (league.get(position).username.equals(user.getUsername()) && league.get(position).userLevel == user.getUserLevel()) {
                holder.btn.setVisibility(View.GONE);
        }else {
            holder.btn.setVisibility(View.VISIBLE);
            final String comp = league.get(position).username;
            final String raceId = UUID.randomUUID().toString();
            final String username= user.getUsername();

            db = new DBConnect(token);
            db.delegate = this;

            holder.btn.setOnClickListener(new View.OnClickListener() {
                boolean raceSent = false;
                public void onClick(View v) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("compUsername", comp);
                        object.put("username", username);
                        object.put("id", raceId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(isInternetAvailable()){
                        for (Races races : user.getRaces()) {
                            if (races.getCUsername().equals(comp) && !races.isComplete) {
                                raceSent = true;
                            }
                        }
                        if (raceSent) {
                            Toast.makeText(context, "Race already set with " + comp,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            db.post("/user/" + username + "/acceptRace", object);
                            Toast.makeText(context, "Race request sent to " + comp,
                                    Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, "No internet connection available",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return league.size();
    }

    @Override
    public void processFinish(String data) {
        System.out.println("done");
        Intent intent = new Intent(context, UserMain.class);
        context.startActivity(intent);
    }

    public static class LeagueViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView leagueName;
        TextView leagueLevel;
        TextView leaguePosition;
        TextView points;
        TextView avg;
        Button btn;

        LeagueViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            leagueName = (TextView)itemView.findViewById(R.id.league_name);
            leagueLevel = (TextView)itemView.findViewById(R.id.league_level);
            leaguePosition = (TextView)itemView.findViewById(R.id.league_position);
            points = (TextView)itemView.findViewById(R.id.league_points);
            avg = (TextView)itemView.findViewById(R.id.avg);
            btn = (Button)itemView.findViewById(R.id.lRaceButton);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
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

}