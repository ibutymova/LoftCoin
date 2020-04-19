package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.screens.start.StartPresenter;
import com.example.butymovaloftcoin.screens.start.StartPresenterImpl;
import com.example.butymovaloftcoin.screens.start.StartRouter;
import com.example.butymovaloftcoin.screens.start.StartRouterImpl;
import com.example.butymovaloftcoin.screens.start.StartView;
import com.example.butymovaloftcoin.screens.start.StartViewImpl;

import dagger.Binds;
import dagger.Module;


@Module
public interface StartModule {

    @Binds
    StartPresenter bindStartPresenter(StartPresenterImpl presenter);

    @Binds
    StartRouter bindStartRouter(StartRouterImpl router);

    @Binds
    StartView bindStartView(StartViewImpl view);
}
