package com.example.spite;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class CloudMsg extends FirebaseMessagingService {

    private Context context;

    public void onNewToken(String token){
        Log.d("New Token", " Refreshed token: " + token);
        //RegisterToken(token);
    }




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("Error", "From: " + remoteMessage.getFrom());

        if(remoteMessage.getData().size()>0){
            Log.d("CloudMsg","DAta payload:" +remoteMessage.getData());
            if(true)
            {
                WorkRequest work = new OneTimeWorkRequest.Builder(WorkerForCloud.class).build();
                WorkManager.getInstance(context).beginWith((OneTimeWorkRequest)work).enqueue();
            }
            else
            {
               Log.d("CloudMsg","Short task done.");
            }
            if(remoteMessage.getNotification()!= null){
                Log.d("CloudMsg", "Notification Body: " + remoteMessage.getNotification().getBody());
            }
            generateNotification(remoteMessage);
        }
    }
    private void generateNotification(RemoteMessage remoteMessage) {

        Map<String,String> data = remoteMessage.getData();
        String title = data.get("Title");
        String body = data.get("body");
        String channelId = getString(R.string.default_notification_channel_id);
        //Takes the user to the Start workout screen when they tap on the notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //for android O and higher because it requires notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Spite Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Spite");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, notiBuilder.build());

    }

}