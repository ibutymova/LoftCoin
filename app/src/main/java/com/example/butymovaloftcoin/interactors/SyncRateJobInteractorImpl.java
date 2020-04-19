package com.example.butymovaloftcoin.interactors;

import androidx.annotation.ColorRes;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.api.Api;
import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.db.model.CoinEntityMapper;
import com.example.butymovaloftcoin.data.extra.CompareRatesResult;
import com.example.butymovaloftcoin.data.model.Fiat;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.utils.CurrencyFormatter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static java.lang.StrictMath.abs;

public class SyncRateJobInteractorImpl implements SyncRateJobInteractor {

    private PublishSubject<CompareRatesResult> compareRatesResultTrueEvent = PublishSubject.create();
    private PublishSubject<Integer> compareRatesResultFalseEvent = PublishSubject.create();

    private Api api;
    private CoinEntityMapper mapper;
    private Database database;
    private Prefs prefs;
    private CurrencyFormatter currencyFormatter;
    private CompositeDisposable compositeDisposable;

    @Inject
    public SyncRateJobInteractorImpl(Api api,
                                     CoinEntityMapper mapper,
                                     Database database,
                                     Prefs prefs,
                                     CurrencyFormatter currencyFormatter,
                                     CompositeDisposable compositeDisposable) {
        this.api = api;
        this.mapper = mapper;
        this.database = database;
        this.prefs = prefs;
        this.currencyFormatter = currencyFormatter;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void compareRates(String symbol) {
        compositeDisposable.add(api.listings_latest(prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> mapper.mapCoins(rateResponse.data))
                .map(coinEntities -> compareRates(coinEntities, symbol))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(compareRatesResult -> {
                    if (!compareRatesResult.getPriceDiffStr().equals("0"))
                        compareRatesResultTrueEvent.onNext(compareRatesResult);
                }, throwable -> compareRatesResultFalseEvent.onNext(R.string.error_create_notification)));
     }

    private CompareRatesResult compareRates(List<CoinEntity> newCoinEntities, String symbol) {
        Fiat fiat = prefs.getFiatCurrency();
        CompareRatesResult result;

        CoinEntity newCoinEntity = findCoinEntity(newCoinEntities, symbol);
        if (newCoinEntity == null)
            return null;

        CoinEntity oldCoinEntity = database.getCoin(symbol);
        if (oldCoinEntity == null)
            return null;

        double newPrice = newCoinEntity.getQuote(fiat).price;
        double oldPrice = oldCoinEntity.getQuote(fiat).price;

        if (abs(oldPrice - newPrice) >= 0.0001) {
            double priceDiff = newPrice - oldPrice;

            String priceDiffStr;
            @ColorRes int color;

            if (newPrice > oldPrice) {
                priceDiffStr = "+" + currencyFormatter.format(abs(priceDiff), false) + " " + fiat.symbol;
                color = R.color.percent_change_up;
            }
            else {
                priceDiffStr = "-" + currencyFormatter.format(abs(priceDiff), false) + " " + fiat.symbol;
                color = R.color.percent_change_down;
            }
            result = new CompareRatesResult(newCoinEntity, priceDiffStr, color);
        }
        else
            result = new CompareRatesResult(newCoinEntity, "0", R.color.colorPrimary);

        database.saveCoins(newCoinEntities);
        return result;
    }

    private CoinEntity findCoinEntity(List<CoinEntity> coinEntities, String symbol){
        for(CoinEntity coinEntity: coinEntities){
            if (coinEntity.symbol.equals(symbol)){
                return coinEntity;
            }
        }
        return null;
    }

    @Override
    public Observable<CompareRatesResult> compareRatesResultTrueEvent() {
        return compareRatesResultTrueEvent;
    }

    @Override
    public Observable<Integer> compareRatesResultFalseEvent() {
        return compareRatesResultFalseEvent;
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
    }
}
