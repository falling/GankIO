package com.example.falling.gankio.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.falling.gankio.GankBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by falling on 16/5/11.
 */
public class GankDatabaseUtil {

    private static Context mContext;

    public static void initialize(Context context) {
        mContext = context;
    }

    private GankDatabaseUtil() {
    }

    public static void insert(GankBean gankBean) {
        GankDatabase weatherDatabase = new GankDatabase(mContext);
        SQLiteDatabase db = weatherDatabase.getWritableDatabase();
        for (GankBean.Result result : gankBean.getResults()) {
            db.execSQL(result.getInsertSql());
        }
        db.close();
    }

    public static List<GankBean.Result> queryAll() {
        GankDatabase weatherDatabase = new GankDatabase(mContext);
        SQLiteDatabase db = weatherDatabase.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + GankDatabase.TableName, null);
        List<GankBean.Result> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            GankBean.Result item = new GankBean.Result();

            for (int i = 0; i < cursor.getColumnCount(); i++) {
                try {
                    Field field = item.getClass().getDeclaredField(cursor.getColumnName(i));
                    field.setAccessible(true);
                    field.set(item, cursor.getString(i));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            result.add(item);
        }
        cursor.close();
        db.close();
        return result;
    }
}
