package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.screens.main.rate.RatePresenter;
import com.example.butymovaloftcoin.screens.main.rate.RatePresenterImpl;
import com.example.butymovaloftcoin.screens.main.rate.RateRouter;
import com.example.butymovaloftcoin.screens.main.rate.RateRouterImpl;
import com.example.butymovaloftcoin.screens.main.rate.RateView;
import com.example.butymovaloftcoin.screens.main.rate.RateViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface RateModule {

    @Binds
    RatePresenter bindRatePresenter(RatePresenterImpl presenter);

    @Binds
    RateRouter bindRateRouter(RateRouterImpl router);

    @Binds
    RateView bindRateView(RateViewImpl view);
}
