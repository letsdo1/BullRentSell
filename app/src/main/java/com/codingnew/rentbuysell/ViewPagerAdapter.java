package com.codingnew.rentbuysell;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private  final List<Fragment> myList = new ArrayList<>();
    private  final List<String> myListtitle = new ArrayList<>();




    public ViewPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return myList.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return myListtitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return myListtitle.get(position);

    }

    public void addFragments(Fragment fragment, String name){
        myList.add(fragment);
        myListtitle.add(name);


    }



}
