package com.example.academy_intern.sampledesign.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendEmail extends Fragment {

    View view;
    Button btnSendEmail;
    EditText edtSendEmail;
    public static String RESET_TOKEN;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.send_email,container,false);

        btnSendEmail = view.findViewById(R.id.send_email);
        edtSendEmail = view.findViewById(R.id.et_email);

        return view;

    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = edtSendEmail.getText().toString().trim();
                if (email.isEmpty())
                {
                    edtSendEmail.setError("Please fill in your email address.");
                }
                sendEmail();
            }
        });

    }

    public void sendEmail()
    {
        Call<UserProfile> call = Api.getClient().sendEmailProcess(edtSendEmail.getText().toString().trim());

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call <UserProfile> call, Response<UserProfile> response) {
                String successMessage = response.body().getName() + ", please check your email.";
                String failMessage = "It appears that you do not have an account. \nPlease register for one.";

                if (response.body().getName() != null) {
                    RESET_TOKEN = response.body().getResetToken();
                    Toast.makeText(getActivity(), successMessage, Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.display, new SendResetCode()).commit();
                }
                else
                {
                    Toast.makeText(getActivity(), failMessage, Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.display, new Registration()).commit();
                }
            }

            @Override
            public void onFailure(Call <UserProfile> call, Throwable t) {
                Log.d("response", t.getStackTrace().toString());
            }
        });
    }

}
