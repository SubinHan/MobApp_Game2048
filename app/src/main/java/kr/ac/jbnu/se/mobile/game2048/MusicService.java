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
    public boolean isPaused;

    public MusicService() {
        isPaused = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean message = intent.getExtras().getBoolean(MusicService.MESSEAGE_KEY);
        if(message){
            if(mMediaPlayer == null)
                mMediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
            mMediaPlayer.start();
            mMediaPlayer.setLooping(true);
        }else{
            if(mMediaPlayer!=null){
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                }
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