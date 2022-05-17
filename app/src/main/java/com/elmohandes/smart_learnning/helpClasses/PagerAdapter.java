package com.elmohandes.smart_learnning.helpClasses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.elmohandes.smart_learnning.screens.Attendence_screen;
import com.elmohandes.smart_learnning.screens.bus_tracker_screen;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PagerAdapter( FragmentManager fm, int numberOfTabs) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numberOfTabs=numberOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new Attendence_screen();
            case 1:
                return new bus_tracker_screen();
            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
