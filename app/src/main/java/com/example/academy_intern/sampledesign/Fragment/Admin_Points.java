package com.example.academy_intern.sampledesign.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_Points extends Fragment {

    View view;
    Button btnPay;
    EditText txtMoneyInPoints, txtEmail;
    TextView  tvHeading, tvEmail, tvBalance;
    int points;
    String email;


    public static Admin_Points newInstance() {
        Admin_Points fragment = new Admin_Points();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.admin_points, container, false);
        initialiseWidgets();

//        SaveData(0);
        getAdminBalance();


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = txtEmail.getText().toString();
                points = Integer.parseInt(txtMoneyInPoints.getText().toString());
                payUser(email, points);
            }
        });

        return view;
    }

//    public void SaveData(final int point){
//
//        Call<String> call = Api.getClient().updateWallet(point);
//
//        call.enqueue(new Callback<String>()
//        {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response)
//            {
//
//                txtvalue.setText(response.body());
//                if(point > 0) {
//                    Toast.makeText(getActivity(), "Points added successfully ", Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t)
//            {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    private void initialiseWidgets()
    {
        btnPay = view.findViewById(R.id.btnPay);
        txtMoneyInPoints = view.findViewById(R.id.txtPointInRands);
        tvHeading = view.findViewById(R.id.txtHeading);
        tvEmail = view.findViewById(R.id.tv_email);
        txtEmail = view.findViewById(R.id.txtEmail);
        tvBalance = view.findViewById(R.id.tv_balance);
    }

    public void payUser(String email, final int points)
    {

        Call<String> call = Api.getClient().allocatePoints(email, points);

        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {

                String message = response.body();
                if(points > 0)
                {
                    messageDialog(getActivity(), message);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAdminBalance()
    {
        Call<String> call = Api.getClient().getBalance();

        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {
                String message = "Amount in Wallet: " + response.body();
                tvBalance.setText(message);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void messageDialog(Activity activity, String message)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Points");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        alertDialog.dismiss();
                        txtEmail.setText("");
                        txtMoneyInPoints.setText("");
                    }
                });

        alertDialog.setIcon(R.drawable.success);
        alertDialog.show();
    }

}