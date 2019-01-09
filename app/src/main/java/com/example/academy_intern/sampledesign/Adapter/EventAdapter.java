package com.example.academy_intern.sampledesign.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/*The EventAdapter class provides the template for how the user's event history shall be displayed
 * in the application. A RecyclerView and CardView are used.*/
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{

    private ArrayList<EventProfile> eventsList;
    private String title, description, date, time, location;

    public EventAdapter(ArrayList<EventProfile> eventsList)
    {
        this.eventsList = eventsList;
    }

    /*The layout for this view is row_event.xml, which contains the textviews in which we shall set
     * information about events attended.*/
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_event, parent, false);
        return new EventViewHolder(view);
    }

    /*The void method, onBindViewHolder, allows for the desired data to be displayed in the view. It
     * takes, as one of its parameters, an instance of the EventViewHolder class. The EventViewHolder
     * class initialises the widgets. The onBindViewHolder method puts data and functionality into
     * the respective widgets.*/
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position)
    {
        title = "" + eventsList.get(position).getEventTitle();
        description = "Description: " + eventsList.get(position).getEventDescription();
        date =" " + StringUtils.substring(eventsList.get(position).getEventDateTime(), 0, 10) ;
        time = " " + StringUtils.substring(eventsList.get(position).getEventDateTime(), 11,16);
        location = "Location: " + eventsList.get(position).getEventLocation();

        holder.tvTitle.setText(title);
        holder.tvDescription.setText(description);
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);
        holder.tvLocation.setText(location);
    }

    @Override
    public int getItemCount()
    {
        return eventsList.size(); //number of objects in the arraylist
    }

    /*This class initialises the widgets that will be displayed in the view.*/
    class EventViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvTitle, tvDescription, tvDate, tvTime, tvLocation;

        EventViewHolder(View itemView)
        {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_event_title);
            tvDescription = itemView.findViewById(R.id.tv_event_description);
            tvDate = itemView.findViewById(R.id.tv_event_date);
            tvTime = itemView.findViewById(R.id.tv_event_time);
            tvLocation = itemView.findViewById(R.id.tv_event_location);
        }
    }
}