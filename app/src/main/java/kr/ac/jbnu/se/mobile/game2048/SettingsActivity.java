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

    private CheckBox backgroundMusic;
    private boolean saveMusicData;

    private EditText setTimer;
    private Button setTimerButton;
    private int saveTimerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        backgroundMusic = findViewById(R.id.backgroundMusic);
        setTimer = findViewById(R.id.setTimer);
        setTimerButton = findViewById(R.id.setTimerButton);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        if(saveMusicData){
            backgroundMusic.setChecked(true);
            Intent intent = new Intent(this, MusicService.class);
            intent.putExtra(MusicService.MESSEAGE_KEY, true);
            startService(intent);
        }
        backgroundMusic.setOnClickListener(v -> {
            Intent intent = new Intent(this, MusicService.class);
            if(backgroundMusic.isChecked()){
                saveMusic(true);
                intent.putExtra(MusicService.MESSEAGE_KEY, true);
            }else{
                saveMusic(false);
                intent.putExtra(MusicService.MESSEAGE_KEY, false);
            }
            startService(intent);
        });

        if(saveTimerData != 0){
            setTimer.setText(String.valueOf(saveTimerData));
        }
        setTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimer(Integer.parseInt(setTimer.getText().toString()));
                Toast.makeText(getApplicationContext(),"Timer Set Complete", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void load(){//설정 값 불러오기, 존재하지 않을 시 기본값
        saveMusicData = appData.getBoolean("SAVE_MUSIC_DATA", false);
        saveTimerData = appData.getInt("SAVE_TIMER_DATA",0);
    }

    public void saveMusic(boolean value){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_MUSIC_DATA", value);
        editor.apply();
    }

    public void saveTimer(int time){
        SharedPreferences.Editor editor = appData.edit();
        editor.putInt("SAVE_TIMER_DATA", time);
        editor.apply();
    }
    public int getTimer(){
        saveTimerData = appData.getInt("SAVE_TIMER_DATA", 0);
        return saveTimerData;
    }
}
