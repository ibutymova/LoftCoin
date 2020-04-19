package com.example.butymovaloftcoin.interactors;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class RateDatabaseInteractorImpl implements RateDatabaseInteractor {

    private BehaviorSubject<List<CoinEntity>> loadRateResultTrueEvent = BehaviorSubject.create();
    private PublishSubject<Integer> loadRateResultFalseEvent = PublishSubject.create();
    private PublishSubject<List<CoinEntity>> getRateShareTrueEvent = PublishSubject.create();
    private PublishSubject<Integer> getRateShareFalseEvent = PublishSubject.create();
    private PublishSubject<Integer> isRateDatabaseEmptyTrueEvent = PublishSubject.create();
    private PublishSubject<Object> isRateDatabaseEmptyFalseEvent = PublishSubject.create();

    private Database database;
    private CompositeDisposable compositeDisposable;

    @Inject
    public RateDatabaseInteractorImpl(Database database,
                                      CompositeDisposable compositeDisposable) {
        this.database = database;
        this.compositeDisposable = compositeDisposable;
    }

     public void loadRateDatabase(){
        compositeDisposable.add( database.getCoins()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntities -> loadRateResultTrueEvent.onNext(coinEntities),
                        throwable -> loadRateResultFalseEvent.onNext(R.string.error_load_rate_database))
        );
    }

    @Override
    public void getRateForShare() {
        if (loadRateResultTrueEvent.getValue()!=null)
            getRateShareTrueEvent.onNext(loadRateResultTrueEvent.getValue());
    }

    @Override
    public void isRateDatabaseEmpty() {
        compositeDisposable.add(Observable.fromCallable(() -> (database.countCoins() > 0 ? new Object() : null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obj -> isRateDatabaseEmptyFalseEvent.onNext(new Object()),
                        throwable -> isRateDatabaseEmptyTrueEvent.onNext(R.string.error_load_rate_api))
        );
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
    }

    @Override
    public Observable<List<CoinEntity>> loadRateResultTrueEvent() {
        return loadRateResultTrueEvent;
    }

    @Override
    public Observable<Integer> loadRateResultFalseEvent() {
        return loadRateResultFalseEvent;
    }

    @Override
    public Observable<List<CoinEntity>> getRateShareTrueEvent() {
        return getRateShareTrueEvent;
    }

    @Override
    public Observable<Integer> getRateShareFalseEvent() {
        return getRateShareFalseEvent;
    }

    @Override
    public Observable<Integer> isRateDatabaseEmptyTrueEvent() {
        return isRateDatabaseEmptyTrueEvent;
    }

    @Override
    public Observable<Object> isRateDatabaseEmptyFalseEvent() {
        return isRateDatabaseEmptyFalseEvent;
    }
}
