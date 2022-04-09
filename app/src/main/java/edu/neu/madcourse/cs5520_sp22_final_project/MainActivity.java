package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private AlarmManager alarm;
    private PendingIntent alarmIntent;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
    }

    public void clickListener(View v) {

        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        //month is 0-indexed
        calendar.set(2022, 4 - 1 , 8, 23, 13, 0);



        System.out.println("+++++++++++++++++");
        System.out.println("calendar" + calendar.getTimeInMillis());
        System.out.println("+++++++++++++++++");
        System.out.println("now" + System.currentTimeMillis());
        System.out.println("+++++++++++++++++");


        Intent intent = new Intent(context, Receiver.class);
        PendingIntent pItent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pItent);
    }
}