package kr.ac.jbnu.se.mobile.game2048;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    // TODO: Settings with Shared Preferences: BACKGROUND MUSIC ON/OFF, NOTIFICATION ON/OFF, SET TIMER COUNT

    public static Context context;
    private SharedPreferences appData;

    private boolean saveMusicData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();
    }

    public void load(){//설정 값 불러오기, 존재하지 않을 시 기본값
        saveMusicData = appData.getBoolean("SAVE_MUSIC_DATA", false);
    }

}
