package com.example.butymovaloftcoin.screens.main;

import androidx.appcompat.app.AppCompatActivity;

public interface MainRouter {

    void attach(AppCompatActivity activity);

    void showRateFragment();

    void showWalletsFragment();

    void showConverterFragment();

    void detach();

}
