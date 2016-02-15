package com.project.saadadeel.CompetiFit;

        import android.os.AsyncTask;
        import android.os.Parcel;
        import android.os.Parcelable;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;
        import android.view.View;
        import android.widget.TextView;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import com.google.gson.Gson;
        import com.project.saadadeel.CompetiFit.connection.DBConnect;
        import com.project.saadadeel.CompetiFit.connection.User;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.Serializable;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.logging.Level;
        import java.util.logging.Logger;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    User user;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, User user) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.user = user;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
//        System.out.println("////////////////////////" + user.getUsername() + "///////////////");
        Bundle args = new Bundle();
        args.putParcelable("User", user);

        if(position == 0 && user!=null) // if the position is 0 we are returning the First tab
        {
            LeagueTable ltView = new LeagueTable();
            ltView.setArguments(args);
            return ltView;
        }
        if (position == 1)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Performance performanceView = new Performance();
            performanceView.setArguments(args);
            return performanceView;
        }
        if(position == 2)            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            profile pView = new profile();
            pView.setArguments(args);
            return pView;
        }
        else           // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            profile pView1 = new profile();
            pView1.setArguments(args);
            return pView1;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}