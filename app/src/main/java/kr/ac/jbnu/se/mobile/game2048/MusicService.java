package kr.ac.jbnu.se.mobile.game2048;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
    private MediaPlayer mMediaPlayer;
    public final static String MESSEAGE_KEY = "Music";

    public MusicService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean message = intent.getExtras().getBoolean(MusicService.MESSEAGE_KEY);
        if(message){
            mMediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);

            Intent mMainIntent = new Intent(this, MainActivity.class);
            PendingIntent mPendingIntent = PendingIntent.getActivity(
                    this, 1, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, "channel")
                            .setSmallIcon(R.drawable.background_rectangle)
                            .setContentTitle("Back to Main")
                            .setContentIntent(mPendingIntent)
                            .setContentText("Playing music in the background")
                            .setAutoCancel(true);
            NotificationManager mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotifyManager.createNotificationChannel(new NotificationChannel("channel", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
            }
            mNotifyManager.notify(001,mBuilder.build());
        }else{
            if(mMediaPlayer!=null){
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
                mMediaPlayer.release();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}