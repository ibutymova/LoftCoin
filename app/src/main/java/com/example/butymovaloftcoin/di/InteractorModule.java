package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.interactors.ConverterInteractor;
import com.example.butymovaloftcoin.interactors.ConverterInteractorImpl;
import com.example.butymovaloftcoin.interactors.RateApiInteractor;
import com.example.butymovaloftcoin.interactors.RateApiInteractorImpl;
import com.example.butymovaloftcoin.interactors.RateDatabaseInteractor;
import com.example.butymovaloftcoin.interactors.RateDatabaseInteractorImpl;
import com.example.butymovaloftcoin.interactors.SyncRateJobInteractor;
import com.example.butymovaloftcoin.interactors.SyncRateJobInteractorImpl;
import com.example.butymovaloftcoin.interactors.WalletsInteractor;
import com.example.butymovaloftcoin.interactors.WalletsInteractorImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface InteractorModule {

    @Binds
    RateApiInteractor bindRateIteractor(RateApiInteractorImpl interactor);

    @Binds
    RateDatabaseInteractor bindRateInnerIteractor(RateDatabaseInteractorImpl interactor);

    @Binds
    ConverterInteractor bindConverterInteractor(ConverterInteractorImpl interactor);

    @Binds
    WalletsInteractor bindWalletsInteractor(WalletsInteractorImpl interactor);

    @Binds
    SyncRateJobInteractor bindSyncRateJobInteractor(SyncRateJobInteractorImpl interactor);
}
