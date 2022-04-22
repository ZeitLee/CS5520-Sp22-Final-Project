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

import edu.neu.madcourse.cs5520_sp22_final_project.SchedulePermission;

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

        switch (repeat) {
            case "None":
                System.out.println("none");
                break;
            case "Daily":
                if (day > maxDay) {
                    day -= maxDay;
                    month++;
                }
                System.out.println(msg + "" + year + ", " + month + ", " + day + ", " + hour + ", " + min);
                calendar.set(year, month - 1 , day, hour, min, 0);
                break;
            case "Weekly":
                if (day > maxDay) {
                    day -= maxDay;
                    month++;
                }
                System.out.println(msg + "" + year + ", " + month + ", " + day + ", " + hour + ", " + min);
                calendar.set(year, month - 1 , day, hour, min, 0);
                break;
            case "Monthly":
                System.out.println("Monthly");
                //month is 0-indexed
                System.out.println(msg + "" + year + ", " + month + ", " +
                        Math.min(day, maxDay) + ", " + hour + ", " + min);
                calendar.set(year, month - 1 , Math.min(day, maxDay), hour, min, 0);
                break;
            default:
                break;
        }

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
        PendingIntent pItent;
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pItent = PendingIntent.getBroadcast(context, ALARM_NO, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);
            System.out.println(alarm.canScheduleExactAlarms());
            if (!alarm.canScheduleExactAlarms()) {
                Intent permission = new Intent(context, SchedulePermission.class);
                permission.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(permission);
            }
        } else {
            pItent = PendingIntent.getBroadcast(context, ALARM_NO, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pItent);

        return ALARM_NO;
    }
}
