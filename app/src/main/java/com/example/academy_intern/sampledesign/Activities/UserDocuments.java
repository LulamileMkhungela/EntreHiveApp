package com.example.academy_intern.sampledesign.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.R;

import java.util.ArrayList;

import android.Manifest;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;

public class UserDocuments extends AppCompatActivity
{
    ListView simpleList;
    Button btnBack;
    public String downloadLink;
    ArrayList<String> listOfDocuments = new ArrayList <>();
    ArrayList<String> listOfDownloadLinks = new ArrayList <>();
    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;

    ArrayList<Long> list = new ArrayList<>();

    @Override   protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_documents);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        simpleList = (findViewById(R.id.simpleListView));
        btnBack = findViewById(R.id.btn_back);
        getDocumentNames();
        getDownloadLinks();

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDocuments.super.onBackPressed();
            }
        });


        /*The names of the documents are listed in a list view. If the admin desires to view the contents
        * of a particular document, he/she merely clicks on the document name. The download link for the
        * chosen document is stored in the array list, listOfDownloadLinks. The download link for the
        * chosen document has the array list index that is equal to the position of the chosen document
        * in the list view. */
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int positionOfClickedItem, long arg3)
            {

                downloadLink = listOfDownloadLinks.get(positionOfClickedItem);

                view();

                list.clear();

                Download_Uri = Uri.parse(downloadLink);

                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle("GadgetSaint Downloading " + "Sample" + ".docx");
                request.setDescription("Downloading " + "Sample" + ".pdf");
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/GadgetSaint/" + "/" + "Sample" + ".pdf");


                refid = downloadManager.enqueue(request);


                Log.e("OUT", "" + refid);

                list.add(refid);
            }
        });

    }

    /*This method gets the names of the documents that the user has uploaded to the server. The document
    * names are strings that are stored in an array list. The names of the documents are displayed in a
    * list view.*/
    private void getDocumentNames()
    {

        Intent intent = getIntent();

        int userId = intent.getIntExtra("User Id",0 );

        Call<ArrayList<String>> call = Api.getClient().getDocumentNames(userId);

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<String>>()
        {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response)
            {
                listOfDocuments = response.body();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.document_list_view, R.id.textView, listOfDocuments);
                simpleList.setAdapter(arrayAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* This method gets the download links of the documents that the user has uploaded to the server. The download
     * links are strings that are stored in an array list. */
    private void getDownloadLinks()
    {
        Intent intent = getIntent();

        int userId = intent.getIntExtra("User Id",0 );

        Call<ArrayList<String>> call = Api.getClient().getDownloadLinks(userId);

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ArrayList<String>>()
        {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response)
            {
                listOfDownloadLinks = response.body();
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
            else
            {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else
        {     //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {


            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


            Log.e("IN", "" + referenceId);

            list.remove(referenceId);


            if (list.isEmpty()) {


                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("GadgetSaint")
                                .setContentText("All Download completed");


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());


            }

        }
    };


    @Override
    protected void onDestroy() {


        super.onDestroy();

        unregisterReceiver(onComplete);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            // permission granted

        }
    }

    /*This method takes the user to the activity where the actual document can be viewed. The download
    * link is sent to the DocumentViewer, where the document can be viewed.*/
    public void view()
    {
        Intent i = new Intent(getApplicationContext(), DocumentViewer.class);
        i.putExtra("Download Link", downloadLink);
        startActivity(i);
    }

}
