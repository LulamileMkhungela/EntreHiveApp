package com.example.academy_intern.sampledesign.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.PointsWarningDialog;
import com.example.academy_intern.sampledesign.Services.SessionManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.example.academy_intern.sampledesign.Services.PointsWarningDialog.displayWarning;

public class MainActivity extends AppCompatActivity {
    public static boolean IS_USER_LOGGED_IN;
    public static boolean IS_USER_ADMIN;
    public static boolean HAS_USER_SEEN_SLIDESHOW;
    public static int LOGGED_IN_USER_ID;
    public static int USER_BALANCE;
    public static String LOGGED_IN_USER_DETAILS;
    public static boolean NEW_PHOTO_ADDED = false;
    private final String TOPIC = "JavaSampleApproach";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        SessionManager sessionManager = new SessionManager(this);

        IS_USER_LOGGED_IN = sessionManager.isLoggedIn(); // a boolean value is stored in shared preferences
        LOGGED_IN_USER_ID = sessionManager.getLoggedInId(); // a logged in user's id is stored in shared preferences
        IS_USER_ADMIN = sessionManager.getUserRole();
        HAS_USER_SEEN_SLIDESHOW = sessionManager.firstTimeLaunch();
        USER_BALANCE = sessionManager.getUserBalance();

        /* If user has already registered or logged in, he/she shall be directed to the dashboard.
         * If the user is not registered, or has not logged in, he/she shall be directed to the
         * home page, where they can choose whether they'd like to register or log in. If the
         * user's role is that of an admin, then the user shall be directed to the admin dashboard.*/

        if(IS_USER_ADMIN)
        {
            Intent adminIntent = new Intent(this, AdminDashboard.class);
            startActivity(adminIntent);
        }

        else if (IS_USER_LOGGED_IN && LOGGED_IN_USER_ID != 0)
        {
            storeUserDetailsInString();
//            storeUserDetailsInHashMap();
            Intent userIntent = new Intent(this, UserDashboard.class);
            startActivity(userIntent);
        }

        else
        {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
        }

    }

    /* The user object, which has been fetched from the database, is converted to a string, using
     * the GSon converter. This method is static, since it needs to be called whenever a user logs
     * in or registers for the first time. */
    public static String storeUserDetailsInString()
    {
        Callback <UserProfile> responseCallback = new Callback <UserProfile>()
        {
            @Override
            public void onResponse(Call <UserProfile> call, Response <UserProfile> response)
            {
                Gson gson = new Gson();
                LOGGED_IN_USER_DETAILS = gson.toJson(response.body());
            }

            @Override
            public void onFailure(Call <UserProfile> call, Throwable t)
            {
                Log.d("Gson", t.getMessage());
            }
        };

        getUserDetailsFromDatabase(responseCallback);

        return LOGGED_IN_USER_DETAILS;
    }

    /* The user's details are fetched from the database. The details are stored as an object of the
     * class, UserProfile, which is the class that contains all the user's attributes and
     * corresponding getters, setters, and constructors. */
    public static void getUserDetailsFromDatabase(Callback<UserProfile> callback)
    {
        Api.getClient().retrieveUserDetails(LOGGED_IN_USER_ID).enqueue(callback);
    }
}