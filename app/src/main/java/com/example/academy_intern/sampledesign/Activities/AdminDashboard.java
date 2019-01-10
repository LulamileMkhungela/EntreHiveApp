package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.academy_intern.sampledesign.Fragment.Admin_Events;
import com.example.academy_intern.sampledesign.Fragment.Admin_Points;
import com.example.academy_intern.sampledesign.Fragment.Admin_Users;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.SessionManager;

public class AdminDashboard extends AppCompatActivity {

   Button btnpopupmenu;
EditText searchBox;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Admin Dashboard");
//        setSupportActionBar(toolbar);

//        btnSearch = findViewById(R.id.btnSearch);
        searchBox = findViewById(R.id.et_search);
        btnpopupmenu = findViewById(R.id.btn_profile_admin);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        //Navigates you to the selected option
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.action_item1:
                        selectedFragment = Admin_Events.newInstance();
                        break;
                    case R.id.action_item2:
                        selectedFragment = Admin_verification.newInstance();
                        break;
                    case R.id.action_item3:
                        selectedFragment = Admin_Points.newInstance();
                        break;
                    case R.id.action_item4:
                        selectedFragment = Admin_Users.newInstance();
                        break;


                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.admin_frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }

        };
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_frame_layout, Admin_Events.newInstance());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener
                (mOnNavigationItemSelectedListener);


        btnpopupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This is for the functionality of the hamburger icon that takes you to options to choose from
                PopupMenu popupMenu = new PopupMenu(AdminDashboard.this, btnpopupmenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main_admin, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.add_admin:
                                Intent profile = new Intent(AdminDashboard.this, AddAdminActivity.class);
                                startActivity(profile);
                                return true;

                            case R.id.logout:
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                sessionManager.logoutUser();
                                Intent logoutIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(logoutIntent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        return true;
    }

}