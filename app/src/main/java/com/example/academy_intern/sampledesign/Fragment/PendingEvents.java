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

import com.example.academy_intern.sampledesign.Activities.EventProfileForAdmin;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingEvents extends android.support.v4.app.Fragment
{
    public ArrayList<EventProfile> pendingEvents = new ArrayList <>();
    public ArrayList<EventProfile> filteredList = new ArrayList <>();
    // RecycleView adapter object
    public EventRecyclerViewAdapter mAdapter;
    // Search edit box
    public EditText searchBox;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.pending_events, container, false);

        getPendingEvents();

        recyclerView = view.findViewById(R.id.pendingEvents);

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

    private void getPendingEvents()
    {
        Call<ArrayList<EventProfile>> call = Api.getClient().getPendingEvents();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<EventProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<EventProfile>> call, Response<ArrayList<EventProfile>> response)
            {
                pendingEvents = response.body();
                filteredList.addAll(pendingEvents);
                generatePendingEventsList();
            }

            @Override
            public void onFailure(Call<ArrayList<EventProfile>> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* The method, generateEventHistoryList, takes the array list which contains the event objects
     * and displays it in the RecyclerView. This is achieved by calling the EventAdapter class and
     * all the appropriate methods, such as setLayoutManager and setAdapter.*/
    public void generatePendingEventsList()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        assert recyclerView != null;
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EventRecyclerViewAdapter(filteredList);
        recyclerView.setAdapter(mAdapter);
    }

    public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> implements Filterable {
        private ArrayList<EventProfile> mValues;
        private EventRecyclerViewAdapter.CustomFilter mFilter;
        private String title, description, date, time, location, userId, eventId;

        public EventRecyclerViewAdapter(ArrayList<EventProfile> items) {
            mValues = items;
            mFilter = new EventRecyclerViewAdapter.CustomFilter(EventRecyclerViewAdapter.this);
        }

        @Override
        public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pending_events, parent, false);
            return new EventRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final EventRecyclerViewAdapter.ViewHolder holder, int position)
        {
            title = "" + pendingEvents.get(position).getEventTitle();
            description = "Description: " + pendingEvents.get(position).getEventDescription();
            date =" " + StringUtils.substring(pendingEvents.get(position).getEventDateTime(), 0, 10) ;
            time = " " + StringUtils.substring(pendingEvents.get(position).getEventDateTime(), 11,16);
            location = "Location: " + pendingEvents.get(position).getEventLocation();
            userId = String.valueOf(pendingEvents.get(position).getUserId());
            eventId = String.valueOf(pendingEvents.get(position).getEventId());

            holder.tvTitle.setText(title);
            holder.tvDescription.setText(description);
            holder.tvDate.setText(date);
            holder.tvTime.setText(time);
            holder.tvLocation.setText(location);
            holder.tvUserId.setText(userId);
            holder.tvEventId.setText(eventId);
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

            TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation, tvEventId, tvUserId;

            CardView pendingEventsId;

            ViewHolder(View itemView)
            {
                super(itemView);

                tvTitle = itemView.findViewById(R.id.tv_event_title);
                tvDescription = itemView.findViewById(R.id.tv_event_description);
                tvDate = itemView.findViewById(R.id.tv_event_date);
                tvTime = itemView.findViewById(R.id.tv_event_time);
                tvLocation = itemView.findViewById(R.id.tv_event_location);
                tvEventId = itemView.findViewById(R.id.tv_event_id);
                tvUserId = itemView.findViewById(R.id.tv_user_id);

                pendingEventsId = itemView.findViewById(R.id.pendingEventsId);

                pendingEventsId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String EventId = tvEventId.getText().toString();
                        String Description = org.apache.commons.lang3.StringUtils.substringAfter(tvDescription.getText().toString(), "Description: ");
                        String Date = org.apache.commons.lang3.StringUtils.substringAfter(tvDate.getText().toString(), "");
                        String Location = org.apache.commons.lang3.StringUtils.substringAfter(tvLocation.getText().toString(), "Location: ");
                        String Title = org.apache.commons.lang3.StringUtils.substringAfter(tvTitle.getText().toString(), "");
                        String Time = org.apache.commons.lang3.StringUtils.substringAfter(tvTime.getText().toString(), "Time: ");
                        String userId = tvUserId.getText().toString();


                        Intent profileIntent = new Intent(v.getContext(), EventProfileForAdmin.class);

                        profileIntent.putExtra("EventId", EventId);
                        profileIntent.putExtra("Time", Time);
                        profileIntent.putExtra("Date", Date);
                        profileIntent.putExtra("Description", Description);
                        profileIntent.putExtra("Title", Title);
                        profileIntent.putExtra("Location", Location);
                        profileIntent.putExtra("UserId", userId);

                        v.getContext().startActivity(profileIntent);

                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + tvDescription.getText() + "'";
            }
        }

        class CustomFilter extends Filter
        {
            private EventRecyclerViewAdapter mAdapter;
            private CustomFilter(EventRecyclerViewAdapter mAdapter)
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
                    filteredList.addAll(pendingEvents);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final EventProfile mWords : pendingEvents) {
                        if (mWords.getEventTitle().toLowerCase().startsWith(filterPattern)) {
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
                System.out.println("Count Number 2 " + ((ArrayList<EventProfile>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }
}

