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
import com.internship.remindersfacebookapp.app.LoginFragment;
import com.internship.remindersfacebookapp.app.R;
import com.internship.remindersfacebookapp.app.ReminderView;
import com.internship.remindersfacebookapp.models.Reminder;
import com.internship.remindersfacebookapp.models.RemindersUser;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BROADCAST_RECEIVER";
    private String mContent;
    private String mDate;
    private String mReminderID;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContent =intent.getExtras().get(Reminder.CONTENT).toString();
        mDate =intent.getExtras().get(Reminder.DATE).toString();
        mReminderID =intent.getExtras().get(Reminder.ID).toString();
        SQLiteAdapter db = new SQLiteAdapter(context);

        if(db.isReminderExisting(mReminderID)) {
            db.updateStateToInactive(mReminderID);
            if (RemindersUser.IS_FB_USER) {
                if (Session.getActiveSession().getState() == SessionState.CLOSED) {
                    Log.w(TAG, Session.getActiveSession().getState().toString());
                } else {
                    createNotification(context);
                }
            }else{
                if (!LoginFragment.mGoogleApiClient.isConnected()) {
                    Log.w(TAG, String.valueOf(LoginFragment.mGoogleApiClient.isConnected()));
                } else {
                    createNotification(context);
                }
            }

        }
        db.close();
    }

    private void createNotification(Context context){
        Intent notificationIntent = new Intent(context, ReminderView.class);
        notificationIntent.putExtra(Reminder.CONTENT, mContent);
        notificationIntent.putExtra(Reminder.DATE, mDate);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(mContent)
                        .setContentText(mDate);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Integer.parseInt(mReminderID), mBuilder.build());
    }
}
