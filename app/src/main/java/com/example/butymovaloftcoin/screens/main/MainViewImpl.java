package com.example.butymovaloftcoin.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import com.example.butymovaloftcoin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MainViewImpl implements MainView,
        BottomNavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener{

    public static final String KEY_OPEN_FROM_NOTIFICATION = "open_from_notification";

    private View view;
    private Bundle savedInstanceState;
    private PublishSubject<Integer> itemBottomNavigationClickEvent = PublishSubject.create();
    private PublishSubject<Bundle> initEvent = PublishSubject.create();
    private PublishSubject<Boolean> newIntentEvent = PublishSubject.create();

    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;

    private Unbinder unbinder;

    @Inject
    public MainViewImpl() {

    }

    @Override
    public void attach(View view, Bundle savedInstanceState) {
        this.view = view;
        this.savedInstanceState = savedInstanceState;

        unbinder = ButterKnife.bind(this, view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemReselectedListener(this);
    }

    @Override
    public void initView() {
        initEvent.onNext(savedInstanceState == null ? new Bundle() : savedInstanceState );
    }

    @Override
    public void onNewIntent(Intent intent) {
        newIntentEvent.onNext(intent.getBooleanExtra(KEY_OPEN_FROM_NOTIFICATION, false));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        itemBottomNavigationClickEvent.onNext(item.getItemId());
        return true;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        itemBottomNavigationClickEvent.onNext(item.getItemId());
    }

    @Override
    public void setSelectedItemId(@IdRes int id) {
        bottomNavigationView.setSelectedItemId(id);
    }

    @Override
    public void detach() {
        unbinder.unbind();
        view = null;
    }

    @Override
    public Observable<Integer> itemBottomNavigationClickEvent() {
        return itemBottomNavigationClickEvent;
    }

    @Override
    public Observable<Bundle> initEvent() {
        return initEvent;
    }

    @Override
    public Observable<Boolean> newIntentEvent() {
        return newIntentEvent;
    }
}
