package com.project.saadadeel.CompetiFit;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TableLayout;

        import com.google.gson.Gson;
        import com.project.saadadeel.CompetiFit.Models.User;
        import com.project.saadadeel.CompetiFit.Models.minimalUser;
        import com.project.saadadeel.CompetiFit.ViewGenerator.ViewGenerator;
        import com.project.saadadeel.CompetiFit.ViewGenerator.leagueRVA;

        import java.util.ArrayList;

public class LeagueTable extends Fragment {

    ViewGenerator generator;
    User u;
    String token;
    public SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.league_table, container, false);
        this.sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
//        populate(v);
        Bundle bundle = this.getArguments();
        this.u = bundle.getParcelable("User");
        ArrayList<minimalUser> mU = bundle.getParcelableArrayList("userLeague");
        this.token = this.sharedPreferences.getString("TOKEN", "");
        this.context = getActivity();

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        leagueRVA adapter = new leagueRVA(mU, u, context, this.token);
        rv.setAdapter(adapter);
        return v;
    }

    public void populate(View v){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            this.sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
            String json = this.sharedPreferences.getString("user", "");
            User u = gson.fromJson(json, User.class);
            this.generator = new ViewGenerator(v,getActivity(),u);

            ArrayList<minimalUser> mU = bundle.getParcelableArrayList("userLeague");
            RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);

//            leagueRVA adapter = new leagueRVA(mU);
//            rv.setAdapter(adapter);

            setTable(v, mU);
        }
    }

    public void setTable(View view, ArrayList<minimalUser> r) {
        TableLayout table = (TableLayout) view.findViewById(R.id.tableLayout1);
        generator.populateLeagueView(table, r);
    }
}
