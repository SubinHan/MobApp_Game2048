package kr.ac.jbnu.se.mobile.game2048;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class HighscoreActivity extends AppCompatActivity {
    public static Context context;
    private String DB_PATH = "score.db";

    ListView listView_score = null;
    DBHandler mHandler = null;
    Cursor mCursor = null;
    SimpleCursorAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        context = this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
