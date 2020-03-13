package com.codingnew.rentbuysell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

public class Buy extends AppCompatActivity {
    private ViewPager mViewPager;
    private ViewPagerAdapter mSectionsPagerAdapter;
    public  users myuser ;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        myuser = new users(this);

        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);

        mSectionsPagerAdapter.addFragments(new AppliancesFragment(),"A");
        mSectionsPagerAdapter.addFragments(new BooksFragment(),"b");
        mSectionsPagerAdapter.addFragments(new SportsFragment(),"S");
        mSectionsPagerAdapter.addFragments(new FurnitureFragment(),"Furni");
        mSectionsPagerAdapter.addFragments(new FashionFragment(),"Fashion");
        mSectionsPagerAdapter.addFragments(new VehiclesFragment(),"Vehicles");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        final NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts);
        navigationTabStrip.setViewPager(mViewPager);





    }
}
