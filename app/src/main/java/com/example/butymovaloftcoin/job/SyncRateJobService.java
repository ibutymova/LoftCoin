package com.example.butymovaloftcoin.job;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.di.DaggerJobComponent;
import com.example.butymovaloftcoin.di.JobComponent;

public class SyncRateJobService extends JobService {

    public static final String CHANNEL_ID = "channel_change_rate_id";
    public static final int NOTIFY_ID = 99;
    public static final String EXTRA_SYMBOL = "symbol";
    public static final int REQUEST_CODE = 9999;

    private SyncRateJobPresenter presenter;

    public SyncRateJobService() {
        JobComponent component = DaggerJobComponent.createComponent(App.getAppComponent());
        SyncRateJobRouter router = component.getSyncRateJobRouter();
        router.attach(this);
        presenter = component.getSyncRateJobPresenter();
        presenter.attach(router);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        presenter.onStartJob(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void onDestroy() {
        presenter.detach();
        DaggerJobComponent.clearComponent();
        super.onDestroy();
    }
}
