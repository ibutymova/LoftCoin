package com.example.butymovaloftcoin.screens.welcome;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import com.example.butymovaloftcoin.screens.start.StartActivity;

import javax.inject.Inject;

public class WelcomeRouterImpl implements WelcomeRouter {

    private AppCompatActivity activity;

    @Inject
    WelcomeRouterImpl() {

    }

    @Override
    public void attach(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void startActivityStart() {
        Intent starter = new Intent(activity, StartActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(starter);
    }

    @Override
    public void detach() {
        this.activity = null;
    }
}
