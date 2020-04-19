package com.example.butymovaloftcoin.screens.start;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.butymovaloftcoin.screens.main.MainActivity;

import javax.inject.Inject;

public class StartRouterImpl implements StartRouter {

    private AppCompatActivity activity;

    @Inject
    public StartRouterImpl() {

    }

    @Override
    public void attach(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(activity, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        activity.startActivity(intent);
    }

    @Override
    public void showToast(Integer resId) {
        Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        activity.finishAndRemoveTask();
    }

    @Override
    public void detach() {
        activity = null;
    }
}
