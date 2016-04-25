package com.project.saadadeel.CompetiFit.ViewGenerator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import android.view.Menu;

import com.project.saadadeel.CompetiFit.LeagueTable;
import com.project.saadadeel.CompetiFit.Performance;
import com.project.saadadeel.CompetiFit.Race;
import com.project.saadadeel.CompetiFit.Models.User;
import com.project.saadadeel.CompetiFit.profile;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    CharSequence tabTitles[];
    int numberOfTabs;

    public ViewPagerAdapter(FragmentManager fm,CharSequence titles[], int numbOfTabs) {
        super(fm);
        this.tabTitles = titles;
        this.numberOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        {
            LeagueTable ltView = new LeagueTable();
            return ltView;
        }
        if (position == 1)
        {
            Performance performanceView = new Performance();
            return performanceView;
        }
        else
        {
            Race rView = new Race();
            return rView;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}