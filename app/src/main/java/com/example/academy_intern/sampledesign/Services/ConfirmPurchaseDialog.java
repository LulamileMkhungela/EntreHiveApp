package com.example.academy_intern.sampledesign.Services;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.academy_intern.sampledesign.Fragment.Scanner;
import com.example.academy_intern.sampledesign.R;

import static com.example.academy_intern.sampledesign.Fragment.Scanner.ITEM_NAME;
import static com.example.academy_intern.sampledesign.Fragment.Scanner.ITEM_PRICE;

public class ConfirmPurchaseDialog extends Dialog implements
        android.view.View.OnClickListener
{

    private Activity c;
    public Dialog d;
    private Button btnConfirm, btnCancel;
    TextView tvPurchaseMessage;
    Scanner scan;

    public ConfirmPurchaseDialog(Activity a)
    {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confirm_purchase);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        tvPurchaseMessage = findViewById(R.id.tv_purchase);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        String message = "Confirm the purchase of item " + ITEM_NAME + " for " + ITEM_PRICE + " points?";
        tvPurchaseMessage.setText(message);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_confirm:
//                scan.makePurchase();
                c.finish();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

}