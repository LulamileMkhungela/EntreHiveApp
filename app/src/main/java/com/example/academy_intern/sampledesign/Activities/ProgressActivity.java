package com.example.academy_intern.sampledesign.Activities;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.R;

public class ProgressActivity extends AppCompatActivity {

    static int progressStatus = 0;
    static Handler handler = new Handler();
    TextView tv;
    static ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        showProgress();
    }

    public void setupProgress()
    {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        mProgress = findViewById(R.id.circularProgressbar);
        tv = findViewById(R.id.tv);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);
    }

    public void showProgress()
    {
        setupProgress();

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (progressStatus < 100)
                {
                    progressStatus += 1;

                    handler.post(new Runnable()
                    {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(progressStatus);
                            String percentStatus = progressStatus + "%";
                            tv.setText(percentStatus);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(30); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
