package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.InputMessageDialog;
import com.example.academy_intern.sampledesign.Services.SessionManager;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.IS_USER_ADMIN;

public class Event_Profile extends AppCompatActivity {


    public static boolean IS_USER_LOGGED_IN;
    public static boolean IS_USER_ADMIN;
    public static int LOGGED_IN_USER_ID;
    TextView txt_title, txt_date_time, txt_location, txt_description;
    Button btnaccept, btndocument, btndecline;
    EditText txt_number_of_people;
    String number_of_people;
    String EventId, deviceToken;

    static String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__profile);


        Intent profileIntent = getIntent();

        EventId = profileIntent.getStringExtra("EventId");
        String Time = profileIntent.getStringExtra("Time");
        String Date = profileIntent.getStringExtra("Date");
        String Description = profileIntent.getStringExtra("Description");
        String Title = profileIntent.getStringExtra("Title");
        String Location = profileIntent.getStringExtra("Location");
        String UserId = profileIntent.getStringExtra("UserId");

        final int id = Integer.parseInt(UserId);

//        Toast.makeText(getApplicationContext(),UserId+"",Toast.LENGTH_LONG).show();

        deviceToken =  getUserToken(id);

        String DateTime = Date+" "+Time;
        txt_title = findViewById(R.id.txt_title);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_location = findViewById(R.id.txt_location);
        txt_description = findViewById(R.id.txt_description);
        txt_number_of_people = findViewById(R.id.txt_number_of_people);

        btnaccept = findViewById(R.id.btn_accept);
        btndecline = findViewById(R.id.btn_reject);
        btndocument = findViewById(R.id.btn_documents);

        txt_title.setText(Title);
        txt_location.setText(Location);
        txt_date_time.setText(DateTime);
        txt_description.setText(Description);

        SessionManager sessionManager = new SessionManager(this);
        LOGGED_IN_USER_ID = sessionManager.getLoggedInId();
        IS_USER_LOGGED_IN = sessionManager.isLoggedIn();
        IS_USER_ADMIN = sessionManager.getUserRole();

        btnaccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                number_of_people = txt_number_of_people.getText().toString();

                Call<Void> call = Api.getClient().acceptEvent(EventId,number_of_people);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        Toast.makeText(Event_Profile.this, "Request was made successfully...",Toast.LENGTH_LONG).show();

                        if(IS_USER_LOGGED_IN){

                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);

                        }

                        if(IS_USER_ADMIN)
                        {
                            Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {


                    }
                });

            }
        });

        btndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rejectRegistration();

            }
        });

        btndocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent adminIntent = new Intent(getApplicationContext(), UserEventDocuments.class);
                adminIntent.putExtra("User Id", id);

                startActivity(adminIntent);
            }
        });

    }

    private void rejectRegistration()
    {
        String messageToSendToUser = "";

        new InputMessageDialog(Event_Profile.this, new InputMessageDialog.InputSenderDialogListener()
        {
            @Override
            public void onOK(final String message)
            {
                Log.d("Send", "The admin sent a message: " + message);
            }

            @Override
            public void onCancel()
            {
                Log.d("Cancel", "The admin did not send a message.");
            }
        }).setMessage(messageToSendToUser).show();

        Call <String> call = Api.getClient().sentNotificationToUser(deviceToken, messageToSendToUser);

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


    public String getUserToken(int userId){

        Call <ResponseBody> call = Api.getClient().getUserToken(userId);

        call.enqueue(new Callback <ResponseBody>()
        {

            @Override
            public void onResponse(Call <ResponseBody> call, Response <ResponseBody> response)
            {
                token = String.valueOf(response.body());
            }

            @Override
            public void onFailure(Call <ResponseBody> call, Throwable t) {
                // close it after response
                Log.v("this", "No Response!");
            }
        });

        return token;

    }

}
