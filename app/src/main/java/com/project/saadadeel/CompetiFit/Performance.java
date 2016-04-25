package com.project.saadadeel.CompetiFit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
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
    public SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.performance, container, false);
//        Bundle bundle = this.getArguments();
        this.sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);

//        if (bundle != null) {
//            this.user = bundle.getParcelable("User");
        String data = this.sharedPreferences.getString("user", "");
        this.user = new Gson().fromJson(data, User.class);
        this.userRuns = this.user.getRuns();
//        }
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rvRun);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        runRVA adapter = new runRVA(this.userRuns, user);
        rv.setAdapter(adapter);
        return v;
    }
}