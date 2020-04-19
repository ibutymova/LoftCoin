package com.example.butymovaloftcoin.job;

import android.app.job.JobParameters;

public interface SyncRateJobPresenter {

    void attach(SyncRateJobRouter router);

    void detach();

    void onStartJob(JobParameters params);
}
