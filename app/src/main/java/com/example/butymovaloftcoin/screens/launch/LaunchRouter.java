package com.example.butymovaloftcoin.screens.launch;

import android.app.Activity;

public interface LaunchRouter {

    void attach(Activity activity);

    void startActivityStart();

    void StartActivityWelcome();

    void detach();
}
