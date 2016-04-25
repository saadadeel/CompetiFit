package com.project.saadadeel.CompetiFit.ViewGenerator;

import android.app.LauncherActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.saadadeel.CompetiFit.Models.Runs;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.Models.minimalUser;
import com.project.saadadeel.CompetiFit.R;

import java.util.ArrayList;

/**
 * Created by saadadeel on 11/04/2016.
 */
public class runRVA extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Runs> runs;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    boolean isHeaderSet = false;
    User header;

    public runRVA(ArrayList<Runs> runs, User u){
        this.runs = runs;
        this.header = u;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(!isHeaderSet){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.perf_header, parent, false);
            this.isHeaderSet = true;
            return  new Header(v);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_card, parent, false);
            return new RunViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Header)
        {
            Header VHheader = (Header)holder;
            VHheader.txtScore.setText(header.getUserScore() + "pts");
            VHheader.txtLevel.setText("level " + this.header.getUserLevel());
            VHheader.txtAvg.setText(this.header.getAverageDist() + " km @ " + this.header.getAverageSpeed() + "km/hr");
        }
        else if(holder instanceof RunViewHolder)
        {
           Runs runs = getItem(position-1);
            RunViewHolder rholder = (RunViewHolder) holder;
          rholder.runDate.setText(runs.getDate() + " ");
          rholder.runAvg.setText(runs.getKMDist() + "Km @ " + runs.getKMperHrSpeed() + "Km/Hr");
          rholder.runPoints.setText(runs.getScore() + "pts.");
        }
    }

    @Override
    public int getItemCount() {
        return runs.size() + 1;
    }

    public static class RunViewHolder extends RecyclerView.ViewHolder {
        CardView run;
        TextView runDate;
        TextView runAvg;
        TextView runPoints;

        RunViewHolder(View itemView) {
            super(itemView);
            run = (CardView)itemView.findViewById(R.id.runCard);
            runDate = (TextView)itemView.findViewById(R.id.run_date);
            runAvg = (TextView)itemView.findViewById(R.id.run_avg);
            runPoints = (TextView)itemView.findViewById(R.id.run_points);
        }
    }
    public static class Header extends RecyclerView.ViewHolder{
        TextView txtScore;
        TextView txtLevel;
        TextView txtAvg;
        public Header(View itemView) {
            super(itemView);
            this.txtScore = (TextView)itemView.findViewById(R.id.score);
            this.txtLevel = (TextView)itemView.findViewById(R.id.level);
            this.txtAvg = (TextView)itemView.findViewById(R.id.KmAndSpeed);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
    private Runs getItem(int position)
    {
        return runs.get(position);
    }

}