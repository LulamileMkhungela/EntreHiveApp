package com.example.academy_intern.sampledesign.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.Activities.UserProfileForAdmin;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InactiveUsers extends android.support.v4.app.Fragment {
    private static final String TAG = "MainActivity";
    // List of all dictionary words
    public ArrayList<UserProfile> inactiveUsers = new ArrayList <>();
    public ArrayList<UserProfile> filteredList = new ArrayList <>();
    // RecycleView adapter object
    public UserRecyclerViewAdapter mAdapter;
    // Search edit box
    public EditText searchBox;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inactive_users, container, false);

        getInactiveUsers();
//        searchBox = view.findViewById(R.id.search_box);
        recyclerView = view.findViewById(R.id.recycler_view_inactive_users_list);

        searchBox = getActivity().findViewById(R.id.et_search);

        // search suggestions using the edittext widget
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

    public void getInactiveUsers()
    {
        Call<ArrayList<UserProfile>> call = Api.getClient().getInactiveUsers();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<UserProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response)
            {
                inactiveUsers = response.body();
                filteredList.addAll(inactiveUsers);
                generateInactiveUsersList();
            }

            @Override
            public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateInactiveUsersList()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        assert recyclerView != null;
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserRecyclerViewAdapter(filteredList);
        recyclerView.setAdapter(mAdapter);
    }

    // create a custom RecycleViewAdapter class
    public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> implements Filterable {
        private ArrayList<UserProfile> mValues;
        private CustomFilter mFilter;
        private String name, email, dateOfRegistration, timeOfRegistration;

        public UserRecyclerViewAdapter(ArrayList<UserProfile> items) {
            mValues = items;
            mFilter = new CustomFilter(UserRecyclerViewAdapter.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            name = "Name: " + mValues.get(position).getName();
            email = "Email Address: " + mValues.get(position).getEmail();
            dateOfRegistration = " " + StringUtils.substring(mValues.get(position).getDateTime(), 0, 10) ;
            timeOfRegistration = " " + StringUtils.substring(mValues.get(position).getDateTime(), 11,16);

            holder.tvName.setText(name);
            holder.tvEmail.setText(email);
            holder.tvDateOfRegistration.setText(dateOfRegistration);
            holder.tvTimeOfRegistration.setText(timeOfRegistration);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvName, tvEmail, tvDateOfRegistration, tvTimeOfRegistration;

            CardView cvUserDetails;

            public ViewHolder(View view) {
                super(view);
                tvName = itemView.findViewById(R.id.tv_name_of_user);
                tvEmail = itemView.findViewById(R.id.tv_email_of_user);
                tvDateOfRegistration = itemView.findViewById(R.id.tv_registration_date);
                tvTimeOfRegistration = itemView.findViewById(R.id.tv_registration_time);

                cvUserDetails = itemView.findViewById(R.id.cv_user);

                cvUserDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Gson gson = new Gson();
                        UserProfile user = inactiveUsers.get(getAdapterPosition());
                        String serializedUser = gson.toJson(user);
                        Intent profileIntent = new Intent(v.getContext(), UserProfileForAdmin.class);
                        profileIntent.putExtra("User Details", serializedUser);
                        v.getContext().startActivity(profileIntent);
                    }
                });
            }
            @Override
            public String toString() {
                return super.toString() + " '" + tvEmail.getText() + "'";
            }
        }

        class CustomFilter extends Filter
        {
            private UserRecyclerViewAdapter mAdapter;
            private CustomFilter(UserRecyclerViewAdapter mAdapter)
            {
                super();
                this.mAdapter = mAdapter;
            }
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredList.addAll(inactiveUsers);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final UserProfile mWords : inactiveUsers) {
                        if (mWords.getName().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(mWords);
                        }
                    }
                }
                System.out.println("Count Number " + filteredList.size());
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println("Count Number 2 " + ((ArrayList<UserProfile>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }
}