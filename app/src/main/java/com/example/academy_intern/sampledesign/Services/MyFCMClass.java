package com.example.academy_intern.sampledesign.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.academy_intern.sampledesign.Activities.MainActivity;
import com.example.academy_intern.sampledesign.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class MyFCMClass extends FirebaseMessagingService
{

    public class MyFirebaseMessagingService extends FirebaseMessagingService
    {

        private static final String TAG = "MyFirebaseMsgService";

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage)
        {

            String event_id = "0";
            String event_date_time = "0";
            String event_description = "0";
            String event_location = "0";
            String title = "0";


            String M_view = "0";

            if (remoteMessage.getData().size() > 0) {

                event_id = remoteMessage.getData().get("event_id");
                event_date_time = remoteMessage.getData().get("event_date_time");
                event_description = remoteMessage.getData().get("event_description");
                event_location = remoteMessage.getData().get("event_location");
                title = remoteMessage.getData().get("title");

                M_view = remoteMessage.getData().get("M_view");

            }

            String click_action = remoteMessage.getNotification().getClickAction();

            //Calling method to generate notification
            sendNotification(event_id,event_date_time,event_location,event_description, title, M_view, click_action);
        }

        private void sendNotification(String event_id, String event_date_time, String event_location,String event_description,String title ,String M_view, String click_action)
        {
            Intent intent = new Intent(click_action);

            intent.putExtra("event_id", event_id);
            intent.putExtra("event_date_time", event_date_time);
            intent.putExtra("event_location",event_location);
            intent.putExtra("title",title);
            intent.putExtra("event_description",event_description);

            intent.putExtra("M_view", M_view);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
//                    .setContentTitle(messageTitle)
//                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
//
//        /**
//         * Called if InstanceID token is updated. This may occur if the security of
//         * the previous token had been compromised. Note that this is called when the InstanceID token
//         * is initially generated so this is where you would retrieve the token.
//         */
//        @Override
//        public void onNewToken(String token)
//        {
//            Log.d(TAG, "Refreshed token: " + token);
//
//            // If you want to send messages to this application instance or
//            // manage this apps subscriptions on the server side, send the
//            // Instance ID token to your app server.
//            sendRegistrationToServer(token);
//        }
//        // [END on_new_token]
//
//
//        @Override
//        public void onTokenRefresh()
//        {
//            super.onTokenRefresh();
//
//        }

        public String createDeviceToken()
        {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.i(TAG, "Refreshed token: " + token);
            return token;
        }

//        public void token()
//        {
//            // Get token
//            FirebaseInstanceId.getInstance().getInstanceId()
//                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                            if (!task.isSuccessful()) {
//                                Log.w(TAG, "getInstanceId failed", task.getException());
//                                return;
//                            }
//
//                            // Get new Instance ID token
//                            String token = task.getResult().getToken();
//
//                            // Log and toast
//                            String msg = getString(R.string.msg_token_fmt, token);
//                            Log.d(TAG, msg);
//                            Toast.makeText(MyFCMClass.this, msg, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        }
    }

}
