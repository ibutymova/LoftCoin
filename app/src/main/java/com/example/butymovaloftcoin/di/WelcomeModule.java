package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.screens.welcome.WelcomePresenter;
import com.example.butymovaloftcoin.screens.welcome.WelcomePresenterImpl;
import com.example.butymovaloftcoin.screens.welcome.WelcomeRouter;
import com.example.butymovaloftcoin.screens.welcome.WelcomeRouterImpl;
import com.example.butymovaloftcoin.screens.welcome.WelcomeView;
import com.example.butymovaloftcoin.screens.welcome.WelcomeViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface WelcomeModule {

    @Binds
    WelcomePresenter bindWelcomePresenter(WelcomePresenterImpl presenter);

    @Binds
    WelcomeRouter bindWelcomeRoter(WelcomeRouterImpl router);

    @Binds
    WelcomeView bindWelcomeView(WelcomeViewImpl view);

}
