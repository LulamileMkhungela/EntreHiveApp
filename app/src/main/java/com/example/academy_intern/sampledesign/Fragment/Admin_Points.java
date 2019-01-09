package com.example.academy_intern.sampledesign.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    Button btnSave;
    EditText txtMoneyInPoints;
    TextView  txtvalue;
    int points;


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

        btnSave = view.findViewById(R.id.btnSave);
        txtMoneyInPoints = view.findViewById(R.id.txtPointInRands);
        txtvalue = view.findViewById(R.id.txtBalance);

        SaveData(0);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                points = Integer.parseInt(txtMoneyInPoints.getText().toString());
                SaveData(points);
            }
        });

        return view;
    }

    public void SaveData(final int point){

        Call<String> call = Api.getClient().updateWallet(point);

        call.enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response)
            {

                txtvalue.setText(response.body());
                if(point > 0) {
                    Toast.makeText(getActivity(), "Points added successfully ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}