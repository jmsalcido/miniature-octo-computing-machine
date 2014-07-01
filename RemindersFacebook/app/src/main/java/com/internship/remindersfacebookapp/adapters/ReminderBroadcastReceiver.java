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
import com.internship.remindersfacebookapp.app.R;
import com.internship.remindersfacebookapp.app.ReminderView;
import com.internship.remindersfacebookapp.models.Reminder;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BROADCAST_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        String content=intent.getExtras().get(Reminder.CONTENT).toString();
        String date=intent.getExtras().get(Reminder.DATE).toString();
        String reminderID=intent.getExtras().get(Reminder.ID).toString();
        SQLiteAdapter db = new SQLiteAdapter(context);

        if(db.isReminderExisting(reminderID)) {
            db.updateStateToInactive(reminderID);

            if (Session.getActiveSession().getState() == SessionState.CLOSED) {
                Log.w(TAG, Session.getActiveSession().getState().toString());
            } else {
                Intent notificationIntent = new Intent(context, ReminderView.class);
                notificationIntent.putExtra(Reminder.CONTENT,content);
                notificationIntent.putExtra(Reminder.DATE,date);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(content)
                                .setContentText(date);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                mBuilder.setAutoCancel(true);
                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(Integer.parseInt(reminderID), mBuilder.build());
            }
        }
        db.close();
    }
}
