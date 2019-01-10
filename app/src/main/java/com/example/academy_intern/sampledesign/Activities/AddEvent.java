package com.example.academy_intern.sampledesign.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.FileUtils.FileUtils;
import com.example.academy_intern.sampledesign.Model.BaseResponse;
import com.example.academy_intern.sampledesign.Model.PlaceAutocompleteAdapter;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.SessionManager;
import com.example.academy_intern.sampledesign.Services.UploadService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.storeUserDetailsInString;

public class AddEvent extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,PlaceSelectionListener, GoogleApiClient.OnConnectionFailedListener
{
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    private Button btn_create,btn_attach_file;
    Button btnBack;
    private MaterialEditText mdateTime,txt_mesaj,reminderTime, edtTitle;
    private MaterialAutoCompleteTextView pleke;
    private static final String TAG = "AddEvent";
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    int day,month,year,hour,minute;
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal,seconds;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final int PICK_IMAGE = 1;
    private Uri uri;
    String fileName;
    private static final String TYPE_1 = "multipart";
    private UploadService uploadService;
    private EditText txt_attach_file;
    public static boolean IS_USER_ADMIN;
    public static int LOGGED_IN_USER_ID;
    public static boolean IS_USER_LOGGED_IN;



    //private PlaceAutocompleteFragment autocompleteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mdateTime = findViewById(R.id.et_date);
        edtTitle = findViewById(R.id.et_title);
        pleke = findViewById(R.id.et_location);
        btn_create = findViewById(R.id.btn_create1);
        reminderTime =  findViewById(R.id.et_time);
        txt_mesaj = findViewById(R.id.et_description);
        txt_attach_file = findViewById(R.id.txt_attach_file);

        SessionManager sessionManager = new SessionManager(this);
        IS_USER_ADMIN = sessionManager.getUserRole();
        IS_USER_LOGGED_IN = sessionManager.isLoggedIn(); // a boolean value is stored in shared preferences
        LOGGED_IN_USER_ID = sessionManager.getLoggedInId(); // a logged in user's idea is stored in shared preferences

        btnBack = findViewById(R.id.btn_toolB);
        btn_attach_file = findViewById(R.id.btn_attach_file);

        btn_attach_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v == btn_attach_file) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getParent(),
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_STORAGE);

                    }
                    choosePhoto();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashIntent = new Intent(AddEvent.this, UserDashboard.class);
                startActivity(dashIntent);
            }
        });

        txt_attach_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //we enforce users to accept permissions for accessing phone storage ,so that they will have full access of the application
               //Permissions are automatically granted when the SDK<21
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        //Permission for WRITE_EXTERNAL STORAGE
                        && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getParent(),
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);

                }

                choosePhoto();
            }
        });

        //Initializing

        mdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,
                        AddEvent.this,year,month,day);
                datePickerDialog.show();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == btn_create) {
                    if(uri != null) {
                        try {
                            File file = FileUtils.getFile(getApplicationContext(), uri);
                            uploadMultipart(file);
                            createEvent();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Error "+e, Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(getApplicationContext(), "Your request was successfully submitted.", Toast.LENGTH_SHORT).show();

                        if(IS_USER_ADMIN)
                        {
                            Intent adminIntent = new Intent(getApplicationContext(), AdminDashboard.class);
                            startActivity(adminIntent);
                        }

                        else if (IS_USER_LOGGED_IN && LOGGED_IN_USER_ID != 0)
                        {
                            storeUserDetailsInString();
                            Intent userIntent = new Intent(getApplicationContext(), UserDashboard.class);
                            startActivity(userIntent);
                        }

                        else
                        {
                            Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(homeIntent);
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "You must choose the file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        init();
    }
    private void init()
    {
        Log.d(TAG, "init: initializing");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_GEOCODE)
                .setCountry("ZA")
                .build();
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, typeFilter);
        pleke.setAdapter(mPlaceAutocompleteAdapter);
    }
    private void setDate(final Calendar calendar)
    {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((MaterialEditText) findViewById(R.id.et_date)).setText(dateFormat.format(calendar.getTime()));
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = Integer.valueOf(String.format(Locale.getDefault(),"%02d", month + 1));
        dayFinal = Integer.valueOf(String.format(Locale.getDefault(),"%02d", dayOfMonth));
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        seconds =c.get(Calendar.SECOND);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this,AddEvent.this,
                hour,minute, android.text.format.DateFormat.is24HourFormat(this));
        timePickerDialog.show();
        String fullDate = yearFinal + "-" + String.format(Locale.getDefault(), "%02d", monthFinal) + "-" + String.format(Locale.getDefault(), "%02d", dayFinal);
        mdateTime.setText(fullDate);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        String fullTime = String.format(Locale.getDefault(), "%02d", hourFinal) + ":" + String.format(Locale.getDefault(), "%02d", minuteFinal);
        reminderTime.setText(fullTime);
    }
    public void createEvent()
    {
        //initializing my values so that i
        String title = edtTitle.getText().toString().trim();
        String description = txt_mesaj.getText().toString().trim();
        String date = mdateTime.getText().toString().trim();
        String time =reminderTime.getText().toString().trim();
        String DateTime =date+" "+time;
        String venue = pleke.getText().toString().trim();
        //validating if the fields contain input
        if(date.isEmpty())
        {
            mdateTime.setError("invalid date");
            mdateTime.requestFocus();
        }
        if(description.isEmpty())
        {
            txt_mesaj.setError("field required");
            txt_mesaj.requestFocus();
        }
        //implementing a post request from my api interface that helps me push my data to the server then server
        Call<Void> call = Api.getClient().addEvent(title, description, venue, MainActivity.LOGGED_IN_USER_ID, DateTime);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Failure:", "onFailure: failed to add event");
            }
        });
    }
    @Override
    public void onPlaceSelected(Place place) {
    }
    @Override
    public void onError(Status status) {
    }
//upload a files


    private void choosePhoto()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        } else {
            openGallery();
        }
    }

    public void openGallery()
    {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null)
            {
                uri = data.getData();
                String path = uri.toString();
                txt_attach_file.setText(path);
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

    private void uploadMultipart(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        fileName = file.getName();
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), TYPE_1);
        uploadService = new UploadService();
        uploadService.uploadFileEventMultipart(LOGGED_IN_USER_ID, action, fileToUpload, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();
                if(baseResponse != null) {
                    Toast.makeText(getApplicationContext(),"You have successfully uploaded", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}