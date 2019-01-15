package com.example.academy_intern.sampledesign.Fragment;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
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

import static com.example.academy_intern.sampledesign.Fragment.SendEmail.RESET_TOKEN;

public class SendResetCode extends Fragment {

    View view;
    Button btnSendResetCode;
    EditText edtResetCode;
    String resetToken = RESET_TOKEN;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.send_reset_code,container,false);

        edtResetCode = view.findViewById(R.id.et_reset_code);
        btnSendResetCode = view.findViewById(R.id.send_reset_code);


        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSendResetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetCode();
            }
        });
    }

    public void sendResetCode()
    {
        Call<UserProfile> call = Api.getClient().sendResetCodeProcess(resetToken, edtResetCode.getText().toString().trim());

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call <UserProfile> call, Response<UserProfile> response) {
                String successMessage = response.body().getName() + ", please reset your password.";
                String failMessage = "Incorrect code. \nPlease try again.";

                if (response.body().getName() != null) {
                    Toast.makeText(getActivity(), successMessage, Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.display, new ResetPassword()).commit();
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