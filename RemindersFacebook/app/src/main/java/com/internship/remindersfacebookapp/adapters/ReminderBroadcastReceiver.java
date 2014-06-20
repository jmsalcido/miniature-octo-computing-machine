package com.internship.remindersfacebookapp.adapters;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.facebook.Session;
import com.facebook.SessionState;
import com.internship.remindersfacebookapp.app.MainActivity;
import com.internship.remindersfacebookapp.models.Reminder;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Session.getActiveSession().getState() == SessionState.CLOSED){
            SQLiteAdapter db = new SQLiteAdapter(context);
            db.updateStateToInactive(intent.getExtras().get(Reminder.ID).toString());
        }else {
            SQLiteAdapter db = new SQLiteAdapter(context);
            db.updateStateToInactive(intent.getExtras().get(Reminder.ID).toString());
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.star_big_on)
                            .setContentTitle("You have a reminder expired")
                            .setContentText("better check it out");
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
