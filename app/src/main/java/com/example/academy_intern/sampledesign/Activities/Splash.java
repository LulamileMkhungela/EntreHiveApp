package com.example.academy_intern.sampledesign.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.academy_intern.sampledesign.R;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.IS_USER_ADMIN;

public class Splash extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT =5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ImageView image = findViewById(R.id.iv_logo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        image.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if(IS_USER_ADMIN)
                {
                    Intent intent = new Intent(Splash.this, AdminDashboard.class);
                    startActivity(intent);
                }

                else
                {
                    Intent intent = new Intent(getApplicationContext(), Welcome.class);
                    startActivity(intent);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}