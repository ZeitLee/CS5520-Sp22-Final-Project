package edu.neu.madcourse.cs5520_sp22_final_project.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

public class Alarm {
    private final Context context;

    public Alarm(Context context) {
        this.context = context;
    }

    /**
     * trigger alarm based on date and msg
     */
    public void fireAlarm(String msg, int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        //month is 0-indexed
        System.out.println(msg + "" + year + ", " + month + ", " + day + ", " + hour + ", " + min);
        calendar.set(year, month - 1 , day, hour, min, 0);

        System.out.println("+++++++++++++++++");
        System.out.println("calendar" + calendar.getTimeInMillis());
        System.out.println("now" + System.currentTimeMillis());
        System.out.println("+++++++++++++++++");

        Intent intent = new Intent(context, Receiver.class);
        intent.putExtra("msg", msg);
        PendingIntent pItent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pItent);
    }
}
