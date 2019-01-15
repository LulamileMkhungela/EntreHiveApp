package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class SubscribeToAnEvent extends AppCompatActivity {

    Button btn_subscribe, btn_reject;
    int event_id;
    public static int LOGGED_IN_USER_ID;
    public static boolean IS_USER_LOGGED_IN;
    String event_date_time = "nothing";
    String event_description = "nothing";
    String event_location = "nothing";
    String title = "nothing";

    TextView txt_date_time,txt_location,txt_title,txt_description;

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

        txt_location = findViewById(R.id.txt_location);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_description = findViewById(R.id.txt_description);
        txt_title = findViewById(R.id.txt_title);
        btn_subscribe =  findViewById(R.id.btn_subscribe);
        btn_reject = findViewById(R.id.btn_reject);

        txt_date_time.setText(event_date_time);
        txt_location.setText(event_location);
        txt_description.setText(event_description);
        txt_title.setText(title);

        SessionManager sessionManager = new SessionManager(this);
        LOGGED_IN_USER_ID = sessionManager.getLoggedInId();
        IS_USER_LOGGED_IN = sessionManager.isLoggedIn();
        IS_USER_ADMIN = sessionManager.getUserRole();

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscribeToAnEvent.super.onBackPressed();
            }
        });

        btn_subscribe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                //Receives getClient instance from the Api
                Call<Void> call = Api.getClient().subscribeToEvent(LOGGED_IN_USER_ID, event_id);

                call.enqueue(new Callback<Void>()
                {
                    @Override
                    public void onResponse(Call <Void> call, Response<Void> response)
                    {
                        Toast.makeText(getApplicationContext(),"You have been subscribed to the event.", Toast.LENGTH_LONG).show();

                        if(IS_USER_LOGGED_IN){

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

                    @Override
                    public void onFailure(Call <Void> call, Throwable t)
                    {
                        Log.d("response", t.getStackTrace().toString());
                    }
                });

            }
        });



    }

}
