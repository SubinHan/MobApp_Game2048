package kr.ac.jbnu.se.mobile.game2048;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class GameActivity extends AppCompatActivity implements IGameListener {

    Button bRestart;
    Button bQuit;
    TextView tScore;
    FragmentGame fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        fragment = (FragmentGame)getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragment.view.game.setGameListener(this);

        bRestart = findViewById(R.id.buttonRestart);
        bQuit = findViewById(R.id.buttonQuit_Game);
        tScore = findViewById(R.id.textScore);

        bRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.view.game.newGame();
            }
        });

        bQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Save State.
                finish();
            }
        });
    }

    @Override
    public void moved(SnapshotData snapshotData) {
        tScore.setText(String.valueOf(snapshotData.getScore()));
    }

    @Override
    public void gameOver(SnapshotData snapshotData) {

    }
}
