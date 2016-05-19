package com.example.falling.gankio.application;

import android.app.Application;

import com.example.falling.gankio.db.GankDatabaseUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by falling on 16/5/8.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        GankDatabaseUtil.initialize(this);

    }
}
