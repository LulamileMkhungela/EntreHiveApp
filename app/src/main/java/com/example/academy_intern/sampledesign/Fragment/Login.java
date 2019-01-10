package com.example.academy_intern.sampledesign.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.Activities.AdminDashboard;
import com.example.academy_intern.sampledesign.Activities.UserDashboard;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.ConfirmPurchaseDialog;
import com.example.academy_intern.sampledesign.Services.MyFCMClass;
import com.example.academy_intern.sampledesign.Services.PointsWarningDialog;
import com.example.academy_intern.sampledesign.Services.SessionManager;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.IS_USER_LOGGED_IN;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.IS_USER_ADMIN;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.USER_BALANCE;
//import static com.example.academy_intern.sampledesign.Activities.MainActivity.storeUserDetailsInHashMap;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.storeUserDetailsInString;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment {
    Button btnLogin;
    TextView tvForgetPassword, tvRegister;
    View view;
    EditText etPassword,etEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_screen, container, false);
        btnLogin = view.findViewById(R.id.btn_login);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        tvRegister = view.findViewById(R.id.tv_question);
        tvForgetPassword = view.findViewById(R.id.tv_forgot_password);
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateEmail()&&validatePass())
                {
                    login();
                }
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.display, new SendEmail()).commit();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.display, new Registration()).commit();
            }
        });

    }

    private boolean validateEmail() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email required");
            etEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            etEmail.setError("Format incorrect");
            etEmail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePass() {
        String pass = etPassword.getText().toString().trim();

        if (pass.isEmpty()) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void login(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        final SessionManager sessionManager = new SessionManager(getActivity());
        MyFCMClass.MyFirebaseMessagingService myFirebaseMessagingService = new MyFCMClass().new MyFirebaseMessagingService();
        String deviceToken = myFirebaseMessagingService.createDeviceToken();

        if(email.isEmpty())
        {
            etEmail.setError("field required");
            etEmail.requestFocus();
        }

        if(password.isEmpty())
        {
            etPassword.setError("field required");
            etPassword.requestFocus();
        }

        if(password.length()<6)
        {
            etPassword.setError("password must be at least 6 characters");
            etPassword.requestFocus();
        }

        Call <UserProfile> call = Api.getClient().login(email, password, deviceToken);

        call.enqueue(new Callback <UserProfile>()
        {
            @Override
            public void onResponse(Call <UserProfile> call, Response <UserProfile> response)
            {
//                String successMessage = response.body().getName() + ", you have successfully logged in!";
                String failMessage = "You have failed to log in! \nPlease try again.";
                //response.body().getName() != null

                if (response.code() == 200)
                {
                    USER_BALANCE = response.body().getPoints();

                    sessionManager.createLoginSession(response.body().getUserId(), response.body().isUserRole(), response.body().getPoints());

                    IS_USER_LOGGED_IN = sessionManager.isLoggedIn();
                    IS_USER_ADMIN = sessionManager.getUserRole();
                    LOGGED_IN_USER_ID = sessionManager.getLoggedInId();
                    storeUserDetailsInString();
//                    storeUserDetailsInHashMap();

                    if(IS_USER_ADMIN)
                    {
                        Intent intent = new Intent(getActivity(), AdminDashboard.class);
                        startActivity(intent);
                    }
                    else
                        {

                        Intent intent = new Intent(getActivity(), UserDashboard.class);
                        startActivity(intent);
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), failMessage, Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.display, new Login()).commit();
                }
            }

            @Override
            public void onFailure(Call <UserProfile> call, Throwable t)
            {
                Log.d("response", t.getStackTrace().toString());
            }
        });
    }
}