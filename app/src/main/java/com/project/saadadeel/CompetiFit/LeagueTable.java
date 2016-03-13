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

        import java.util.ArrayList;

public class LeagueTable extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.league_table, container, false);
        populate(v);
        return v;
    }

    public void populate(View v){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            User u = bundle.getParcelable("User");
            TextView first = (TextView) v.findViewById(R.id.name1);
            first.setText(u.getUsername());

            ArrayList<minimalUser> mU = bundle.getParcelableArrayList("userLeague");
            setTable(v, mU);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setTable(View view, ArrayList<minimalUser> r) {
        TableLayout table = (TableLayout) view.findViewById(R.id.tableLayout1);
        int i =1;

        for (minimalUser leagueComp: r) {
            TableRow tr = new TableRow(getActivity());
            TableRow tr1 = new TableRow(getActivity());

            TextView competitor = new TextView(getActivity());
            TextView secondText = new TextView(getActivity());
            TextView thirdText = new TextView(getActivity());
            TextView fourthText = new TextView(getActivity());

            competitor.setText(String.valueOf(i + "."));
            competitor.setId(3 + 34);
            competitor.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            competitor.setTextSize(20);
            competitor.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            competitor.setGravity(Gravity.CENTER);
            competitor.setPadding(4, 30, 4, 30);

            secondText.setText(String.valueOf(leagueComp.username));
            secondText.setId(34 + 3);
            secondText.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            secondText.setTextSize(20);
            secondText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            secondText.setGravity(Gravity.CENTER);
            secondText.setPadding(4, 30, 4, 30);

            thirdText.setText(String.valueOf(leagueComp.userScore));
            thirdText.setId(34 + 9);
            thirdText.setTextColor(getResources().getColor(R.color.colorPrimary));
            thirdText.setTextSize(20);
            thirdText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
            thirdText.setGravity(Gravity.CENTER);
            thirdText.setPadding(4, 30, 4, 30);

            tr.addView(competitor);
            tr.addView(secondText);
            tr.addView(thirdText);

//            fourthText.setText(String.valueOf(leagueComp.userScore));
//            fourthText.setId(34 + 39);
//            fourthText.setTextColor(getResources().getColor(R.color.colorPrimary));
//            fourthText.setTextSize(20);
//            fourthText.setBackgroundColor(getResources().getColor(R.color.colorForeground));
//            fourthText.setGravity(Gravity.CENTER);
//            fourthText.setPadding(4, 30, 4, 30);

            tr.setElevation(20);
//            LinearLayout.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.FILL_PARENT);
//            layoutParams.setMargins(60,60,60,60);
//            tr1.setLayoutParams(layoutParams);
//            tr1.addView(thirdText);
//            tr1.addView(fourthText);

            table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
//            table.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            i++;
        }
    }
}
