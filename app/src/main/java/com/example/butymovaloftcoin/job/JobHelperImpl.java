package com.example.butymovaloftcoin.job;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import java.util.concurrent.TimeUnit;

import static com.example.butymovaloftcoin.job.SyncRateJobService.EXTRA_SYMBOL;

public class JobHelperImpl implements JobHelper {

    private static final int SYNC_RATE_JOB_ID = 99;

    private Context context;
    private JobHelper.Listener listener;

    public JobHelperImpl(Context context, JobHelper.Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void startSyncRateJob(String symbol) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler == null)
            return;

        ComponentName componentName = new ComponentName(context, SyncRateJobService.class);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(EXTRA_SYMBOL, symbol);

        JobInfo jobInfo = new JobInfo.Builder(SYNC_RATE_JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setPeriodic(TimeUnit.MINUTES.toMillis(10))
                .setExtras(bundle)
                .setPersisted(true)
                .build();

        int result = jobScheduler.schedule(jobInfo);

        if (listener!=null && result == JobScheduler.RESULT_SUCCESS)
            listener.syncRateJobStarted(symbol);
    }

    @Override
    public void stopSyncRateJob() {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        if (jobScheduler == null)
            return;

        jobScheduler.cancel(SYNC_RATE_JOB_ID);

        if (listener!=null)
            listener.syncRateJobStopped();
    }
}




