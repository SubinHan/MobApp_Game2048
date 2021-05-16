package kr.ac.jbnu.se.mobile.game2048;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    // TODO: Settings with Shared Preferences: BACKGROUND MUSIC ON/OFF, NOTIFICATION ON/OFF, SET TIMER COUNT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
