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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by saadadeel on 11/04/2016.
 */
public class leagueRVA extends RecyclerView.Adapter<leagueRVA.PersonViewHolder>{

    ArrayList<minimalUser> league;
    User user;
    Context context;
    String token;

    public leagueRVA(ArrayList<minimalUser> league, User user, Context context, String token){
        this.league = league;
        this.user = user;
        this.context = context;
        this.token = token;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_card, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.personName.setText(league.get(position).username);
        holder.personAge.setText("lvl. " + league.get(position).userLevel);
        holder.personPhoto.setText(position + 1 + ".");
        holder.points.setText(league.get(position).userScore + "pts");
        holder.avg.setText(league.get(position).getAverageDist() + "Km @ " + league.get(position).getAverageSpeed() + "KM/hr");

        if (league.get(position).username.equals(user.getUsername()) && league.get(position).userLevel == user.getUserLevel()) {
            System.out.println("USERNAME LEAGUE   " + user.getUsername());
                holder.btn.setVisibility(View.GONE);
        }else {
            holder.btn.setVisibility(View.VISIBLE);
            final String comp = league.get(position).username;
            final String raceId = UUID.randomUUID().toString();
            final String username= user.getUsername();
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

                    if(isNetworkAvailable()){
                        for (Races races : user.getRaces()) {
                            if (races.getCUsername().equals(comp) && !races.isComplete) {
                                raceSent = true;
                            }
                        }
                        if (raceSent) {
                            Toast.makeText(context, "Race already set with " + comp,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            DBConnect db = new DBConnect(object, token);
                            db.post("/activity/acceptRace");
                            Toast.makeText(context, "Race request sent to " + comp,
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, UserMain.class);
                            context.startActivity(intent);
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

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        TextView personPhoto;
        TextView points;
        TextView avg;
        Button btn;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (TextView)itemView.findViewById(R.id.person_photo);
            points = (TextView)itemView.findViewById(R.id.league_points);
            avg = (TextView)itemView.findViewById(R.id.avg);
            btn = (Button)itemView.findViewById(R.id.lRaceButton);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}