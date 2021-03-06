package com.project.saadadeel.CompetiFit.RunTracker;

/**
 * Created by saadadeel on 11/04/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.project.saadadeel.CompetiFit.LeagueTable;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.Performance;
import com.project.saadadeel.CompetiFit.Race;

/**
 * Created by hp1 on 21-01-2015.
 */
public class runningVPA extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    User user;

    Bundle args = new Bundle();

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public runningVPA(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb, User user) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        setArgs(user);
    }

    public void setArgs(User newU){
        this.user = newU;
        args.putParcelable("User", user);
        args.putParcelableArrayList("userRuns", user.getRuns());
        args.putParcelableArrayList("userLeague", user.getleague());
        args.putParcelableArrayList("userRaces", user.getRaces());
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0 && user!=null) // if the position is 0 we are returning the First tab
        {
            LeagueTable ltView = new LeagueTable();
            ltView.setArguments(this.args);
            return ltView;
        }
        if (position == 1)
        {
            Performance performanceView = new Performance();
            performanceView.setArguments(this.args);
            return performanceView;
        }
        else
        {
            Race rView = new Race();
            rView.setArguments(this.args);
            return rView;
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

    public void update(User u){
        this.user = u;
        this.args.putParcelable("User", user);
        this.args.putParcelableArrayList("userRaces", user.getRaces());
        this.args.putParcelableArrayList("userRuns", user.getRuns());
    }
}
