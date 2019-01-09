package com.example.academy_intern.sampledesign.Services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.R;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.USER_BALANCE;

public class PointsWarningDialog extends Dialog implements android.view.View.OnClickListener
{

    private Activity c;
    public Dialog d;
    private Button btnOk;
    TextView tvWarningMessage;

    public PointsWarningDialog(Activity a)
    {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_points_warning_dialog);
        btnOk = findViewById(R.id.btn_ok);
        tvWarningMessage = findViewById(R.id.tv_warning);
        btnOk.setOnClickListener(this);
        String message = "You currently have " + USER_BALANCE + " points. Please attend an event as soon as possible.";
        tvWarningMessage.setText(message);
    }

    @Override
    public void onClick(View v)
    {
        dismiss();
    }

    public static void displayWarning(Activity activity)
    {
        PointsWarningDialog warning = new PointsWarningDialog(activity);
        warning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        warning.show();
    }

}
