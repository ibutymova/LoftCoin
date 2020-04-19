package com.example.butymovaloftcoin.job;

import android.app.job.JobParameters;

import com.example.butymovaloftcoin.di.scopes.ServiceScope;
import com.example.butymovaloftcoin.interactors.SyncRateJobInteractor;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

import static com.example.butymovaloftcoin.job.SyncRateJobService.EXTRA_SYMBOL;

@ServiceScope
public class SyncRateJobPresenterImpl implements SyncRateJobPresenter {

    private SyncRateJobInteractor syncRateJobInteractor;
    private CompositeDisposable compositeDisposable;

    private JobParameters params;
    private SyncRateJobRouter router;

    @Inject
    public SyncRateJobPresenterImpl(SyncRateJobInteractor syncRateJobInteractor,
                                    CompositeDisposable compositeDisposable) {
        this.syncRateJobInteractor = syncRateJobInteractor;
        this.compositeDisposable = compositeDisposable;

        compositeDisposable.add(syncRateJobInteractor.compareRatesResultTrueEvent().subscribe(compareRatesResult -> {
            router.showRateChangedNotification(compareRatesResult);
            router.jobFinished(params, false);
        }));

        compositeDisposable.add(syncRateJobInteractor.compareRatesResultFalseEvent().subscribe(errorId -> {
            router.showRateChangedNotificationFailed(errorId);
            router.jobFinished(params, false);
        }));
    }

    @Override
    public void onStartJob(JobParameters params) {
        this.params = params;
        syncRateJobInteractor.compareRates(params.getExtras().getString(EXTRA_SYMBOL));
    }

    @Override
    public void attach(SyncRateJobRouter router) {
        this.router = router;
    }

    @Override
    public void detach() {
        syncRateJobInteractor.detach();
        compositeDisposable.clear();
        router.detach();
        router = null;
    }
}
