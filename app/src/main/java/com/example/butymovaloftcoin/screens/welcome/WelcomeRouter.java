package com.example.butymovaloftcoin.screens.welcome;

import androidx.appcompat.app.AppCompatActivity;

public interface WelcomeRouter {

    void attach(AppCompatActivity activity);

    void startActivityStart();

    void detach();

}
