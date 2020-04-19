package com.example.butymovaloftcoin.screens.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.di.DaggerWelcomeComponent;
import com.example.butymovaloftcoin.di.WelcomeComponent;

public class WelcomeActivity extends AppCompatActivity  {

    private WelcomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(this).inflate(R.layout.activity_welcome, null);
        setContentView(view);

        WelcomeComponent component = DaggerWelcomeComponent.createComponent(App.getAppComponent());
        WelcomeView contentView = component.getWelcomeView();
        contentView.attach(view, getSupportFragmentManager());
        WelcomeRouter router = component.getWelcomeRouter();
        router.attach(this);
        presenter = component.getWelcomePresenter();
        presenter.attach(contentView, router);
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        if (isFinishing())
            DaggerWelcomeComponent.clearComponent();
        super.onDestroy();
    }
}
