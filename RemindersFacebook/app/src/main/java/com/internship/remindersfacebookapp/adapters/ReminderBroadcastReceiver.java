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
import com.internship.remindersfacebookapp.app.R;
import com.internship.remindersfacebookapp.app.ReminderView;
import com.internship.remindersfacebookapp.models.Reminder;
import com.internship.remindersfacebookapp.models.RemindersUser;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BROADCAST_RECEIVER";
    private String mContent;
    private String mDate;
    private String mReminderId;
    private String mReminderUserId;

    @Override
    public void onReceive(Context context, Intent intent) {
        String mCurrentUserId = MainActivity.mCurrentUserId;
        mContent = intent.getExtras().get(Reminder.CONTENT).toString();
        mDate = intent.getExtras().get(Reminder.DATE).toString();
        mReminderId = intent.getExtras().get(Reminder.ID).toString();
        mReminderUserId = intent.getExtras().get(RemindersUser.USER_ID).toString();
        SQLiteAdapter db = new SQLiteAdapter(context);
        if (db.isReminderExisting(mReminderId)) {
            db.updateStateToInactive(mReminderId);
            if(Session.getActiveSession() != null && !mCurrentUserId.equals(null)){
                if (Session.getActiveSession().getState() == SessionState.CLOSED
                        && !MainActivity.mGoogleApiClient.isConnected()) {
                    Log.w(TAG, "Nothing connected, fine");
                } else {
                    String notificationUserReminderId = db.getUserIdForNotificationReminder(mReminderUserId, mReminderId);
                    if (notificationUserReminderId.equals(mCurrentUserId)) {
                        createNotification(context);
                    } else {
                        Log.w(TAG, mCurrentUserId + " not equals " + notificationUserReminderId);
                    }
                }
            }
        }
        db.close();
    }

    private void createNotification(Context context) {
        Intent notificationIntent = new Intent(context, ReminderView.class);
        notificationIntent.putExtra(Reminder.CONTENT, mContent);
        notificationIntent.putExtra(Reminder.DATE, mDate);
        notificationIntent.setAction("foo");
        PendingIntent contentIntent = PendingIntent.getActivity(context, Integer.parseInt(mReminderId), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(mContent)
                        .setContentText(mDate)
                        .setContentIntent(contentIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Integer.parseInt(mReminderId), mBuilder.build());
    }
}
