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

        //trigger alarm in the next month
        if (intent.getStringExtra("repeat").equals("Monthly")) {
            int month = intent.getIntExtra("month", 1);
            System.out.println("month getting from intent " + month);
            int year = intent.getIntExtra("year", 2022);
            month += 1;
            //if month > 12, means to go to the next year's first month.
            if (month > 12) {
                month = 1;
                year++;
            }
            new Alarm(context).fireAlarm(
                    msg, "Monthly", intent.getIntExtra("alarm_no", 0),
                    year, month,
                    intent.getIntExtra("day", 1),
                    intent.getIntExtra("hour", 1),
                    intent.getIntExtra("min", 1)
            );
        }
    }
}
