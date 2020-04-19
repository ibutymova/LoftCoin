package com.example.butymovaloftcoin.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;

import io.reactivex.Observable;

public interface MainView {

    void attach(View view, Bundle savedInstanceState);

    void initView();

    void onNewIntent(Intent intent);

    void setSelectedItemId(@IdRes int id);

    void detach();

    Observable<Integer> itemBottomNavigationClickEvent();

    Observable<Bundle> initEvent();

    Observable<Boolean> newIntentEvent();

}
