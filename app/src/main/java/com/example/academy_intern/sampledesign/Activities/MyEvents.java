package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.Adapter.EventAdapter;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEvents extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    SessionManager sessionManager;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_events);
        back = findViewById(R.id.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MyEvents.this,UserDashboard.class);
                startActivity(intent);
            }
        });

       //getMyEvents();
    }

    private void getMyEvents()
    {
        Call<ArrayList<EventProfile>> call =Api.getClient().getMyEvents(MainActivity.LOGGED_IN_USER_ID);

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<EventProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<EventProfile>> call, Response<ArrayList<EventProfile>> response)
            {
                generateMyEventList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<EventProfile>> call, Throwable t)
            {
                Toast.makeText(MyEvents.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*The method, generateEventHistoryList, takes the arraylist which contains the event objects
     * and displays it in the RecyclerView. This is achieved by calling the EventAdapter class and
     * all the appropriate methods, such as setLayoutManager and setAdapter.*/
    private void generateMyEventList(ArrayList<EventProfile> myEventlist)
    {
        recyclerView = findViewById(R.id.recycler_view_my_event_list);

        adapter = new EventAdapter(myEventlist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }
}
