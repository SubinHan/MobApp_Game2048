package kr.ac.jbnu.se.mobile.game2048;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler {
    SQLiteOpenHelper mHelper = null;
    SQLiteDatabase mDB = null;

    public DBHandler(Context context, String name){
        mHelper = new DBHelper(context, name, null, 1);
    }

    public static DBHandler open(Context context, String name){
        return new DBHandler(context, name);
    }

    public Cursor select(){
        mDB = mHelper.getReadableDatabase();
        Cursor mCursor = mDB.query("score",null, null, null, null, null, null);
        return mCursor;
    }

    public void insert(int score){
        mDB = mHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("score", score);

        mDB.insert("score", null, contentValues);
    }

    public void close(){
        mHelper.close();
    }

}
