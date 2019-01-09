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
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastEvents extends android.support.v4.app.Fragment {
    private static final String TAG = "MainActivity";
    // List of all dictionary words
    public ArrayList<EventProfile> pastEvents = new ArrayList <>();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.past_events, container, false);

        getPastEvents();
//        searchBox = view.findViewById(R.id.search_box);
        recyclerView = view.findViewById(R.id.recycler_view_past_events_list);

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

    public void getPastEvents()
    {
        Call<ArrayList<EventProfile>> call = Api.getClient().getPastEvents();

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<EventProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<EventProfile>> call, Response<ArrayList<EventProfile>> response)
            {
                pastEvents = response.body();
                filteredList.addAll(pastEvents);
                generatePastEventsList();
            }

            @Override
            public void onFailure(Call<ArrayList<EventProfile>> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generatePastEventsList()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        assert recyclerView != null;
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EventRecyclerViewAdapter(filteredList);
        recyclerView.setAdapter(mAdapter);
    }

//    public interface FilterActiveUsersListener
//    {
//        public void filterActiveUsers();
//    }
//
//    FilterActiveUsersListener mListener;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try
//        {
//            mListener = (FilterActiveUsersListener) context;
//        }
//        catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement FilterActiveUsersListener");
//        }
//    }

    // create a custom RecycleViewAdapter class
    public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> implements Filterable {
        private ArrayList<EventProfile> mValues;
        private CustomFilter mFilter;
        private String title, description, date, time, location;

        public EventRecyclerViewAdapter(ArrayList<EventProfile> items) {
            mValues = items;
            mFilter = new CustomFilter(EventRecyclerViewAdapter.this);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            title = "" + pastEvents.get(position).getEventTitle();
            description = "Description: " + pastEvents.get(position).getEventDescription();
            date =" " + StringUtils.substring(pastEvents.get(position).getEventDateTime(), 0, 10) ;
            time = " " + StringUtils.substring(pastEvents.get(position).getEventDateTime(), 11,16);
            location = "Location: " + pastEvents.get(position).getEventLocation();

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


            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_event_title);
                tvDescription = itemView.findViewById(R.id.tv_event_description);
                tvDate = itemView.findViewById(R.id.tv_event_date);
                tvTime = itemView.findViewById(R.id.tv_event_time);
                tvLocation = itemView.findViewById(R.id.tv_event_location);
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
                    filteredList.addAll(pastEvents);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final EventProfile event : pastEvents) {
                        if (event.getEventTitle().toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(event);
//                            Collections.reverse(filteredList);
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
//                System.out.println("Count Number 2 " + ((ArrayList<UserProfile>) results.values).size());
//                filteredList = (ArrayList<EventProfile>) results.values;
//                notifyDataSetChanged();
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }
}