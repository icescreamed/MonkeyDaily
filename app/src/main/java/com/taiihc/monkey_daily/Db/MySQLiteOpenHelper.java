package com.taiihc.monkey_daily.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tai on 2016/6/19.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_COLLECTION_TABLE = "create table Collection ( " +
            "id integer primary key autoincrement, " +
            "_id integer, " +
            " _content text)";
    public static final String COLLECTION_ID = "_id";
    public static final String COLLECTION_CONTENT = "_content";
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COLLECTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
