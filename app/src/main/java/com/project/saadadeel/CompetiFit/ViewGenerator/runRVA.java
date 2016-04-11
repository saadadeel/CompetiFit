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
    User header;

    public runRVA(ArrayList<Runs> runs, User u){
        this.runs = runs;
        this.header = u;
    }
//
//    @Override
//    public RunViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_card, parent, false);
////        RunViewHolder rvh = new RunViewHolder(v);
////        return rvh;
//        if(viewType == TYPE_HEADER)
//        {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
//            return  new Header(v);
//        }
//        else if(viewType == TYPE_ITEM)
//        {
//            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//            return new VHItem(v);
//        }
//        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.perf_header, parent, false);
            return  new Header(v);
        }
        else if(viewType == TYPE_ITEM)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_card, parent, false);
            return new RunViewHolder(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
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
//           Runs runs = getItem(position-1);
            RunViewHolder rholder = (RunViewHolder) holder;
          rholder.runDate.setText(runs.get(position).getDate() + " ");
          rholder.runAvg.setText(runs.get(position).getKMDist() + "Km @ " + runs.get(position).getKMperHrSpeed() + "Km/Hr");
          rholder.runPoints.setText(runs.get(position).getScore() + "pts.");
        }
    }

//    @Override
//    public void onBindViewHolder(RunViewHolder holder, int position) {
//        holder.runDate.setText(runs.get(position).getDate() + " ");
//        holder.runAvg.setText(runs.get(position).getKMDist() + "Km @ " + runs.get(position).getKMperHrSpeed() + "Km/Hr");
//        holder.runPoints.setText(runs.get(position).getScore() + "pts.");
//    }

    @Override
    public int getItemCount() {
        return runs.size();
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