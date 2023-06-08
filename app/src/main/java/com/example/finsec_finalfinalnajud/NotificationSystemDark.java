package com.example.finsec_finalfinalnajud;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationSystemDark extends AppCompatActivity {

    private static final String CHANNEL_ID = "finsec";
    private static final String CHANNEL_NAME = "Finsec.";
    private static final String CHANNEL_DESC = "Finsec. Notification System";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_dark);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        findViewById(R.id.imgbtnnotif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNotification();
            }
        });
    }

    private void displayNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setContentTitle("Yay, Its working!")
                        .setContentText("Notification is working!")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(1, mBuilder.build());
    }
}
