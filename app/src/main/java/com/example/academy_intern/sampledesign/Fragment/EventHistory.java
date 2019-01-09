package com.example.academy_intern.sampledesign.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EventHistory extends Fragment
{

    View view;
    private EventAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.activity_event_history, container, false);

        getEventHistory();

        return view;
    }

    private void getEventHistory()
    {
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
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*The method, generateEventHistoryList, takes the arraylist which contains the event objects
    * and displays it in the RecyclerView. This is achieved by calling the EventAdapter class and
    * all the appropriate methods, such as setLayoutManager and setAdapter.*/
    private void generateEventHistoryList(ArrayList<EventProfile> listOfEvents)
    {
        recyclerView = view.findViewById(R.id.recycler_view_event_history_list);

        adapter = new EventAdapter(listOfEvents);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }
}