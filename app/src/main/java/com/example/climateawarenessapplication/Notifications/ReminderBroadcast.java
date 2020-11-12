package com.example.climateawarenessapplication.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.climateawarenessapplication.R;

public class ReminderBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String title1 = "ClimAware - ToDo List 1";
        String title1_task1 = "\uD83D\uDDD1 Recycle your rubbish at least twice today";
        String title1_task2 = "\uD83D\uDECD Use at least two or more reusable bags";
        String title1_task3 = "\uD83D\uDEBF Spend only 4 - 5 minutes in your shower";
        String title1_task4 = "\uD83D\uDEB4 Going somewhere today? Try cycle it!";
        String title1_task5 = "\uD83D\uDEAE Pick up at least one piece of rubbish today";

        String title2 = "ClimAware - ToDo List 2";
        String title2_task1 = "\uD83D\uDE8C Take the bus at least once today";
        String title2_task2 = "\uD83D\uDCA1 Switch off unused lights in your house";
        String title2_task3 = "\uD83D\uDCBB Try to shop virtually if possible";
        String title2_task4 = "\uD83C\uDF42 Create a compost pile";
        String title2_task5 = "\uD83C\uDF10 Do at least 15 minutes of climate research";

        String title3 = "ClimAware - ToDo List 3";
        String title3_task1 = "\uD83C\uDF72 Start to begin eating locally if possible";
        String title3_task2 = "\uD83E\uDDD1\u200D\uD83C\uDF3E Help out around your garden today";
        String title3_task3 = "\uD83D\uDDA5 Turn off your computer if you are sleeping";
        String title3_task4 = "⏲️Starting trying to not pre-heat your oven";
        String title3_task5 = "\uD83E\uDD5B Begin to recycle your glass where possible";


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.ic_notifications_main)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(title1_task1)
                        .addLine(title1_task2)
                        .addLine(title1_task3)
                        .addLine(title1_task4)
                        .addLine(title1_task5))
                .setContentTitle(title1)
                .setGroup("ClimGroup")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager =  NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());

//        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context, "notify2")
//                .setSmallIcon(R.drawable.ic_notifications_main)
//                .setStyle(new NotificationCompat.InboxStyle()
//                        .addLine(title2_task1)
//                        .addLine(title2_task2)
//                        .addLine(title2_task3)
//                        .addLine(title2_task4)
//                        .addLine(title2_task5))
//                .setContentTitle(title2)
//                .setGroup("ClimGroup")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//        NotificationManagerCompat notificationManager2 =  NotificationManagerCompat.from(context);
//
//        SystemClock.sleep(5000);
//        notificationManager2.notify(201, builder2.build());
//
//        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(context, "notify3")
//                .setSmallIcon(R.drawable.ic_notifications_main)
//                .setStyle(new NotificationCompat.InboxStyle()
//                        .addLine(title3_task1)
//                        .addLine(title3_task2)
//                        .addLine(title3_task3)
//                        .addLine(title3_task4)
//                        .addLine(title3_task5))
//                .setContentTitle(title3)
//                .setGroup("ClimGroup")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//        NotificationManagerCompat notificationManager3 =  NotificationManagerCompat.from(context);
//
//        SystemClock.sleep(5000);
//        notificationManager3.notify(202, builder3.build());


    }
}
