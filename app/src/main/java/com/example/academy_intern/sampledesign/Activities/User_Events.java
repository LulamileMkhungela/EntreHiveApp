package com.example.academy_intern.sampledesign.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.Adapter.TabAdapter;
import com.example.academy_intern.sampledesign.Fragment.EventHistory;
import com.example.academy_intern.sampledesign.Fragment.TransactionHistory;
import com.example.academy_intern.sampledesign.Fragment.UpcomingEvents;
import com.example.academy_intern.sampledesign.R;

public class User_Events extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static User_Events newInstance()
    {
        User_Events fragment = new User_Events();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.admin_events, container, false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return view;
    }
//Performs the task of moving from one fragment to another
    private void setupViewPager(ViewPager viewPager)
    {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new UpcomingEvents(), "Tab1");
        adapter.addFragment(new EventHistory(), "Tab2");

        viewPager.setAdapter(adapter);
    }
//Method that retrieves the ID of the viewpager as well as text view
    private void setupTabIcons() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.admin_custom_tab, null);
        TextView tabOne =(TextView)view.findViewById(R.id.text);
        tabOne.setText("Upcoming");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.admin_custom_tab, null);
        TextView tabTwo =(TextView)view1.findViewById(R.id.text);
        tabTwo.setText("History");
        tabLayout.getTabAt(1).setCustomView(tabTwo);


    }
}