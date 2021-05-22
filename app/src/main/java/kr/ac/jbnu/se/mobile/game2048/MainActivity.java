package kr.ac.jbnu.se.mobile.game2048;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences appData;

    private boolean saveMusicData;

    DBHandler mHandler;
    Cursor mCursor;
    TextView tHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tHighscore = findViewById(R.id.textHighscore);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if(saveMusicData){
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra(MusicService.MESSEAGE_KEY, true);
            startService(intent);
        }

        mHandler = DBHandler.open(this, HighscoreView.DB_PATH);
        mCursor = mHandler.select();
        mCursor.moveToFirst();
        if(mCursor.getCount() != 0)
            tHighscore.setText("" + mCursor.getInt(1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCursor = mHandler.select();
        mCursor.moveToFirst();
        tHighscore.setText("" + mCursor.getInt(1));
    }

    public void load(){//설정 값 불러오기, 존재하지 않을 시 기본값
        saveMusicData = appData.getBoolean("SAVE_MUSIC_DATA", false);
    }

}