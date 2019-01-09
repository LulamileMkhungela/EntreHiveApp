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
import com.example.academy_intern.sampledesign.Fragment.PendingEvents;
import com.example.academy_intern.sampledesign.Fragment.InactiveUsers;
import com.example.academy_intern.sampledesign.R;

public class Admin_verification extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static Admin_verification newInstance() {
        Admin_verification fragment = new Admin_verification();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.admin_verification, container, false);
        tabLayout=(TabLayout)view.findViewById(R.id.tab_layout);
        viewPager=(ViewPager)view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter =
                new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new InactiveUsers(), "Tab1");
        adapter.addFragment(new PendingEvents(), "Tab2");

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.admin_custom_tab, null);
        TextView tabOne = (TextView) view.findViewById(R.id.text);
        tabOne.setText("Pending Users");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.admin_custom_tab, null);
        TextView tabTwo = (TextView) view1.findViewById(R.id.text);
        tabTwo.setText("Pending Events");

        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }
}