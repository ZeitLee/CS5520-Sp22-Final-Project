package edu.neu.madcourse.cs5520_sp22_final_project.Alarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.neu.madcourse.cs5520_sp22_final_project.R;

public class Notification {
    private final NotificationManagerCompat notificationManagerCompat;
    private static final String CID = "CID_1";
    private static final String MESSAGE = "Reminder ";
    private static int NID = 1;
    private final Context context;

    public Notification(Context context) {
        this.context = context;
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(new NotificationChannel(CID, "channel1",
                NotificationManager.IMPORTANCE_HIGH));
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
    }

    public void notification(String msg) {
        android.app.Notification n = new NotificationCompat.Builder(context, CID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(MESSAGE)
                .setContentText(msg)
                .build();
        this.notificationManagerCompat.notify(NID++, n);
    }
}
