package com.example.academy_intern.sampledesign.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.academy_intern.sampledesign.Fragment.Scanner;
import com.example.academy_intern.sampledesign.Fragment.TransactionHistory;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.SessionManager;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.USER_BALANCE;
import static com.example.academy_intern.sampledesign.Services.PointsWarningDialog.displayWarning;

public class UserDashboard extends AppCompatActivity
{
    Button btnpopupmenu;
    FloatingActionButton mFab;
    BottomNavigationView bottomNavigationView;
    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        mFab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.navigation);
        btnpopupmenu = findViewById(R.id.btn_profile);

        openCamera();

        accessDocuments();

        if (USER_BALANCE < 60)
        {
            insufficientPointsDialog(this);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        /*This onclick method is for the user to be able to direct to the various options that
          are provided to navigate to the selected item*/
        btnpopupmenu.setOnClickListener(new View.OnClickListener() {
            //Dropdown list for Profile,Points and Logout
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(UserDashboard.this, btnpopupmenu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.item1:
                                Intent profile = new Intent(UserDashboard.this, Profile.class);
                                startActivity(profile);
                                return true;

                            case R.id.item2:
                                Intent intent = new Intent(getApplicationContext(), ExpandableList.class);
                                startActivity(intent);
                                return true;

                            case R.id.item3:
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

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId())
                {
                    case R.id.navigation_events:
                        selectedFragment = User_Events.newInstance();
                        break;
                    case R.id.navigation_scan:
                        selectedFragment = Scanner.newInstance();
                        break;
                    case R.id.navigation_transactions:
                        selectedFragment = TransactionHistory.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.user_frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }

        };
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.user_frame_layout, User_Events.newInstance());
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener
                (mOnNavigationItemSelectedListener);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eventIntent = new Intent(UserDashboard.this, AddEvent.class);
                startActivity(eventIntent);
            }
        });
    }
//Method that opens the camera based on the provided permissions
    private void openCamera()
    {
        //Option to choose to allow access to camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        }
    }

    private void accessDocuments()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        }
    }

    //Method to notify the user of the current points balance each time the user lands on the dashboard depending on the minimum balance
    private void insufficientPointsDialog(Activity activity)
    {
        String message = "You currently have " + USER_BALANCE + " points. Please attend an event as soon as possible.";
        String title="Warning";
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        alertDialog.dismiss();
                    }
                });

        alertDialog.setIcon(R.drawable.funds);
        alertDialog.show();

    }

}