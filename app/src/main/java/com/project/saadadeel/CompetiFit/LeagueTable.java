package com.project.saadadeel.CompetiFit;

        import android.graphics.Color;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TableLayout;
        import android.widget.TableRow;
        import android.widget.TextView;

        import org.w3c.dom.Text;

public class LeagueTable extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.league_table, container, false);
        setTable(v);
        return v;

    }

    public void setTable(View view) {
        TableLayout table = (TableLayout) view.findViewById(R.id.tableLayout1);

        for (int i = 0; i < 20; i++) {
        /* Create a new row to be added. */
            TableRow tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        /* Position*/
            TextView position = new TextView(getActivity());
            position.setText("1.");
            position.setId(200 + 28);
            position.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            position.setTextSize(30);
            position.setGravity(Gravity.TOP);

            position.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(position);

        /* Name*/
            TextView name = new TextView(getActivity());
            name.setText("Saad Adeel");
            name.setId(200 + 27);
            name.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            name.setTextSize(30);
            name.setGravity(Gravity.TOP);

            name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(name);

        /* Points*/
            TextView points = new TextView(getActivity());
            points.setText("57 pts");
            points.setId(200 + 26);
            points.setTextColor(getResources().getColor(R.color.primary_text_material_dark));
            points.setTextSize(50);
            points.setGravity(Gravity.RIGHT);

            points.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(points);
            tr.setPadding(0,40,0,40);
            tr.setClickable(true);

            //tr.setBackgroundResource(R.drawable.sf_gradient_03);
            table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}


