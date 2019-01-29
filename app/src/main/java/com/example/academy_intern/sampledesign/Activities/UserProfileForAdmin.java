package com.example.academy_intern.sampledesign.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.InputMessageDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileForAdmin extends AppCompatActivity
{
    TextView tvName, tvIdNumber, tvEmail, tvPhoneNumber, tvDocuments, tvCurrentPoints, tvCompanyName, tvCompanyNumber;
    ImageView ivPhoto, ivDocuments;
    Button btnAccept, btnReject, btnBack;
    ProgressDialog activateProgress;

    int userId;
    boolean userStatus;
    String name, email, idNumber, phoneNumber, companyName, companyNumber,
            currentPoints, photoPath, deviceToken;

    static String MESSAGE_TO_SEND_TO_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_adminprofile);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        activateProgress = new ProgressDialog(this);

        /*The user's email address is received from a different class, UserAdapter. This is done to ensure that
        * when the admin clicks the cardview of a particular user, the profile of that particular
        * user is shown. */

        UserProfile user = getUserDetails();

        userId = user.getUserId();
        name = user.getName();
        email = user.getEmail();
        userStatus = user.getActive();
        currentPoints = String.valueOf(user.getPoints());
        idNumber = user.getIdentityNumber();
        companyName = user.getCompanyName();
        companyNumber = user.getCompanyNumber();
        phoneNumber = user.getPhoneNumber();
        photoPath = user.getPhotoPath();
        deviceToken = user.getDeviceToken();

        initialiseWidgets();

        setUserProfile();

        ivDocuments.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent documentIntent = new Intent(UserProfileForAdmin.this, UserDocuments.class);
                documentIntent.putExtra("User Id", userId);

                startActivity(documentIntent);
            }
        });

        tvDocuments.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent documentIntent = new Intent(UserProfileForAdmin.this, UserDocuments.class);
                documentIntent.putExtra("User Id", userId);
                startActivity(documentIntent);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserProfileForAdmin.super.onBackPressed();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (userStatus)
                {
                    Toast.makeText(getApplicationContext(), "User is already active.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    activateProgress.setMessage("Please wait...");
                    activateProgress.show();
                    acceptRegistration();
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rejectRegistration();
            }
        });
    }

        private void setUserProfile()
        {
            tvName.setText(name);
            tvIdNumber.setText(idNumber);
            tvPhoneNumber.setText(phoneNumber);
            tvEmail.setText(email);
            tvCurrentPoints.setText(currentPoints);
            tvCompanyName.setText(companyName);
            tvCompanyNumber.setText(companyNumber);
            Picasso.get().load(photoPath).resize(100,100).centerCrop().into(ivPhoto);
        }

        private void initialiseWidgets()
        {
            tvName = findViewById(R.id.name);
            tvIdNumber = findViewById(R.id.text_idno);
            tvEmail = findViewById(R.id.text_mail);
            tvPhoneNumber = findViewById(R.id.text_number);
            tvCurrentPoints = findViewById(R.id.points);
            tvCompanyName = findViewById(R.id.tv_company_name);
            tvCompanyNumber = findViewById(R.id.tv_company_number);
            tvDocuments = findViewById(R.id.tv_documents);
            ivDocuments = findViewById(R.id.iv_documents);
            ivPhoto = findViewById(R.id.profile);
            btnAccept = findViewById(R.id.btn_accept);
            btnReject = findViewById(R.id.btn_reject);
            btnBack = findViewById(R.id.btn_back_to_admin);
        }

        private void acceptRegistration()
        {
            String message = "Your registration has been approved.";

            Call <UserProfile> call = Api.getClient().acceptRegistration(userId, deviceToken, message);


            call.enqueue(new Callback <UserProfile>()
            {

                @Override
                public void onResponse(Call <UserProfile> call, Response <UserProfile> response)
                {
                    Log.v("this", "Yes!");
                    if (response.body().getName() != null)
                    {
                        activateProgress.dismiss();
                       // Toast.makeText(getApplicationContext(), "User's account has been activated.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        activateProgress.dismiss();
                       // Toast.makeText(getApplicationContext(), "For some unknown reason, the user's account has not been activated.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call <UserProfile> call, Throwable t) {
                    // close it after response
                    Log.v("this", "No Response!");
                    activateProgress.dismiss();
                 //   Toast.makeText(getApplicationContext(), "Unable to reach the server. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    private void rejectRegistration()
    {
        new InputMessageDialog(UserProfileForAdmin.this, new InputMessageDialog.InputSenderDialogListener()
        {
            @Override
            public void onOK(final String message)
            {
                Log.d("Send", "The admin sent a message: " + message);

                MESSAGE_TO_SEND_TO_USER = message;

        Call <String> call = Api.getClient().sentNotificationToUser(deviceToken, MESSAGE_TO_SEND_TO_USER);

                call.enqueue(new Callback <String>()
                {

                    @Override
                    public void onResponse(Call <String> call, Response <String> response)
                    {
                        Log.v("this", "Yes!");
                    }

                    @Override
                    public void onFailure(Call <String> call, Throwable t) {
                        // close it after response
                        Log.v("this", "No Response!");
                    }
                });
            }

            @Override
            public void onCancel()
            {
                Log.d("Cancel", "The admin did not send a message.");
            }
        }).setMessage(MESSAGE_TO_SEND_TO_USER).show();


    }

    public UserProfile getUserDetails()
    {
        Intent profileIntent = getIntent();
        Gson gson = new Gson();
        String serializedUser = profileIntent.getStringExtra("User Details");
        UserProfile user = gson.fromJson(serializedUser, UserProfile.class);
        return user;
    }


}
