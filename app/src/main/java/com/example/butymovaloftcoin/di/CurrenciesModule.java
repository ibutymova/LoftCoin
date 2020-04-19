package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheetPresenter;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheetPresenterImpl;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheetView;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheetViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface CurrenciesModule {

    @Binds
    CurrenciesBottomSheetPresenter bindCurrenciesBottomSheetPresenter(CurrenciesBottomSheetPresenterImpl presenter);

    @Binds
    CurrenciesBottomSheetView bindCurrenciesBottomSheetView(CurrenciesBottomSheetViewImpl view);
}
