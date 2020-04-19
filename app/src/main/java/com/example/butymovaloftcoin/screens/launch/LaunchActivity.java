package com.example.butymovaloftcoin.screens.launch;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.di.DaggerLaunchComponent;
import com.example.butymovaloftcoin.di.LaunchComponent;

public class LaunchActivity extends Activity {

    private LaunchPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LaunchComponent component = DaggerLaunchComponent.createComponent(App.getAppComponent());
        LaunchRouter router = component.getLaunchRouter();
        router.attach(this);
        presenter = component.getLaunchPresenter();
        presenter.attach(router);
    }

    @Override
    protected void onDestroy() {
       presenter.detach();
       if (isFinishing())
            DaggerLaunchComponent.clearComponent();
       super.onDestroy();
    }
}
