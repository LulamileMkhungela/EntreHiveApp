package com.example.academy_intern.sampledesign.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.Activities.EventProfileForAdmin;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class PendingEventAdapter extends RecyclerView.Adapter<PendingEventAdapter.PendingEventViewHolder>
{

    private ArrayList<EventProfile> pendingeventsList;
    private String title, description, date, time, location, EventId;

    public PendingEventAdapter(ArrayList<EventProfile> pendingeventsList)
    {
        this.pendingeventsList = pendingeventsList;
    }

    /*The layout for this view is row_event.xml, which contains the textviews in which we shall set
     * information about events attended.*/
    @Override
    public PendingEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_pending_events, parent, false);
        return new PendingEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingEventViewHolder pendingEventViewHolder, int position) {

        title = "" + pendingeventsList.get(position).getEventTitle();
        description = "Description: " + pendingeventsList.get(position).getEventDescription();
        date = " " + StringUtils.substring(pendingeventsList.get(position).getEventDateTime(), 0, 10) ;
        time = " " + StringUtils.substring(pendingeventsList.get(position).getEventDateTime(), 11,16);
        location = "Location: " + pendingeventsList.get(position).getEventLocation();
        EventId = "Event Id: " +pendingeventsList.get(position).getEventId();

        pendingEventViewHolder.tvTitle.setText(title);
        pendingEventViewHolder.tvDescription.setText(description);
        pendingEventViewHolder.tvDate.setText(date);
        pendingEventViewHolder.tvTime.setText(time);
        pendingEventViewHolder.tvLocation.setText(location);
        pendingEventViewHolder.tvEventId.setText(EventId);
    }

    /*The void method, onBindViewHolder, allows for the desired data to be displayed in the view. It
     * takes, as one of its parameters, an instance of the EventViewHolder class. The EventViewHolder
     * class initialises the widgets. The onBindViewHolder method puts data and functionality into
     * the respective widgets.*/

    @Override
    public int getItemCount()
    {
        return pendingeventsList.size(); //number of objects in the arraylist
    }

    /*This class initialises the widgets that will be displayed in the view.*/
    class PendingEventViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation, tvEventId;

        CardView pendingEventsId;

        PendingEventViewHolder(View itemView)
        {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_event_title);
            tvDescription = itemView.findViewById(R.id.tv_event_description);
            tvDate = itemView.findViewById(R.id.tv_event_date);
            tvTime = itemView.findViewById(R.id.tv_event_time);
            tvLocation = itemView.findViewById(R.id.tv_event_location);
            tvEventId = itemView.findViewById(R.id.tv_event_id);


            pendingEventsId = itemView.findViewById(R.id.pendingEventsId);

            pendingEventsId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String EventId = org.apache.commons.lang3.StringUtils.substringAfter(tvEventId.getText().toString(), "Event Id: ");
                    String Description = org.apache.commons.lang3.StringUtils.substringAfter(tvDescription.getText().toString(), "Description: ");
                    String Date = org.apache.commons.lang3.StringUtils.substringAfter(tvDate.getText().toString(), "");
                    String Location = org.apache.commons.lang3.StringUtils.substringAfter(tvLocation.getText().toString(), "Location: ");
                    String Title = org.apache.commons.lang3.StringUtils.substringAfter(tvTitle.getText().toString(), "");
                    String Time = org.apache.commons.lang3.StringUtils.substringAfter(tvTime.getText().toString(), "Time: ");

                    Intent profileIntent = new Intent(v.getContext(), EventProfileForAdmin.class);

                    profileIntent.putExtra("EventId", EventId);
                    profileIntent.putExtra("Time", Time);
                    profileIntent.putExtra("Date", Date);
                    profileIntent.putExtra("Description", Description);
                    profileIntent.putExtra("Title", Title);
                    profileIntent.putExtra("Location", Location);


                    v.getContext().startActivity(profileIntent);

                }
            });
        }
    }
}
