package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.academy_intern.sampledesign.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DocumentViewer extends AppCompatActivity
{
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_viewer);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        pdfView=(PDFView) findViewById(R.id.pdfView);
        // http://10.1.0.88:8080/downloadFile/1,git_tutorial.pdf
        //This is a function read from the URL;

        Intent intent = getIntent();
        String downloadLink = intent.getExtras().getString("Download Link");

        new RetrievePDFStream().execute(downloadLink);
    }

    class RetrievePDFStream extends AsyncTask<String,Void,InputStream>
    {
        //This is a method that retrieves a pdf document using the inputStream object
        @Override
        protected InputStream doInBackground(String... strings)
        {
            InputStream inputStream=null;
            try {
                //Receives data from Uri
                URL uri= new URL(strings[0]);
                // Opening a connection
                HttpURLConnection urlConnection=(HttpURLConnection) uri.openConnection();
                if (urlConnection.getResponseCode()==200)
                {
                    inputStream=new BufferedInputStream(urlConnection.getInputStream());
                }

            }
            catch (IOException e)
            {
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream){
            pdfView.fromStream(inputStream).load();
        }
    }

}
