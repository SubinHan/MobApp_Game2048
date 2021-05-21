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

        listView_score = findViewById(R.id.listView_score);

        if(mHandler == null){
            mHandler = DBHandler.open(HighscoreActivity.this, DB_PATH);
        }
        mHandler.insert(500);

        mCursor = mHandler.select();

        String[] from = {"score"};
        int[] to = {android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_activated_2, mCursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView_score.setAdapter(mAdapter);
    }

    public void insertToDB(SnapshotData snapshotData){
        int score = (int) snapshotData.getScore();

        mHandler.insert(score);
        mCursor = mHandler.select();//db 새로 가져오기
        mAdapter.changeCursor(mCursor);//adapter에 변경된 cursor 설정
        mAdapter.notifyDataSetChanged();//업데이트
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.close();
    }
}
