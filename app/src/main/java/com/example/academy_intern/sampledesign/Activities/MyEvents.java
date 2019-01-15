package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Fragment.EventQRCodeGenerator;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.IS_USER_ADMIN;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;

public class MyEvents extends AppCompatActivity {
    public ArrayList<EventProfile> myEvents = new ArrayList <>();
    public ArrayList<EventProfile> filteredList = new ArrayList <>();
    // RecycleView adapter object
    public EventRecyclerViewAdapter mAdapter;
    RecyclerView recyclerView;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        getUpcomingEvents();

        recyclerView = findViewById(R.id.recycler_view_my_events_list);
        btnBack = findViewById(R.id.toolB);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent dashIntent = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(dashIntent);
            }
        });

    }
    private void getUpcomingEvents()
    {
        Call<ArrayList<EventProfile>> call = Api.getClient().getMyEvents(LOGGED_IN_USER_ID);

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<EventProfile>>()
        {
            @Override
            public void onResponse(Call<ArrayList<EventProfile>> call, Response<ArrayList<EventProfile>> response)
            {
                myEvents = response.body();
                filteredList.addAll(myEvents);
                generateUpcomingEventsList();
            }

            @Override
            public void onFailure(Call<ArrayList<EventProfile>> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*The method, generateUpcomingEventsList, takes the array list which contains the event objects
     * and displays it in the RecyclerView. This is achieved by calling the EventAdapter class and
     * all the appropriate methods, such as setLayoutManager and setAdapter.*/
    public void generateUpcomingEventsList()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        assert recyclerView != null;
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EventRecyclerViewAdapter(filteredList);
        recyclerView.setAdapter(mAdapter);
    }

    public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder> implements Filterable {
        private ArrayList<EventProfile> mValues;
        private EventRecyclerViewAdapter.CustomFilter mFilter;
        private String title, event_description, date, time, event_location, event_id, event_date_time;

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
            title = "" + myEvents.get(position).getEventTitle();
            event_description = "Description: " + myEvents.get(position).getEventDescription();
            event_date_time = myEvents.get(position).getEventDateTime();
            date =" " + StringUtils.substring(myEvents.get(position).getEventDateTime(), 0, 10) ;
            time = " " + StringUtils.substring(myEvents.get(position).getEventDateTime(), 11,16);
            event_location = "Location: " + myEvents.get(position).getEventLocation();
            event_id = String.valueOf(myEvents.get(position).getEventId());


            holder.tvTitle.setText(title);
            holder.tvDescription.setText(event_description);
            holder.tvDate.setText(date);
            holder.tvTime.setText(time);
            holder.tvLocation.setText(event_location);
            holder.tvEventId.setText(event_id);
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

            TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation, tvEventId;

            CardView cardView;

            public ViewHolder(View view) {
                super(view);
                tvTitle = view.findViewById(R.id.tv_event_title);
                tvDescription = view.findViewById(R.id.tv_event_description);
                tvDate = view.findViewById(R.id.tv_event_date);
                tvTime = view.findViewById(R.id.tv_event_time);
                tvLocation = view.findViewById(R.id.tv_event_location);
                tvEventId = view.findViewById(R.id.tv_event_id);
                cardView = view.findViewById(R.id.cardview);


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent qrIntent = new Intent(v.getContext(), EventQRCode.class);

                        qrIntent.putExtra("event_id", event_id);
                        qrIntent.putExtra("title", title);

                        v.getContext().startActivity(qrIntent);
                    }
                });

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
                    filteredList.addAll(myEvents);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final EventProfile event : myEvents) {
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

//
//    private void getMyEvents()
//    {
//        Call<ArrayList<EventProfile>> call =Api.getClient().getMyEvents(7);
//
//        Log.wtf("URL Called", call.request().url() + "");
//
//        call.enqueue(new Callback<ArrayList<EventProfile>>()
//        {
//            @Override
//            public void onResponse(Call<ArrayList<EventProfile>> call, Response<ArrayList<EventProfile>> response)
//            {
//                myEvents = response.body();
//                generateMyEventList(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<EventProfile>> call, Throwable t)
//            {
//                Toast.makeText(MyEvents.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    /*The method, generateEventHistoryList, takes the array list which contains the event objects
//     * and displays it in the RecyclerView. This is achieved by calling the EventAdapter class and
//     * all the appropriate methods, such as setLayoutManager and setAdapter.*/
//    private void generateMyEventList(ArrayList<EventProfile> myEventlist)
//    {
//        recyclerView = findViewById(R.id.recycler_view_my_event_list);
//
//        adapter = new EventAdapter(myEventlist);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//
//        recyclerView.setLayoutManager(layoutManager);
//
//        recyclerView.setAdapter(adapter);
//    }
//
//    public class EventRecyclerViewAdapter extends RecyclerView.Adapter<MyEvents.EventRecyclerViewAdapter.ViewHolder>  {
////        private ArrayList<EventProfile> mValues;
////        private MyEvents.EventRecyclerViewAdapter.CustomFilter mFilter;
//        private String title, event_description, date, time, event_location, event_id, event_date_time;
//
////        public EventRecyclerViewAdapter(ArrayList<EventProfile> items) {
////            mValues = items;
////            mFilter = new MyEvents.EventRecyclerViewAdapter.CustomFilter(EventRecyclerViewAdapter.this);
////        }
//
//        @Override
//        public MyEvents.EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_event, parent, false);
//            return new MyEvents.EventRecyclerViewAdapter.ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final MyEvents.EventRecyclerViewAdapter.ViewHolder holder, int position)
//        {
//            title = "" + myEvents.get(position).getEventTitle();
//            event_description = "Description: " + myEvents.get(position).getEventDescription();
//            event_date_time = myEvents.get(position).getEventDateTime();
//            date =" " + StringUtils.substring(myEvents.get(position).getEventDateTime(), 0, 10) ;
//            time = " " + StringUtils.substring(myEvents.get(position).getEventDateTime(), 11,16);
//            event_location = "Location: " + myEvents.get(position).getEventLocation();
//            event_id = String.valueOf(myEvents.get(position).getEventId());
//
//            holder.tvTitle.setText(title);
//            holder.tvDescription.setText(event_description);
//            holder.tvDate.setText(date);
//            holder.tvTime.setText(time);
//            holder.tvLocation.setText(event_location);
//            holder.tvEventId.setText(event_id);
//        }
//
//        @Override
//        public int getItemCount() {
//            return myEvents.size();
//        }
//
////        @Override
////        public Filter getFilter() {
////            return mFilter;
////        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation, tvEventId;
//
//            CardView cardView;
//
//            public ViewHolder(View view) {
//                super(view);
//                tvTitle = view.findViewById(R.id.tv_event_title);
//                tvDescription = view.findViewById(R.id.tv_event_description);
//                tvDate = view.findViewById(R.id.tv_event_date);
//                tvTime = view.findViewById(R.id.tv_event_time);
//                tvLocation = view.findViewById(R.id.tv_event_location);
//                tvEventId = view.findViewById(R.id.tv_event_id);
//                cardView = view.findViewById(R.id.cardview);
//
//                cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("event_id", event_id);
//                        bundle.putString("title", title);
//                        EventQRCodeGenerator code = new EventQRCodeGenerator();
//                        code.setArguments(bundle);
//                        getFragmentManager().beginTransaction().replace(R.id.my_events, code).commit();
//                    }
//                });
//
//            }
//            @Override
//            public String toString() {
//                return super.toString() + " '" + tvTitle.getText() + "'";
//            }
//        }
//
////        class CustomFilter extends Filter
////        {
////            private UpcomingEvents.EventRecyclerViewAdapter mAdapter;
////            private CustomFilter(UpcomingEvents.EventRecyclerViewAdapter mAdapter)
////            {
////                super();
////                this.mAdapter = mAdapter;
////            }
////            @Override
////            protected FilterResults performFiltering(CharSequence constraint)
////            {
////                filteredList.clear();
////                final FilterResults results = new FilterResults();
////                if (constraint.length() == 0) {
////                    filteredList.addAll(myEvents);
////                } else {
////                    final String filterPattern = constraint.toString().toLowerCase().trim();
////                    for (final EventProfile event : myEvents) {
////                        if (event.getEventTitle().toLowerCase().startsWith(filterPattern)) {
////                            filteredList.add(event);
////                        }
////                    }
////                }
//////                System.out.println("Count Number " + filteredList.size());
////                results.values = filteredList;
////                results.count = filteredList.size();
////                return results;
////            }
////            @Override
////            protected void publishResults(CharSequence constraint, FilterResults results) {
//////                System.out.println("Count Number 2 " + ((ArrayList<EventProfile>) results.values).size());
////                this.mAdapter.notifyDataSetChanged();
////            }
////        }
//    }
//}
