package com.example.butymovaloftcoin.screens.launch;

import android.app.Activity;
import android.content.Intent;

import com.example.butymovaloftcoin.screens.start.StartActivity;
import com.example.butymovaloftcoin.screens.welcome.WelcomeActivity;

import javax.inject.Inject;

public class LaunchRouterImpl implements LaunchRouter {

    private Activity activity;

    @Inject
    public LaunchRouterImpl() {

    }

    @Override
    public void attach(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void startActivityStart() {
        Intent starter = new Intent(activity, StartActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(starter);
    }

    @Override
    public void StartActivityWelcome() {
        Intent starter = new Intent(activity, WelcomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(starter);
    }

    @Override
    public void detach() {
        activity=null;
    }
}
