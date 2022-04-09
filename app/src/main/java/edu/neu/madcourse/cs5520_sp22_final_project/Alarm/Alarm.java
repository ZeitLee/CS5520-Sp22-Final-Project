package edu.neu.madcourse.cs5520_sp22_final_project.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class Alarm {
    private final Context context;

    public Alarm(Context context) {
        this.context = context;
    }

    /**
     * trigger alarm based on date and msg
     */
    public void fireAlarm() {
        Calendar calendar = Calendar.getInstance();
        //month is 0-indexed
        calendar.set(2022, 4 - 1 , 9, 16, 15, 0);

        System.out.println("+++++++++++++++++");
        System.out.println("calendar" + calendar.getTimeInMillis());
        System.out.println("+++++++++++++++++");
        System.out.println("now" + System.currentTimeMillis());
        System.out.println("+++++++++++++++++");

        Intent intent = new Intent(context, Receiver.class);
        PendingIntent pItent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pItent);
    }
}
