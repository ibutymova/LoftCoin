package com.example.butymovaloftcoin.screens.welcome;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import io.reactivex.Observable;

public interface WelcomeView {

    void attach(View view, FragmentManager fragmentManager);

    void detach();

    Observable<Object> onStartClickedEvent();

}
