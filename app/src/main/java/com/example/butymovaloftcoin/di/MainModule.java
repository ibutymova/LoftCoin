package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.screens.main.MainPresenter;
import com.example.butymovaloftcoin.screens.main.MainPresenterImpl;
import com.example.butymovaloftcoin.screens.main.MainRouter;
import com.example.butymovaloftcoin.screens.main.MainRouterImpl;
import com.example.butymovaloftcoin.screens.main.MainView;
import com.example.butymovaloftcoin.screens.main.MainViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface MainModule {

    @Binds
    MainPresenter bindMainPresenter(MainPresenterImpl presenter);

    @Binds
    MainRouter bindMainRouter(MainRouterImpl router);

    @Binds
    MainView bindMainView(MainViewImpl view);
}
