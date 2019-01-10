package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_DETAILS;

public class Profile extends AppCompatActivity {

    Button btnBack, editButton;
    TextView textMail,textNumber,textId,textName,textCompName,textCompNumber, textCurrentPoints, textTotalPoints, textUsedPoints;
    String username, phoneNo, name, compName, compNumber, idNo, currentPoints, totalPoints, usedPoints, photoPath;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialiseWidgets();

        /* When the user arrives at the dashboard, his/her details are retrieved from the database and saved inside
         *  a hash map. Once the profile page is opened, the relevant fields are populated with this data. */

        getUserDetails(LOGGED_IN_USER_DETAILS);
        setUserDetails();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent dashIntent = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(dashIntent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editProfileIntent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(editProfileIntent);
//                getSupportFragmentManager().beginTransaction().replace(R.id.profile, new com.example.academy_intern.sampledesign.Fragment.EditProfile()).commit();
            }
        });

    }

    public void setUserDetails()
    {
        textMail.setText(username);
        textId.setText(idNo);
        textName.setText(name);
        textNumber.setText(phoneNo);
        textCompName.setText(compName);
        textCompNumber.setText(compNumber);
        textCurrentPoints.setText(currentPoints);
        textTotalPoints.setText(totalPoints);
        textUsedPoints.setText(usedPoints);
        if (!MainActivity.NEW_PHOTO_ADDED)
        {
            Picasso.get().load(photoPath).resize(50,50).centerCrop().into(imageView);
        }
        else
        {
            Picasso.get().load(com.example.academy_intern.sampledesign.Activities.EditProfile.LOCAL_PHOTO_PATH).resize(50,50).centerCrop().into(imageView);
        }
    }

    private void initialiseWidgets()
    {
        btnBack = findViewById(R.id.toolB);
        editButton = findViewById(R.id.btn_edit);
        textMail = findViewById(R.id.text_mail);
        textNumber = findViewById(R.id.text_number);
        textId = findViewById(R.id.text_idno);
        textName = findViewById(R.id.text_name);
        textCompName = findViewById(R.id.company_name);
        textCompNumber = findViewById(R.id.company_number);
        textCurrentPoints = findViewById(R.id.current_points);
        textTotalPoints = findViewById(R.id.total_points);
        textUsedPoints = findViewById(R.id.used_points);
        imageView = findViewById(R.id.profile_image);
    }

    public void getUserDetails(String userDetails)
    {
        Gson gson = new Gson();
        UserProfile user = gson.fromJson(userDetails, UserProfile.class);
        username = user.getEmail();
        phoneNo = user.getPhoneNumber();
        idNo = user.getIdentityNumber();
        name = user.getName();
        compName = user.getCompanyName();
        compNumber = user.getCompanyNumber();
        currentPoints = String.valueOf(user.getPoints());
        totalPoints = String.valueOf(user.getTotalPoints());
        usedPoints = String.valueOf(user.getUsedPoints());
        photoPath = user.getPhotoPath();
    }

}
