package kr.ac.jbnu.se.mobile.game2048;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TimeTickReceiver extends BroadcastReceiver {

    public final static String ACTION_TIME_CHANGED = "android.intent.action.TIME_TICK";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(ACTION_TIME_CHANGED.equals(intent.getAction())){
            Intent mMainIntent = new Intent(context, MainActivity.class);
            mMainIntent.setAction(Intent.ACTION_MAIN);
            mMainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
                    mMainIntent, 0);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, "channel")
                            .setSmallIcon(R.drawable.background_rectangle)
                            .setContentTitle("Back to Game")
                            .setContentIntent(mPendingIntent)
                            .setContentText("Complete the game and win!")
                            .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(001, mBuilder.build());
        }
    }

}
