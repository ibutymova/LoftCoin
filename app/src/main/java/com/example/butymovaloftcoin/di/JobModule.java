package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ServiceScope;
import com.example.butymovaloftcoin.job.SyncRateJobPresenter;
import com.example.butymovaloftcoin.job.SyncRateJobPresenterImpl;
import com.example.butymovaloftcoin.job.SyncRateJobRouter;
import com.example.butymovaloftcoin.job.SyncRateJobRouterImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface JobModule {

    @Binds
    SyncRateJobPresenter bindSyncRateJobPresenter(SyncRateJobPresenterImpl presenter);

    @Binds
    SyncRateJobRouter bindSyncRateJobRouter(SyncRateJobRouterImpl router);
}
