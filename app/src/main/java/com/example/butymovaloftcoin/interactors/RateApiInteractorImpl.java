package com.example.butymovaloftcoin.interactors;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.api.Api;
import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.model.CoinEntityMapper;
import com.example.butymovaloftcoin.data.model.Fiat;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RateApiInteractorImpl implements RateApiInteractor {

    private PublishSubject<Fiat> loadRateResultTrueEvent = PublishSubject.create();
    private PublishSubject<Integer> loadRateResultFalseEvent = PublishSubject.create();

    private Api api;
    private CoinEntityMapper mapper;
    private Database database;
    private Prefs prefs;
    private CompositeDisposable compositeDisposable;

    @Inject
    public RateApiInteractorImpl(Api api,
                                 CoinEntityMapper mapper,
                                 Database database,
                                 Prefs prefs,
                                 CompositeDisposable compositeDisposable) {
        this.api = api;
        this.mapper = mapper;
        this.database = database;
        this.prefs = prefs;
        this.compositeDisposable = compositeDisposable;
    }


    @Override
    public void loadRateApi(Fiat currency) {
         compositeDisposable.add(api.listings_latest(currency.name())
                .map(rateResponse -> {
                    database.saveCoins(mapper.mapCoins(rateResponse.data));
                    return new Object();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    prefs.setFiatCurrency(currency);
                    loadRateResultTrueEvent.onNext(currency);
                }, throwable -> loadRateResultFalseEvent.onNext(R.string.error_load_rate_api)));
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
    }

    @Override
    public Observable<Fiat> loadRateResultTrueEvent() {
        return loadRateResultTrueEvent;
    }

    @Override
    public Observable<Integer> loadRateResultFalseEvent() {
        return loadRateResultFalseEvent;
    }

}
