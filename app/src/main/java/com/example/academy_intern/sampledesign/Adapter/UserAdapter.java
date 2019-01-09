package com.example.academy_intern.sampledesign.Adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.Activities.UserProfileForAdmin;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/*The UserAdapter class provides the template for how the user's event history shall be displayed
 * in the application. A RecyclerView and CardView are used.*/
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList <UserProfile> usersList;
    private String name, email, dateOfRegistration, timeOfRegistration;

    public UserAdapter(ArrayList<UserProfile> usersList)
    {
        this.usersList = usersList;
    }

    /*The layout for this view is row_event.xml, which contains the textviews in which we shall set
     * information about events attended.*/
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_user, parent, false);
        return new UserViewHolder(view);
    }

    /*The void method, onBindViewHolder, allows for the desired data to be displayed in the view. It
     * takes, as one of its parameters, an instance of the UserViewHolder class. The UserViewHolder
     * class initialises the widgets. The onBindViewHolder method puts data and functionality into
     * the respective widgets.*/
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position)
    {
//        userId = usersList.get(position).getUserId();
        name = "Name: " + usersList.get(position).getName();
        email = "Email Address: " + usersList.get(position).getEmail();
        dateOfRegistration = " " + StringUtils.substring(usersList.get(position).getDateTime(), 0, 10) ;
        timeOfRegistration =" " +  StringUtils.substring(usersList.get(position).getDateTime(), 11,16);

        holder.tvName.setText(name);
        holder.tvEmail.setText(email);
        holder.tvDateOfRegistration.setText(dateOfRegistration);
        holder.tvTimeOfRegistration.setText(timeOfRegistration);
    }

    @Override
    public int getItemCount()
    {
        return usersList.size(); //number of objects in the arraylist
    }

    /*This class initialises the widgets that will be displayed in the view.*/
    class UserViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvName, tvEmail, tvDateOfRegistration,tvTimeOfRegistration;

        CardView cvUserDetails;

        UserViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_of_user);
            tvEmail = itemView.findViewById(R.id.tv_email_of_user);
            tvDateOfRegistration = itemView.findViewById(R.id.tv_registration_date);
            tvTimeOfRegistration = itemView.findViewById(R.id.tv_registration_time);

            cvUserDetails = itemView.findViewById(R.id.cv_user);

            cvUserDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    UserProfile user = usersList.get(getAdapterPosition());
                    String serializedUser = gson.toJson(user);
                    Intent profileIntent = new Intent(v.getContext(), UserProfileForAdmin.class);
                    profileIntent.putExtra("User Details", serializedUser);
                    v.getContext().startActivity(profileIntent);
                }
            });
        }
    }
}