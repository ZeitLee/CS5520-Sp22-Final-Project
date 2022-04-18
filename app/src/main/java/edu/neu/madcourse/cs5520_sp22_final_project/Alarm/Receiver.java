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
        System.out.println(intent.getStringExtra("msg"));
        new Notification(context).notification(intent.getStringExtra("msg"));
    }
}
