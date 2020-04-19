package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.screens.launch.LaunchPresenter;
import com.example.butymovaloftcoin.screens.launch.LaunchPresenterImpl;
import com.example.butymovaloftcoin.screens.launch.LaunchRouter;
import com.example.butymovaloftcoin.screens.launch.LaunchRouterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface LaunchModule {

    @Binds
    LaunchPresenter bindLaunchPresenter(LaunchPresenterImpl presenter);

    @Binds
    LaunchRouter bindLaunchRouter(LaunchRouterImpl router);
}
