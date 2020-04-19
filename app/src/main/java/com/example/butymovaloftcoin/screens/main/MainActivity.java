package com.example.butymovaloftcoin.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.di.DaggerMainComponent;
import com.example.butymovaloftcoin.di.MainComponent;

public class MainActivity extends AppCompatActivity {

    private MainPresenter presenter;
    private MainView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(view);

        MainComponent component = DaggerMainComponent.createComponent(App.getAppComponent());
        contentView = component.getMainView();
        contentView.attach(view, savedInstanceState);
        MainRouter router = component.getMainRouter();
        router.attach(this);
        presenter = component.getMainPresenter();
        presenter.attach(contentView, router);
        contentView.initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        contentView.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
       presenter.detach();
       if (isFinishing())
           DaggerMainComponent.clearComponent();
       super.onDestroy();
   }
}
