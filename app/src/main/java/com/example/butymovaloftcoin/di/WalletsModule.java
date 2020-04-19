package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.screens.main.wallets.WalletsPresenter;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsPresenterImpl;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsRouter;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsRouterImpl;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsView;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface WalletsModule {

    @Binds
    WalletsPresenter bindWalletsPresenter(WalletsPresenterImpl presenter);

    @Binds
    WalletsRouter  bindWalletsRouter(WalletsRouterImpl router);

    @Binds
    WalletsView bindWalletsView(WalletsViewImpl view);
}