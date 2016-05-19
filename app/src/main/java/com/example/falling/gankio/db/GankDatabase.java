package com.example.falling.gankio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by falling on 16/5/11.
 */
public class GankDatabase extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String TableName = "gank";
    public static final String GANK_DATA = "gank_data";

    public GankDatabase(Context context) {
        super(context, GANK_DATA, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TableName + " (_id PRIMARY KEY ON CONFLICT REPLACE,publishedAt,desc,type,url,who)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
