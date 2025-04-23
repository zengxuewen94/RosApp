package com.zhh.rosApp.base;

import android.app.Application;




public class BaseApplication extends Application {

    protected static BaseApplication instance;

    public static BaseApplication getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

}
