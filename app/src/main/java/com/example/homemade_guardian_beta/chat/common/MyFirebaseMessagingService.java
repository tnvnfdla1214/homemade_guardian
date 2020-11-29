package com.example.homemade_guardian_beta.chat.common;

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

import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.example.homemade_guardian_beta.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


//파이어베이스 클라우드 메세징(FCM) : 메세지를 안정적으로 전송할수있는 교차 플랫폼 메세징 솔루션
//새 이메일이나 기타 데이터를 동기화 할수 있다. 이렇게 알림 메세지를 전송하여 사용자를 유지하고 재참여를 유도할 수 있다.
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("tjrrb","1");
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        Log.d("tjrrb","2");
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }
    // [END on_new_token]



    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        Log.d("tjrrb","11");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("tjrrb","12");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Log.d("tjrrb","13");

        String channelId = getString(R.string.default_notification_channel_id);
        Log.d("tjrrb","14");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Log.d("tjrrb","15");
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.complete)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        Log.d("tjrrb","16");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("tjrrb","17");

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("tjrrb","18");
            NotificationChannel channel = new NotificationChannel("fcm_default_channel",
                    "fcm_default_channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            Log.d("tjrrb","19");
            notificationManager.createNotificationChannel(channel);
            Log.d("tjrrb","20");
        }
        Log.d("tjrrb","21");
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        Log.d("tjrrb","22");
    }
}