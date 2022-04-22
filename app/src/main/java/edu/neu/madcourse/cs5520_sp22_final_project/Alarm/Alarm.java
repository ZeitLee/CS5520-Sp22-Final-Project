package edu.neu.madcourse.cs5520_sp22_final_project.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import java.time.Year;
import java.time.YearMonth;
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

        //check if the day is exceed the max day of that month.
        YearMonth yearMonth = YearMonth.of(year, month);
        int maxDay = yearMonth.lengthOfMonth();

        //month is 0-indexed
        System.out.println(msg + "" + year + ", " + month + ", " +
                Math.min(day, maxDay) + ", " + hour + ", " + min);
        calendar.set(year, month - 1 , Math.min(day, maxDay), hour, min, 0);

        System.out.println("+++++++++++++++++");
        System.out.println("calendar" + calendar.getTimeInMillis());
        System.out.println("now" + System.currentTimeMillis());
        System.out.println("+++++++++++++++++");

        Intent intent = new Intent(context, Receiver.class);
        intent.putExtra("msg", msg);
        intent.putExtra("repeat", repeat);
        intent.putExtra("alarm_no", ALARM_NO);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        intent.putExtra("hour", hour);
        intent.putExtra("min", min);

        System.out.println("Alarm_No: " + ALARM_NO);
        PendingIntent pItent = PendingIntent.getBroadcast(context, ALARM_NO, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarm.canScheduleExactAlarms()) {
//               intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            }
        }

        switch (repeat) {
            case "None":
                System.out.println("none");
                alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pItent);
                break;
            case "Daily":
                System.out.println("Daily");
                alarm.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), 24*60*60*1000,
                        pItent);
                break;
            case "Weekly":
                System.out.println("Weekly");
                alarm.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), 7*24*60*60*1000,
                        pItent);
                break;
            case "Monthly":
                System.out.println("Monthly");
                alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pItent);
                break;
            default:
                break;
        }

        return ALARM_NO;
    }
}
