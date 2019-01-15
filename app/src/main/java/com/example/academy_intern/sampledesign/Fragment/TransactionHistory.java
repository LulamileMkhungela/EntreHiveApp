package com.example.academy_intern.sampledesign.Fragment;

import android.content.pm.ActivityInfo;
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
import com.example.academy_intern.sampledesign.Adapter.TransactionAdapter;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.Model.ItemProfile;
import com.example.academy_intern.sampledesign.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;

public class TransactionHistory extends Fragment
{

    View view;
    private TransactionAdapter adapter;
    private RecyclerView recyclerView;

    public static TransactionHistory newInstance()
    {
        TransactionHistory fragment = new TransactionHistory();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.transaction_history, container, false);

        getTransactionHistory();



        return view;
    }

    private void getTransactionHistory()
    {
        Call<ArrayList<ItemProfile>> call = Api.getClient().getTransactionHistory(LOGGED_IN_USER_ID);

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<ItemProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<ItemProfile>> call, Response<ArrayList<ItemProfile>> response)
            {
                generateTransactionHistoryList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ItemProfile>> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*The method, generateEventHistoryList, takes the arraylist which contains the event objects
     * and displays it in the RecyclerView. This is achieved by calling the EventAdapter class and
     * all the appropriate methods, such as setLayoutManager and setAdapter.*/
    private void generateTransactionHistoryList(ArrayList<ItemProfile> listOfTransactions)
    {
        recyclerView = view.findViewById(R.id.recycler_view_transaction_history_list);

        adapter = new TransactionAdapter(listOfTransactions);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }
}