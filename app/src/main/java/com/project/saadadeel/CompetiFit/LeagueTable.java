package com.project.saadadeel.CompetiFit;

        import android.annotation.TargetApi;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;

        import com.project.saadadeel.CompetiFit.Models.Runs;
        import com.project.saadadeel.CompetiFit.Models.User;
        import com.project.saadadeel.CompetiFit.Models.minimalUser;
        import com.project.saadadeel.CompetiFit.ViewGenerator.ViewGenerator;

        import java.util.ArrayList;

public class LeagueTable extends Fragment {

    ViewGenerator generator;
    User u;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.league_table, container, false);
        populate(v);
        Bundle bundle = this.getArguments();
        this.u = bundle.getParcelable("User");
        return v;
    }

    public void populate(View v){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            User u = bundle.getParcelable("User");
            this.generator = new ViewGenerator(v,getActivity(),u);

            ArrayList<minimalUser> mU = bundle.getParcelableArrayList("userLeague");
            setTable(v, mU);
        }
    }

    public void setTable(View view, ArrayList<minimalUser> r) {
        TableLayout table = (TableLayout) view.findViewById(R.id.tableLayout1);
        generator.populateLeagueView(table, r);
    }
}
