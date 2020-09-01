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

import androidx.core.app.NotificationCompat;

import com.example.homemade_guardian_beta.Main.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


//파이어베이스 클라우드 메세징(FCM) : 메세지를 안정적으로 전송할수있는 교차 플랫폼 메세징 솔루션
//새 이메일이나 기타 데이터를 동기화 할수 있다. 이렇게 알림 메세지를 전송하여 사용자를 유지하고 재참여를 유도할 수 있다.
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            //이건 안바꿔야 하는듯
            String title = remoteMessage.getData().get("title").toString();
            String body = remoteMessage.getData().get("body").toString();
            sendNotification(body);
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        //.setSmallIcon(R.drawable.splash)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
