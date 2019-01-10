package com.example.academy_intern.sampledesign.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.FileUtils.FileUtils;
import com.example.academy_intern.sampledesign.Model.BaseResponse;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.UploadService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;

public class BusinessCertificate extends AppCompatActivity {
    TextView textView_document, tv_attach_file;
    Button uploadFile, btnBack;
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final String TYPE_1 = "multipart";
    private static final String TYPE_2 = "base64";
    String fileName;
    ProgressDialog uploadProgress;


    private UploadService uploadService;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_certificate);
        textView_document = findViewById(R.id.tv_ck_document);
        uploadFile = findViewById(R.id.btn_upload);
        btnBack = findViewById(R.id.toolBack);
        tv_attach_file = findViewById(R.id.tv_attach_file);
        uploadProgress = new ProgressDialog(this);
        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent certificateIntent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(certificateIntent);
            }
        });

        tv_attach_file.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(v == tv_attach_file)
                {
                    choosePhoto();
                }
            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(uri != null)
                {
                    File file = FileUtils.getFile(getApplicationContext(), uri);
                    //fileName = file.getName();
                    uploadProgress.setMessage("Uploading...");
                    uploadProgress.show();
                    uploadMultipart(file);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You must choose the file", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        }else{
            openGallery();
        }
    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }
    private void uploadMultipart(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        fileName = file.getName();
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), TYPE_1);
        uploadService = new UploadService();
        uploadService.uploadFileMultipart(LOGGED_IN_USER_ID, action, fileToUpload, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();
                if(baseResponse != null) {
                    uploadProgress.dismiss();
                    Toast.makeText(getApplicationContext(),"You have successfully uploaded", Toast.LENGTH_SHORT).show();
                }
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
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null)
            {
                uri = data.getData();
                String path = uri.toString();
                textView_document.setText(path);
                uri = Uri.parse(textView_document.getText().toString());
                uri = data.getData();
//
//                imgThumb.setImageURI(uri);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
                return;
            }
        }
    }

}
