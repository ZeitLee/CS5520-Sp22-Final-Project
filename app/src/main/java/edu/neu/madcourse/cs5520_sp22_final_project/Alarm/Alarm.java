package edu.neu.madcourse.cs5520_sp22_final_project.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

public class Alarm {
    private final Context context;
    private int ALARM_NO = 10000000 + new Random().nextInt(900000000);

    public Alarm(Context context) {
        this.context = context;
    }

    /**
     * trigger alarm based on date and msg
     */
    public int fireAlarm(String msg, String repeat, int existedAlarmNo, int year, int month, int day, int hour, int min) {
        ALARM_NO = existedAlarmNo == 0 ? ALARM_NO : existedAlarmNo;
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
        System.out.println("Alarm_No: " + ALARM_NO);
        switch (repeat) {
            case "None":
                System.out.println("none");
                break;
            case "Daily":
                System.out.println("Daily");
                break;
            case "Weekly":
                System.out.println("Weekly");
                break;
            case "Monthly":
                System.out.println("Monthly");
                break;
        }
        PendingIntent pItent = PendingIntent.getBroadcast(context, ALARM_NO, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pItent);

        return ALARM_NO;
    }
}
