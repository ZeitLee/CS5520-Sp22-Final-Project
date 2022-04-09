package edu.neu.madcourse.cs5520_sp22_final_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification {
    private final NotificationManagerCompat notificationManagerCompat;
    private static final String CID = "CID_1";
    private static final String MESSAGE = "You received a sticker from ";
    private static int NID = 1;
    private final Context context;

    public Notification(Context context) {
        this.context = context;
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(new NotificationChannel(CID, "channel1",
                NotificationManager.IMPORTANCE_HIGH));
        this.notificationManagerCompat = NotificationManagerCompat.from(context);
    }

    public void notification(String username) {
        android.app.Notification n = new NotificationCompat.Builder(context, CID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(MESSAGE)
                .setContentText(username)
                .build();
        this.notificationManagerCompat.notify(NID++, n);
    }
}
