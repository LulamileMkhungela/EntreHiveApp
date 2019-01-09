package com.example.academy_intern.sampledesign.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.Adapter.EventAdapter;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;

public class EventHistory extends AppCompatActivity
{

    private EventAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);

        Call<ArrayList<EventProfile>> call = Api.getClient().getEventHistory(LOGGED_IN_USER_ID);

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<EventProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<EventProfile>> call, Response<ArrayList<EventProfile>> response)
            {
                generateEventHistoryList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<EventProfile>> call, Throwable t)
            {
                Toast.makeText(EventHistory.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateEventHistoryList(ArrayList<EventProfile> listOfEvents)
    {
        recyclerView = findViewById(R.id.recycler_view_event_history_list);

        adapter = new EventAdapter(listOfEvents);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(EventHistory.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }
}
