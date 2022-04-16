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
        Bundle b = intent.getExtras();
        System.out.println(b.getString("msg"));
        new Notification(context).notification(b.getString("msg"));
    }
}