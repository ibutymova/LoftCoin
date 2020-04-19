package com.example.butymovaloftcoin.screens.start;

import android.view.View;

import io.reactivex.Observable;

public interface StartView {

    void attach(View view);

    void initView();

    void startAnimations();

    void detach();

    Observable<Object> initEvent();
}
