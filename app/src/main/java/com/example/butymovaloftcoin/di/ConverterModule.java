package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.screens.main.converter.ConverterPresenter;
import com.example.butymovaloftcoin.screens.main.converter.ConverterPresenterImpl;
import com.example.butymovaloftcoin.screens.main.converter.ConverterRouter;
import com.example.butymovaloftcoin.screens.main.converter.ConverterRouterImpl;
import com.example.butymovaloftcoin.screens.main.converter.ConverterView;
import com.example.butymovaloftcoin.screens.main.converter.ConverterViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface ConverterModule {

    @Binds
    ConverterPresenter bindConverterPresenter(ConverterPresenterImpl presenter);

    @Binds
    ConverterRouter  bindConverterRouter(ConverterRouterImpl router);

    @Binds
    ConverterView bindConverterView(ConverterViewImpl view);
}
