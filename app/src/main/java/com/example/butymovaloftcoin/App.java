package com.example.butymovaloftcoin;

import android.app.Application;
import com.example.butymovaloftcoin.di.AppComponent;
import com.example.butymovaloftcoin.di.DaggerAppComponent;

public class App extends Application {
    static private volatile AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.createComponent(this);
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
