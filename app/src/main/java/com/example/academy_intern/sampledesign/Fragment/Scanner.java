package com.example.academy_intern.sampledesign.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.Activities.UserDashboard;
import com.example.academy_intern.sampledesign.ApiConnection.Api;
import com.example.academy_intern.sampledesign.Model.EventProfile;
import com.example.academy_intern.sampledesign.Model.ItemProfile;
import com.example.academy_intern.sampledesign.Model.UserProfile;
import com.example.academy_intern.sampledesign.R;
import com.example.academy_intern.sampledesign.Services.ConfirmPurchaseDialog;

import com.example.academy_intern.sampledesign.Services.SessionManager;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.academy_intern.sampledesign.Activities.MainActivity.LOGGED_IN_USER_ID;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.USER_BALANCE;
import static com.example.academy_intern.sampledesign.Activities.MainActivity.storeUserDetailsInString;
//import static com.example.academy_intern.sampledesign.Services.ConfirmPurchaseDialog.displayPurchaseMessage;

public class Scanner extends Fragment implements ZXingScannerView.ResultHandler
{

    View view;

    private ZXingScannerView mScannerView;
    public static int ITEM_PRICE;
    public static String ITEM_NAME;
//    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static final int OPEN_CAMERA = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    public static int EVENT_ID;
    ItemProfile item = new ItemProfile();
    EventProfile event = new EventProfile();
    ConfirmPurchaseDialog confirmPurchase;

    public static Scanner newInstance()
    {
        Scanner fragment = new Scanner();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.activity_scanner_view, null);
        mScannerView = new ZXingScannerView(getActivity().getApplicationContext());   // Initialise the scanner view
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        openCamera();
        mScannerView.startCamera();


        return mScannerView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }

    /* The handleResult method is called after a QR code is scanned. The rawResult is the text or image
     * that is encoded within the QR code. There are two types of QR codes: one for purchasing goods,
     * and one for accessing a venue. For purchasing goods, a semicolon (;) is used as a delimiter to
     * split the string containing the name of the item and the price. For accessing a venue, a
     * comma (,) is used as a delimiter to split the string containing the event Id and the amount
     * of points to be rewarded for attending the event.*/
    @Override
    public void handleResult(Result rawResult)
    {
        if (rawResult.getText().contains(";"))
        {
            String itemNameAndPrice = rawResult.getText(); //name and price are separated by a semicolon (;)

            String[] itemAndPriceArray = itemNameAndPrice.split(";");

            item.setItemName(itemAndPriceArray[0]);
            item.setItemPrice(Integer.valueOf(itemAndPriceArray[1]));

//            displayPurchaseMessage(getActivity());

            String purchaseMessage = "Confirm the purchase of item " + itemAndPriceArray[0] + " for " + itemAndPriceArray[1] + " points?";

            confirmPurchaseDialog(getActivity(), "Confirm Purchase", purchaseMessage);

            mScannerView.stopCamera();

//            makePurchase();
        }
        else if (rawResult.getText().contains(","))
        {
            String eventIdAndPoints = rawResult.getText(); //event id and attendance points are separated by a comma (,)
            String[] eventIdAndPointsArray = eventIdAndPoints.split(",");
            event.setEventId(Integer.valueOf(eventIdAndPointsArray[0]));
            event.setAttendancePoints(Integer.valueOf(eventIdAndPointsArray[1]));
            welcomeMessageDialog(getActivity(), "Confirm Attendance", "Would you like to attend this event?");
        }
        else
        {
            goBackToDashboard();
        }
    }

    /* The makePurchase method gets the points of a user from the database. If the amount of points is
     * enough to make a purchase, the user's balance is updated. If the user has insufficient funds,
     * the transaction does not take place.*/
    public void makePurchase()
    {
        final SessionManager sessionManager = new SessionManager(getActivity());
        final String itemName = item.getItemName();
        final int itemPrice = item.getItemPrice();
        //final String itemPriceDisplay = "-"+itemPrice;
        Callback<UserProfile> responseCallback = new Callback<UserProfile>()
        {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                int balance = response.body().getPoints();
                String successMessage = "You have purchased a " + itemName + " for " + itemPrice + " points."
                        + "\nYour current balance is " + balance + " points.";

                String failMessage = "You have insufficient funds."
                        + "\nYour current balance is " + balance + " points.";

                if (USER_BALANCE >= itemPrice)
                {
                    USER_BALANCE = USER_BALANCE - itemPrice;
                    response.body().setPoints(USER_BALANCE);
                    response.body().setUsedPoints(response.body().getUsedPoints() + itemPrice);
                    sessionManager.setUserBalance(USER_BALANCE);
                    storeUserDetailsInString();
                    messageDialog(getActivity(), "Purchase Successful", successMessage);
                }
                else
                {
                    insufficientFundsDialog(getActivity(), "Insufficient Funds", failMessage);

                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.d("response", t.getStackTrace().toString());
            }
        };

        Api.getClient().makePurchase(LOGGED_IN_USER_ID, itemName, itemPrice).enqueue(responseCallback);
        mScannerView.stopCamera();
//        goBackToDashboard();
    }

    /* The attendEvent() method checks the database to determine if a user is permitted to attend
     * a given event. If the user is permitted to attend, the user receives points for the
     * attendance. */
    private void attendEvent()
    {
        final int eventId = event.getEventId();
        final int attendancePoints = event.getAttendancePoints();

        Callback<EventProfile> responseCallback = new Callback<EventProfile>()
        {
            @Override
            public void onResponse(Call<EventProfile> call, Response<EventProfile> response)
            {
                String successMessage = "Welcome to the event.";
                String failMessage = "You do not have permission to attend this event.";
                String alreadyAccessedMessage = "You've already accessed the venue. \nYou will not receive additional points.";

                if (response.body().getUserId() == 0)
                {
                    messageDialog(getActivity(), "Not Invited", failMessage);

                }
                else if (response.body().getUserId() == -1)
                {
                    messageDialog(getActivity(), "Already Accessed", alreadyAccessedMessage);
                }
                else
                {
                    USER_BALANCE = USER_BALANCE + attendancePoints;
                    storeUserDetailsInString();
                    messageDialog(getActivity(), "Welcome", successMessage);
                }
            }

            @Override
            public void onFailure(Call<EventProfile> call, Throwable t)
            {
                Log.d("response", t.getStackTrace().toString());

            }
        };
        Api.getClient().allowUserAccess(LOGGED_IN_USER_ID, eventId).enqueue(responseCallback);
        mScannerView.stopCamera();
//        goBackToDashboard();
    }

    private void goBackToDashboard()
    {
        Intent backIntent = new Intent(getActivity(), UserDashboard.class);
        startActivity(backIntent);
    }
    
    public void displayPurchaseMessage(Activity activity)
    {
        ConfirmPurchaseDialog purchase = new ConfirmPurchaseDialog(getActivity());
        purchase.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        purchase.show();
    }

    private void confirmPurchaseDialog(Activity activity, String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        //alertDialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_close);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        makePurchase();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        goBackToDashboard();
                    }
                });
        alertDialog.show();
    }

    private void welcomeMessageDialog(Activity activity, String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        attendEvent();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        goBackToDashboard();
                    }
                });
        alertDialog.show();
    }

    private void messageDialog(Activity activity, String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        goBackToDashboard();
                    }
                });
//        alertDialog.setIcon(R.drawable.success);
        alertDialog.show();
    }

    private void insufficientFundsDialog(Activity activity, String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        goBackToDashboard();
                    }
                });

        alertDialog.setIcon(R.drawable.funds);
        alertDialog.show();


    }


}

