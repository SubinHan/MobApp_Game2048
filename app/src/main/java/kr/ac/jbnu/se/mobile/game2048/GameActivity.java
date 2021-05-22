package kr.ac.jbnu.se.mobile.game2048;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GameActivity extends AppCompatActivity implements IGameListener {

    public static Context context;
    private static final String DB_PATH = "score.db";

    ImageButton bRestart;
    ImageButton bQuit;
    Button bResume;
    TextView tScore;
    TextView tTimer;
    TextView tHighscore;
    FragmentGame fragment;
    SharedPreferences sharedPreferences;
    Timer timer;
    int originTime;
    int currentTime;
    boolean isNotification;
    boolean hasRegisteredReceiver;

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
        bResume = findViewById(R.id.buttonResume);
        tScore = findViewById(R.id.textScore);
        tScore.setText("0");
        tTimer = findViewById(R.id.textTimer);
        tHighscore = findViewById(R.id.textHighscore);
        frameLayout = findViewById(R.id.frameLayout);
        constraintLayout = findViewById(R.id.constraintLayout);

        frameLayout.setVisibility(View.GONE);

        sharedPreferences = context.getSharedPreferences("appData", context.MODE_PRIVATE);
        loadSp();
        currentTime = originTime;
        timer = new Timer();
        timer.sendEmptyMessage(0);

        DBHandler mHandler;
        Cursor mCursor;
        mHandler = DBHandler.open(context, HighscoreView.DB_PATH);
        mCursor = mHandler.select();
        mCursor.moveToFirst();
        if(mCursor.getCount() != 0)
            tHighscore.setText("" + mCursor.getInt(1));

        broadcastReceiver = new TimeTickReceiver();

        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.view.game.newGame();
                timer.sendEmptyMessage(2);
                tScore.setText("0");
            }
        });

        bQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        bResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePauseGame();
            }
        });
    }

    @Override
    public void moved(SnapshotData snapshotData) {
        tScore.setText(String.valueOf(snapshotData.getScore()));
        timer.sendEmptyMessage(3);
    }

    @Override
    public void gameOver(SnapshotData snapshotData) {
        int score = (int) snapshotData.getScore();
        timer.sendEmptyMessage(1);
        DBHandler mHandler;
        mHandler = DBHandler.open(context, DB_PATH);
        mHandler.insert(score);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(hasRegisteredReceiver) {
            unregisterReceiver(broadcastReceiver);
            hasRegisteredReceiver = false;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        togglePauseGame();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(timer!=null){
            timer.removeMessages(0);
            if(isNotification)
                unregisterReceiver(broadcastReceiver);
        }
    }

    public void togglePauseGame(){
        if (pause_flag == false){
            pause_flag = true;
            timer.sendEmptyMessage(1);
            frameLayout.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
            IntentFilter filter = new IntentFilter();
            filter.addAction(TimeTickReceiver.ACTION_TIME_CHANGED);
            if(isNotification) {
                registerReceiver(broadcastReceiver, filter);
                hasRegisteredReceiver = true;
            }
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
                    if(currentTime==0){
                        tTimer.setText("" + currentTime);
                        removeMessages(0);//타이머 중지
                        fragment.view.game.score -= 1000;
                        fragment.view.game.gameState = -1;
                        fragment.view.game.endGame();
                        fragment.view.resyncTime();
                        fragment.view.invalidate();
                        break;
                    }
                    tTimer.setText("" + currentTime--);
                    sendEmptyMessageDelayed(0,1000);
                    break;
                case 1://일시 정지
                    removeMessages(0);
                    tTimer.setText("" + currentTime);
                    break;
                case 2://정지 후 초기화
                    removeMessages(0);
                    currentTime = originTime;
                    tTimer.setText("" + currentTime--);
                    sendEmptyMessageDelayed(0,1000);
                    break;
                case 3:
                    currentTime = originTime;
                    tTimer.setText("" + currentTime);
            }
        }
    }

    private void loadSp(){//설정 값 불러오기, 존재하지 않을 시 기본값
        originTime = sharedPreferences.getInt("SAVE_TIMER_DATA", 30);
        isNotification = sharedPreferences.getBoolean("SAVE_NOTIFICATION_DATA", false);
    }
}
