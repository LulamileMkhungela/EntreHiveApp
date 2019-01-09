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

import com.example.academy_intern.sampledesign.Adapter.UserAdapter;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin1 on 10/2/17.
 */

public class Admin_Tab_All_Users extends Fragment {


    View view;
    private UserAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.all_users, container, false);

        Call<ArrayList<com.example.academy_intern.sampledesign.Model.UserProfile>> call = Api.getClient().fetchUsers();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<com.example.academy_intern.sampledesign.Model.UserProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<com.example.academy_intern.sampledesign.Model.UserProfile>> call, Response<ArrayList<com.example.academy_intern.sampledesign.Model.UserProfile>> response)
            {
                generateUserList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<com.example.academy_intern.sampledesign.Model.UserProfile>> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void generateUserList(ArrayList<UserProfile> listOfUsers)
    {
        recyclerView = view.findViewById(R.id.Userlist);

        adapter = new UserAdapter(listOfUsers);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }
}