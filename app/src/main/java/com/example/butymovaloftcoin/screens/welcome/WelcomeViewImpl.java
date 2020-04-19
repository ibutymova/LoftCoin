package com.example.butymovaloftcoin.screens.welcome;

import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.screens.welcome.welcomeFragment.WelcomePageAdapter;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class WelcomeViewImpl implements WelcomeView, View.OnClickListener {

    private PublishSubject<Object> onStartClickedEvent = PublishSubject.create();

    @BindView(R.id.start_btn)
    Button start_btn;

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.tabs)
    TabLayout tabs;

    private Unbinder unbinder;

    @Inject
    public WelcomeViewImpl() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_btn:{
                onStartClickedEvent.onNext(new Object());
            }
        }
    }

    @Override
    public void attach(View view, FragmentManager fragmentManager) {
        unbinder = ButterKnife.bind(this, view);
        pager.setAdapter(new WelcomePageAdapter(fragmentManager));
        tabs.setupWithViewPager(pager, true);
        start_btn.setOnClickListener(this);
    }

    @Override
    public void detach() {
        unbinder.unbind();
    }

    @Override
    public Observable<Object> onStartClickedEvent() {
        return onStartClickedEvent;
    }
}
