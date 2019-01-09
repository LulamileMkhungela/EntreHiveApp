package com.example.academy_intern.sampledesign.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.FileUtils.FileUtils;
import com.example.academy_intern.sampledesign.Model.BaseResponse;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.UploadService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_DETAILS;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.NEW_PHOTO_ADDED;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;
//import static com.example.academy_intern.sampledesign.Activities.MainActivity.storeUserDetailsInHashMap;


public class EditProfile extends AppCompatActivity {

    Button btnBack, btnSaveChanges, btnCam, btnNext;
    EditText editMail,editNumber,editId,editName,editCompName,editCompNumber;
    TextView textCurrentPoints, textTotalPoints, textUsedPoints;
    int userId;
    String username, phoneNo, name, compName, compNumber, photoPath, idNo, currentPoints, totalPoints, usedPoints;
    public static String LOCAL_PHOTO_PATH;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final int PICK_IMAGE = 1;
    private Uri uri;
    ImageView imageView;
    private static final String TYPE_1 = "multipart";
    private UploadService uploadService;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        initialiseWidgets();

        getUserDetails(LOGGED_IN_USER_DETAILS);

//        getUserDetails();

        setUserDetails();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profileIntent = new Intent(EditProfile.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(uri != null)
                {
                    File file = FileUtils.getFile(getApplicationContext(), uri);
                    uploadMultipart(file);
                    imageView.invalidate();
                }
                updateProfile();
                Intent profileIntent = new Intent(EditProfile.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v == btnNext)
                {
                    if(uri != null)
                    {
                        File file = FileUtils.getFile(getApplicationContext(), uri);
                        uploadMultipart(file);
                        imageView.invalidate();
                    }
                }
                updateProfile();

                Intent i=new Intent(getApplicationContext(), BusinessCertificate.class);
                startActivity(i);
            }

        });
        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choosePhoto();
                if (v == btnCam) {
                    choosePhoto();
                }
            }
        });

    }



    //Uploading a photo
    private void uploadMultipart(File photo) {
        //Map is used to multipart the file using okhttp3.RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), photo);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photo", photo.getName(), requestBody);
        //Parsing any Media type file
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), TYPE_1);



       //Creating an instance of UploadService
        uploadService = new UploadService();
        uploadService.uploadPhotoMultipart(LOGGED_IN_USER_ID, action, fileToUpload, new Callback()
        {
            @Override
            public void onResponse(Call call, Response response)
            {
                BaseResponse baseResponse = (BaseResponse) response.body();

                if(baseResponse != null)
                {
                    Toast.makeText(getApplicationContext(), baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }

//                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //When an image is picked
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null)
            {
                uri = data.getData();
                LOCAL_PHOTO_PATH = uri.toString();
                //Set the image thumb in Imageview Previewing the Media
                imageView.setImageURI(uri);
                NEW_PHOTO_ADDED = true;
            }
        }
    }



    public void updateProfile()
    {
        initialiseVariables();

        Call<UserProfile> call = Api.getClient().updateUser(userId,
                idNo , name, username, phoneNo, compName, compNumber);

        UserProfile user = new UserProfile();
        user.setName(name);
        user.setEmail(username);
        user.setIdentityNumber(idNo);
        user.setPhoneNumber(phoneNo);
        user.setCompanyName(compName);
        user.setCompanyNumber(compNumber);
        user.setPoints(Integer.valueOf(currentPoints));
        user.setTotalPoints(Integer.valueOf(totalPoints));
        user.setUsedPoints(Integer.valueOf(usedPoints));
        user.setPhotoPath(photoPath);

        Gson gson = new Gson();
        LOGGED_IN_USER_DETAILS = gson.toJson(user);

        call.enqueue(new Callback<UserProfile>()
        {
            @Override
            public void onResponse(Call <UserProfile> call, Response<UserProfile> response)
            {
//                Toast.makeText(getActivity(), successMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call <UserProfile> call, Throwable t)
            {
                Log.d("response", t.getStackTrace().toString());
            }
        });
    }

    //Choosing a photo
     private void choosePhoto()
    {
        //Permission is automatically granted when the SDK < 21
        //Permission for READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                //Permission for WRITE_EXTERNAL_STORAGE
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        } else {
            openGallery();
        }
    }

  //Image must be low in Memory or need to be compressed before uploading
    public void openGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void initialiseWidgets()
    {
        btnBack = findViewById(R.id.toolB);
        btnSaveChanges = findViewById(R.id.btn_save_changes);
        editMail = findViewById(R.id.edit_mail);
        editNumber = findViewById(R.id.ed_number);
        editId = findViewById(R.id.ed_idno);
        editName = findViewById(R.id.ed_name);
        editCompName = findViewById(R.id.ed_comp_name);
        editCompNumber = findViewById(R.id.ed_company_number);
        textCurrentPoints = findViewById(R.id.current_balance);
        textTotalPoints = findViewById(R.id.total_points);
        textUsedPoints = findViewById(R.id.used_points);
        btnCam = findViewById(R.id.btnCamera);
        imageView = findViewById(R.id.profile_image);
        btnNext = findViewById(R.id.btn_next);
    }

    private void initialiseVariables()
    {
        userId = LOGGED_IN_USER_ID;
        username = editMail.getText().toString();
        phoneNo = editNumber.getText().toString();
        idNo = editId.getText().toString();
        name = editName.getText().toString();
        compName = editCompName.getText().toString();
        compNumber = editCompNumber.getText().toString();
        currentPoints = textCurrentPoints.getText().toString();
        totalPoints = textTotalPoints.getText().toString();
        usedPoints = textUsedPoints.getText().toString();
    }

    public void getUserDetails(String userDetails)
    {
        Gson gson = new Gson();
        UserProfile user = gson.fromJson(userDetails, UserProfile.class);
        username = user.getEmail();
        phoneNo = user.getPhoneNumber();
        idNo = user.getIdentityNumber();
        name = user.getName();
        compName = user.getCompanyName();
        compNumber = user.getCompanyNumber();
        currentPoints = String.valueOf(user.getPoints());
        totalPoints = String.valueOf(user.getTotalPoints());
        usedPoints = String.valueOf(user.getUsedPoints());
        photoPath = user.getPhotoPath();
    }

//    public void getUserDetails()
//    {
//        HashMap<String, String> userInformation = storeUserDetailsInHashMap();
//
//        username = userInformation.get("Email");
//        phoneNo = userInformation.get("Phone Number");
//        idNo = userInformation.get("Identity Number");
//        name = userInformation.get("Name");
//        compName = userInformation.get("Company Name");
//        compNumber = userInformation.get("Company Number");
//        currentPoints = userInformation.get("Current Points");
//        totalPoints = userInformation.get("Total Points");
//        usedPoints = userInformation.get("Used Points");
//        photoPath = userInformation.get("Photo Path");
//    }

    public void setUserDetails()
    {
        editMail.setText(username);
        editId.setText(idNo);
        editName.setText(name);
        editNumber.setText(phoneNo);
        editCompName.setText(compName);
        editCompNumber.setText(compNumber);
        textCurrentPoints.setText(currentPoints);
        textTotalPoints.setText(totalPoints);
        textUsedPoints.setText(usedPoints);
        if (!NEW_PHOTO_ADDED)
        {
            Picasso.get().load(photoPath).resize(50,50).centerCrop().into(imageView);
        }
        else
        {
            Picasso.get().load(LOCAL_PHOTO_PATH).resize(50,50).centerCrop().into(imageView);
        }
    }
}