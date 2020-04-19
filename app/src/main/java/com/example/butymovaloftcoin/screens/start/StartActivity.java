package com.example.butymovaloftcoin.screens.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.di.DaggerStartComponent;
import com.example.butymovaloftcoin.di.StartComponent;

public class StartActivity extends AppCompatActivity {

    private StartPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(this).inflate(R.layout.activity_start, null);
        setContentView(view);

        StartComponent component = DaggerStartComponent.createComponent(App.getAppComponent());

        StartView contentView = component.getStartView();
        contentView.attach(view);
        StartRouter router = component.getStartRouter();
        router.attach(this);
        presenter = component.getStartPresenter();
        presenter.attach(contentView, router);
        contentView.initView();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        if (isFinishing())
            DaggerStartComponent.clearComponent();
        super.onDestroy();
    }
}

