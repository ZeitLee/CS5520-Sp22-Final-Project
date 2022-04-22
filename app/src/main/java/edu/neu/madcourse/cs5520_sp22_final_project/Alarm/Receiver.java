package edu.neu.madcourse.cs5520_sp22_final_project.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import edu.neu.madcourse.cs5520_sp22_final_project.Alarm.Notification;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("receiver: ");
        String msg = intent.getStringExtra("msg");
        System.out.println(msg);
        new Notification(context).notification(msg);

        int month = intent.getIntExtra("month", 1);
        int year = intent.getIntExtra("year", 2022);
        int day = intent.getIntExtra("day", 1);
        switch (intent.getStringExtra("repeat")) {
            case "Monthly":
                if (month > 12) {
                    month = 1;
                    year++;
                }
                new Alarm(context).fireAlarm(
                        msg, "Monthly", intent.getIntExtra("alarm_no", 0),
                        year, month, day,
                        intent.getIntExtra("hour", 1),
                        intent.getIntExtra("min", 1)
                );
                break;
            case "Daily":
                System.out.println("Daily" + month + day);
                new Alarm(context).fireAlarm(
                        msg, "Daily", intent.getIntExtra("alarm_no", 0),
                        year, month, day + 1,
                        intent.getIntExtra("hour", 1),
                        intent.getIntExtra("min", 1)
                );
                break;
            case "Weekly":
                System.out.println("Weekly" + month + day);
                new Alarm(context).fireAlarm(
                        msg, "Weekly", intent.getIntExtra("alarm_no", 0),
                        year, month, day + 7,
                        intent.getIntExtra("hour", 1),
                        intent.getIntExtra("min", 1)
                );
                break;
            default:
                break;
        }

    }
}
