package com.project.saadadeel.CompetiFit;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.widget.CardView;
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
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.league_table, container, false);
        this.sharedPreferences = getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);

        String data = this.sharedPreferences.getString("user", "");
        this.u = new Gson().fromJson(data, User.class);
        ArrayList<minimalUser> mU = this.u.getUserLeague();

        this.token = this.sharedPreferences.getString("TOKEN", "");
        this.context = getActivity();

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        leagueRVA adapter = new leagueRVA(mU, u, context, this.token);
        rv.setAdapter(adapter);

        if(!isInternetAvailable()){
            CardView messageCard = (CardView) v.findViewById(R.id.noInternetCard);
            messageCard.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        return v;
    }
    private boolean isInternetAvailable() {
        ConnectivityManager conManager =
                (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }
}
