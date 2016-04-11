package com.project.saadadeel.CompetiFit.ViewGenerator;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;

import java.util.ArrayList;

/**
 * Created by saadadeel on 11/04/2016.
 */
public class leagueRVA extends RecyclerView.Adapter<leagueRVA.PersonViewHolder>{

    ArrayList<minimalUser> league;

    public leagueRVA(ArrayList<minimalUser> league){
        this.league = league;
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

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (TextView)itemView.findViewById(R.id.person_photo);
            points = (TextView)itemView.findViewById(R.id.league_points);
            avg = (TextView)itemView.findViewById(R.id.avg);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}