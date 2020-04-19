package com.example.butymovaloftcoin.job;

public interface JobHelper {

    void startSyncRateJob(String symbol);

    void stopSyncRateJob();

    interface Listener{

        void syncRateJobStarted(String symbol);

        void syncRateJobStopped();
    }
}
