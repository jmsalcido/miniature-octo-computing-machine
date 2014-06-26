package com.internship.remindersfacebookapp.adapters;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.internship.remindersfacebookapp.app.MainActivity;
import com.internship.remindersfacebookapp.models.Reminder;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BROADCAST_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        SQLiteAdapter db = new SQLiteAdapter(context);
        db.updateStateToInactive(intent.getExtras().get(Reminder.ID).toString());
        if(Session.getActiveSession().getState() == SessionState.CLOSED){
            Log.w(TAG, Session.getActiveSession().getState().toString());
        }else {
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.star_on)
                            .setContentTitle(intent.getExtras().get(Reminder.CONTENT).toString())
                            .setContentText(intent.getExtras().get(Reminder.DATE).toString());
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }
}
