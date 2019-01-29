package com.example.academy_intern.sampledesign.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.IS_USER_ADMIN;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.USER_BALANCE;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.storeUserDetailsInString;

public class SubscribeToAnEvent extends AppCompatActivity {

    Button btn_subscribe, btn_reject;
    int event_id;
    public static int LOGGED_IN_USER_ID;
    public static boolean IS_USER_LOGGED_IN;
    String event_date_time = "nothing";
    String event_description = "nothing";
    String event_location = "nothing";
    String title = "nothing";
    String attendance_points = "nothing";

    TextView txt_date_time,txt_location,txt_title,txt_description, txt_attendance_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_to_an_event);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Intent intent_o = getIntent();
        event_id = Integer.parseInt(intent_o.getStringExtra("event_id"));
        event_date_time = intent_o.getStringExtra("event_date_time");
        event_description = intent_o.getStringExtra("event_description");
        event_location = intent_o.getStringExtra("event_location");
        title = intent_o.getStringExtra("title");
        attendance_points = intent_o.getStringExtra("attendance_points");
        if (attendance_points == null || attendance_points.equals("0"))
        {
            attendance_points = "Unknown number of";
        }

        txt_location = findViewById(R.id.txt_location);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_description = findViewById(R.id.txt_description);
        txt_title = findViewById(R.id.txt_title);
        txt_attendance_points = findViewById(R.id.txt_points);
        btn_subscribe =  findViewById(R.id.btn_subscribe);
        btn_reject = findViewById(R.id.btn_reject);

        txt_date_time.setText(event_date_time);
        txt_location.setText(event_location);
        txt_description.setText(event_description);
        txt_title.setText(title);
        txt_attendance_points.setText(attendance_points + " points to be awarded");

        SessionManager sessionManager = new SessionManager(this);
        LOGGED_IN_USER_ID = sessionManager.getLoggedInId();
        IS_USER_LOGGED_IN = sessionManager.isLoggedIn();
        IS_USER_ADMIN = sessionManager.getUserRole();

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SessionManager sessionManager = new SessionManager(getApplicationContext());
                //Receives getClient instance from the Api
                Call<String> call = Api.getClient().cancelAttendance(LOGGED_IN_USER_ID, event_id);

                call.enqueue(new Callback<String>()
                {
                    @Override
                    public void onResponse(Call <String> call, Response<String> response)
                    {
                        String message = response.body();
                        if (message != null)
                        {
                            storeUserDetailsInString();
                            cancellationDialog(SubscribeToAnEvent.this, message);
                        }
                        else
                        {
                            SubscribeToAnEvent.super.onBackPressed();
                        }

                    }

                    @Override
                    public void onFailure(Call <String> call, Throwable t)
                    {
                        Log.d("response", t.getStackTrace().toString());
                    }
                });

            }
        });

        btn_subscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                final SessionManager sessionManager = new SessionManager(getApplicationContext());
                //Receives getClient instance from the Api
                Call<String> call = Api.getClient().subscribeToEvent(LOGGED_IN_USER_ID, event_id);

                call.enqueue(new Callback<String>()
                {
                    @Override
                    public void onResponse(Call <String> call, Response<String> response)
                    {
                        String message = response.body();
                        storeUserDetailsInString();
                        subscriptionDialog(SubscribeToAnEvent.this, message);
                    }

                    @Override
                    public void onFailure(Call <String> call, Throwable t)
                    {
                        Log.d("response", t.getStackTrace().toString());
                    }
                });

            }
        });
    }

    private void subscriptionDialog(Activity activity, String message)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Subscription Status");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        if(IS_USER_LOGGED_IN)
                        {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);

                        }

                        if(IS_USER_ADMIN)
                        {
                            Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                            startActivity(intent);
                        }
                    }
                });

//        alertDialog.setIcon(R.drawable.success);
        alertDialog.show();

    }

    private void cancellationDialog(Activity activity, String message)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Cancellation");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        if(IS_USER_LOGGED_IN)
                        {
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);

                        }

                        if(IS_USER_ADMIN)
                        {
                            Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                            startActivity(intent);
                        }
                    }
                });

//        alertDialog.setIcon(R.drawable.success);
        alertDialog.show();

    }

}
