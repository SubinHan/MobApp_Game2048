package kr.ac.jbnu.se.mobile.game2048;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements IGameListener {

    public static Context context;

    Button bRestart;
    Button bQuit;
    Button bPause;
    Button bResume;
    TextView tScore;
    TextView tTimer;
    FragmentGame fragment;
    Timer timer;
    int time;

    boolean pause_flag = false;
    FrameLayout frameLayout;
    ConstraintLayout constraintLayout;

    BroadcastReceiver broadcastReceiver;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = this;

        fragment = (FragmentGame)getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragment.view.game.setGameListener(this);

        bRestart = findViewById(R.id.buttonRestart);
        bQuit = findViewById(R.id.buttonQuit_Game);
        bPause = findViewById(R.id.buttonPause);
        bResume = findViewById(R.id.buttonResume);
        tScore = findViewById(R.id.textScore);
        tTimer = findViewById(R.id.textTimer);
        frameLayout = findViewById(R.id.frameLayout);
        constraintLayout = findViewById(R.id.constraintLayout);

        frameLayout.setVisibility(View.GONE);

        time = ((SettingsActivity) SettingsActivity.context).getTimer();
        timer = new Timer();
        timer.sendEmptyMessage(0);

        broadcastReceiver = new MyBroadcastReceiver();

        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.view.game.newGame();
                timer.sendEmptyMessage(2);
            }
        });

        bQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save State.
                finish();
            }
        });

        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMyBroadCast();
            }
        });

        bResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMyBroadCast();
            }
        });
    }

    @Override
    public void moved(SnapshotData snapshotData) {
        tScore.setText(String.valueOf(snapshotData.getScore()));
    }

    @Override
    public void gameOver(SnapshotData snapshotData) {
        ((HighscoreActivity) HighscoreActivity.context).insertToDB(snapshotData);
    }

    @Override
    public void onResume(){
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MyBroadcastReceiver.gamePause);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(timer!=null){
            timer.removeMessages(0);
        }
    }

    public void sendMyBroadCast(){
        Intent intent = new Intent();
        intent.setAction(MyBroadcastReceiver.gamePause);
        sendBroadcast(intent);
    }

    public void pauseGame(){
        if (pause_flag == false){
            pause_flag = true;
            timer.sendEmptyMessage(1);
            frameLayout.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
        } else {
            pause_flag = false;
            frameLayout.setVisibility(View.GONE);
            timer.sendEmptyMessage(0);
            constraintLayout.setVisibility(View.VISIBLE);
        }
    }

    class Timer extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 0://시작
                    if(time==0){
                        tTimer.setText("Timer : " + time);
                        removeMessages(0);//타이머 중지
                        break;
                    }
                    tTimer.setText("Timer : " + time--);
                    sendEmptyMessageDelayed(0,1000);
                    break;
                case 1://일시 정지
                    removeMessages(0);
                    tTimer.setText("Timer : " + time);
                    break;
                case 2://정지 후 초기화
                    removeMessages(0);
                    time = ((SettingsActivity)SettingsActivity.context).getTimer();
                    tTimer.setText("Timer : " + time--);
                    sendEmptyMessageDelayed(0,1000);
                    break;
            }
        }
    }
}
