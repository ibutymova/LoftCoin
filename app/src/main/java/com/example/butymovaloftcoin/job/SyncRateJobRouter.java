package com.example.butymovaloftcoin.job;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.example.butymovaloftcoin.data.extra.CompareRatesResult;

public interface SyncRateJobRouter {

    void attach(JobService service);

    void jobFinished(JobParameters params, boolean wantsReschedule);

    void showRateChangedNotification(CompareRatesResult compareRatesResult);

    void showRateChangedNotificationFailed(Integer errorId);

    void detach();
}
