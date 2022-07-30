package com.PastPest.competition1;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class FirstActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        AlarmManager alarmManager = (AlarmManager)getSystemService(this.ALARM_SERVICE);
        GregorianCalendar mCalender = new GregorianCalendar();
        setAlarm(alarmManager);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 1000);





    }

    private void setAlarm(AlarmManager alarmManager) {

        Intent receiverIntent = new Intent(this, AlarmRecevier.class); //AlarmReceiver에 값 전달
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                FirstActivity.this, 0, receiverIntent, 0);   //푸쉬알림 클릭시 앱 실행

        Calendar calendar = Calendar.getInstance();       //푸쉬알림 시간 설정
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(Calendar.getInstance().after(calendar)){       //앱 첫 실행시 알림이 오는 오류 방지
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent); //매일 알림

    }
}