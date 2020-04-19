package com.example.butymovaloftcoin.screens.start;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

public interface StartRouter {

    void attach(AppCompatActivity activity);

    void navigateToMainScreen();

    void finish();

    void detach();

    void showToast(@StringRes Integer resId);
}
