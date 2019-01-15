package com.example.academy_intern.sampledesign.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.academy_intern.sampledesign.Adapter.ExpandableListAdapter;
import com.example.academy_intern.sampledesign.R;

public class ExpandableList extends AppCompatActivity {
    Button btnBack;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    /*Method that retrieves the ID of the expandable view in order to display the content which is added
    in the list item
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_list_layout);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        listView = (ExpandableListView)findViewById(R.id.ev_expand);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);
        btnBack = findViewById(R.id.toolB);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent dashIntent = new Intent(getApplicationContext(), UserDashboard.class);
                startActivity(dashIntent);
            }
        });


    }
//Method with hard coded content that will be displayed during program execution
    private void initData(){
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("How to earn points");
        listDataHeader.add("What to do with the points");
        listDataHeader.add("About Events");
        listDataHeader.add("Transactions");

        List<String> question1 = new ArrayList<>();
        question1.add("Register for the first time and upload necessary documents and get your account topped up with points." +
                " Attend events and earn points, when you subscribe to an event you book a seat with points and later when you" +
                " attend the event you get your points back. The more events attended the more you earn points. ");
        List<String> question2 = new ArrayList<>();
        question2.add("Use some of your points to reserve a seat for yourself. Use your points to scan QR codes at the event to" +
                " buy yourself some nice items that will be sold at a particular event you will be attending. ");
        List<String> question3 = new ArrayList<>();
        question3.add("As a user you can attend as many events as you like, as long as you have enough points to reserve a seat " +
                "for that event. You can also create an event that will need to be approved by the administrator. When creating " +
                "an, you need to generate a QR code for that event and the administrator will place the number of points that will" +
                " come with the QR code. You can add items of your choice that you wish to sell at the event, QR codes for the items" +
                " will also be generated. Once the QR codes are generated you can the print them out for users attending the event" +
                " to scan when purchasing the items, alternatively you may let users scan from your phone.\n");
        List<String> question4 = new ArrayList<>();
        question4.add("There are QR codes for earning points which are points earned through attending events, the other QR codes" +
                " are for items that are being sold. If your balance is at 25 points you will be notified so that you can use your" +
                " points with caution in order to be able to use some to reserve a seat for an awesome upcoming event that you would" +
                " like to attend. If your points balance goes as low as 15 points it means you are using the space more than you are" +
                " earning points and will be denied access to go any. Further within the space.");

        listHash.put(listDataHeader.get(0),question1);
        listHash.put(listDataHeader.get(1),question2);
        listHash.put(listDataHeader.get(2),question3);
        listHash.put(listDataHeader.get(3),question4);


    }
}
