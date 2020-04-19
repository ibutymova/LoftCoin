package com.example.butymovaloftcoin.screens.start;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.butymovaloftcoin.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class StartViewImpl implements StartView {

    @BindView(R.id.ivStartBottomCorner)
    ImageView ivStartBottomCorner;

    @BindView(R.id.ivStartTopCorner)
    ImageView ivStartTopCorner;

    private PublishSubject<Object> initEvent = PublishSubject.create();

    private View view;
    private Unbinder unbinder;

    @Inject
    StartViewImpl() {

    }

    @Override
    public void attach(View view) {
        this.view = view;
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void initView() {
        initEvent.onNext(new Object());
    }

    @Override
    public void startAnimations(){
        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(ivStartBottomCorner, "rotation", 0, 360);
        innerAnimator.setDuration(30000);
        innerAnimator.setRepeatMode(ValueAnimator.RESTART);
        innerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        innerAnimator.setInterpolator(new LinearInterpolator());

        ObjectAnimator outerAnimator = ObjectAnimator.ofFloat(ivStartBottomCorner, "rotation", 0, -360);
        outerAnimator.setDuration(60000);
        outerAnimator.setRepeatMode(ValueAnimator.RESTART);
        outerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        outerAnimator.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(innerAnimator).with(outerAnimator);
        set.start();
    }

    @Override
    public Observable<Object> initEvent() {
        return initEvent;
    }

    @Override
    public void detach() {
        unbinder.unbind();
        this.view = null;
    }
}
