package com.example.academy_intern.sampledesign.Fragment;

import android.os.Bundle;
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

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingEvents extends android.support.v4.app.Fragment
{
    public ArrayList<EventProfile> upcomingEvents = new ArrayList <>();
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
        View view = inflater.inflate(R.layout.upcoming_events, container, false);

        getUpcomingEvents();


        recyclerView = view.findViewById(R.id.recycler_view_upcoming_events_list);

//        searchBox = getActivity().findViewById(R.id.et_search);
//
//        // search suggestions using the edittext widget
//        searchBox.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                mAdapter.getFilter().filter(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//
       return view;
    }


    private void getUpcomingEvents()
    {
        Call<ArrayList<EventProfile>> call = Api.getClient().getUpcomingEvents();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<EventProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<EventProfile>> call, Response<ArrayList<EventProfile>> response)
            {
                upcomingEvents = response.body();
                filteredList.addAll(upcomingEvents);
                generateUpcomingEventsList();
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
    public void generateUpcomingEventsList()
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
        private String title, description, date, time, location;

        public EventRecyclerViewAdapter(ArrayList<EventProfile> items) {
            mValues = items;
            mFilter = new EventRecyclerViewAdapter.CustomFilter(EventRecyclerViewAdapter.this);
        }

        @Override
        public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
            return new EventRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final EventRecyclerViewAdapter.ViewHolder holder, int position)
        {
            title = "" + upcomingEvents.get(position).getEventTitle();
            description = "Description: " + upcomingEvents.get(position).getEventDescription();
            date =" " + StringUtils.substring(upcomingEvents.get(position).getEventDateTime(), 0, 10) ;
            time = " " + StringUtils.substring(upcomingEvents.get(position).getEventDateTime(), 11,16);
            location = "Location: " + upcomingEvents.get(position).getEventLocation();

            holder.tvTitle.setText(title);
            holder.tvDescription.setText(description);
            holder.tvDate.setText(date);
            holder.tvTime.setText(time);
            holder.tvLocation.setText(location);
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

            TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation;

            public ViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tv_event_title);
                tvDescription = view.findViewById(R.id.tv_event_description);
                tvDate = view.findViewById(R.id.tv_event_date);
                tvTime = view.findViewById(R.id.tv_event_time);
                tvLocation = view.findViewById(R.id.tv_event_location);
            }
            @Override
            public String toString() {
                return super.toString() + " '" + tvTitle.getText() + "'";
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
                    filteredList.addAll(upcomingEvents);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final EventProfile event : upcomingEvents) {
                        if (event.getEventTitle().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(event);
                        }
                    }
                }
//                System.out.println("Count Number " + filteredList.size());
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                System.out.println("Count Number 2 " + ((ArrayList<EventProfile>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }
}
