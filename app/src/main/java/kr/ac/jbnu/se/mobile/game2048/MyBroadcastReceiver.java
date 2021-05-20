package kr.ac.jbnu.se.mobile.game2048;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.TimerTask;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public final static String gamePause = "kr.ac.jbnu.se.mobile.game2048.ACTION_GAME_PAUSE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(gamePause.equals(intent.getAction())){
            ((GameActivity)GameActivity.context).pauseGame();//일시정지, 계속
        }
    }

}
