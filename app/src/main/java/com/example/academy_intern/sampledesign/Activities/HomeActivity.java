package com.example.academy_intern.sampledesign.Activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.academy_intern.sampledesign.Fragment.HomeFragment;
import com.example.academy_intern.sampledesign.R;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {

    private final String TOPIC = "JavaSampleApproach";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        getFragmentManager().beginTransaction().add(R.id.display, new HomeFragment()).commit();
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

    }
}
