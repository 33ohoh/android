package com.PastPest.competition1;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmRecevier extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String CHANNEL_ID = "pestAlarm";
        NotificationManager mNotificationManager
                = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "병해충 알림",mNotificationManager.IMPORTANCE_HIGH); //휴대폰 알림 설정에 표시되는 이름

            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");

            mNotificationManager.createNotificationChannel(notificationChannel); //manager로 Channel 생성
        }

        // Notivication에 대한 ID 생성
        int NOTIFICATION_ID = 0;

        Intent notificationIntent = new Intent(context, FirstActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("오늘의 병해충 소식")
                .setContentText("병해충 ")
                .setSmallIcon(R.drawable.pest)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true); //알림창 클릭시 상단바에서 알림창을 없앰

        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
}
