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

import static com.example.academy_intern.sampledesign.Fragment.SendEmail.RESET_TOKEN;

public class ResetPassword extends Fragment {

    View view;
    Button btnResetPassword;
    EditText edtResetPassword, edtConfirmResetPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reset_password,container,false);

        edtResetPassword = view.findViewById(R.id.et_password);
        edtConfirmResetPassword = view.findViewById(R.id.et_confirm_password);
        btnResetPassword = view.findViewById(R.id.reset_password);

        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    public void resetPassword()
    {
        Call<UserProfile> call = Api.getClient().resetPasswordProcess(RESET_TOKEN, edtResetPassword.getText().toString().trim());

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call <UserProfile> call, Response<UserProfile> response) {
                String successMessage = response.body().getName() + ", your password has been reset successfully.";
                String failMessage = "Password reset was unsuccessful. \nPlease try again.";

                if (response.body().getName() != null) {
                    Toast.makeText(getActivity(), successMessage, Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.display, new Login()).commit();
                }
                else
                {
                    Toast.makeText(getActivity(), failMessage, Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.display, new SendEmail()).commit();
                }
            }

            @Override
            public void onFailure(Call <UserProfile> call, Throwable t) {
                Log.d("response", t.getStackTrace().toString());
            }
        });
    }


}
