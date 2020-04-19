package com.example.butymovaloftcoin.interactors;

import android.os.Bundle;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.model.Fiat;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.utils.CurrencyFormatter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class ConverterInteractorImpl implements ConverterInteractor {

    private static final String SOURCE_CURRENCY_KEY = "source_currency";
    private static final String DESTINATION_CURRENCY_KEY = "destination_currency";

    private final String  defaultSourceCurrencySymbol = "BTC";
    private final String defaultDestinationCurrencySymbol = "ETH";

    private String sourceCurrencySymbol;
    private String destinationCurrencySymbol;

    private PublishSubject<CoinEntity> sourceCurrencySelectedEvent = PublishSubject.create();
    private PublishSubject<CoinEntity> destinationCurrencySelectedEvent = PublishSubject.create();

    private PublishSubject<String> destinationAmountEvent = PublishSubject.create();
    private PublishSubject<String> sourceAmountEvent = PublishSubject.create();

    private PublishSubject<Integer> errorSourceCurrencySelectedEvent = PublishSubject.create();
    private PublishSubject<Integer> errorDestinationCurrencySelectedEvent = PublishSubject.create();

    private Database database;
    private CompositeDisposable compositeDisposable;
    private Prefs prefs;
    private CurrencyFormatter currencyFormatter ;

    @Inject
    ConverterInteractorImpl(Database database,
                            CurrencyFormatter currencyFormatter,
                            Prefs prefs,
                            CompositeDisposable compositeDisposable) {
        this.database = database;
        this.currencyFormatter = currencyFormatter;
        this.prefs = prefs;
        this.compositeDisposable = compositeDisposable;
    }

    private void loadCoins(){
        compositeDisposable.add(database.getCoins()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntities -> {
                    CoinEntity sourceCoinEntity = findCoinEntity(coinEntities, sourceCurrencySymbol);
                    if (sourceCoinEntity!=null)
                        sourceCurrencySelectedEvent.onNext(sourceCoinEntity);

                    CoinEntity destinationCoinEntity = findCoinEntity(coinEntities, destinationCurrencySymbol);
                    if (destinationCoinEntity!=null)
                        destinationCurrencySelectedEvent.onNext(destinationCoinEntity);
                }, throwable -> errorDestinationCurrencySelectedEvent.onNext(R.string.error_currency_selected)));
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
    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.size()!=0 && savedInstanceState.containsKey(SOURCE_CURRENCY_KEY) && savedInstanceState.getString(SOURCE_CURRENCY_KEY)!=null)
            sourceCurrencySymbol = savedInstanceState.getString(SOURCE_CURRENCY_KEY);
        else
            sourceCurrencySymbol = defaultSourceCurrencySymbol;

        if (savedInstanceState.size()!=0 && savedInstanceState.containsKey(DESTINATION_CURRENCY_KEY) && savedInstanceState.getString(DESTINATION_CURRENCY_KEY)!=null)
            destinationCurrencySymbol = savedInstanceState.getString(DESTINATION_CURRENCY_KEY);
        else
            destinationCurrencySymbol = defaultDestinationCurrencySymbol;

        loadCoins();

        if (savedInstanceState.size()==0)
            sourceAmountEvent.onNext("1");
    }

    @Override
    public void convert(CoinEntity sourceCurrency, CoinEntity destinationCurrency, String sourceAmount) {
        if (sourceAmount == null || sourceAmount.trim().equals("")) {
            destinationAmountEvent.onNext("");
            return;
        }

        if (sourceCurrency == null || destinationCurrency == null)
            return;

        Fiat fiat = prefs.getFiatCurrency();
        if (sourceCurrency.getQuote(fiat)==null || destinationCurrency.getQuote(fiat)==null)
            return;

        double sourceValue = Double.parseDouble(sourceAmount);
        double destinationValue = sourceValue * sourceCurrency.getQuote(fiat).price/destinationCurrency.getQuote(fiat).price;
        String destinationAmountValue = currencyFormatter.formatForConverter(destinationValue);
        destinationAmountEvent.onNext(destinationAmountValue);
    }

    @Override
    public void saveInstanceState(Bundle outState, CoinEntity sourceCurrency, CoinEntity destinationCurrency) {
        outState.putString(SOURCE_CURRENCY_KEY, sourceCurrency.symbol);
        outState.putString(DESTINATION_CURRENCY_KEY, destinationCurrency.symbol);
    }

    @Override
    public void setSourceCurrency(String symbol) {
        sourceCurrencySymbol = symbol;
    }

    @Override
    public void setDestinationCurrency(String symbol) {
        destinationCurrencySymbol = symbol;
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
    }

    @Override
    public Observable<CoinEntity> sourceCurrencySelectedEvent() {
        return sourceCurrencySelectedEvent;
    }

    @Override
    public Observable<CoinEntity> destinationCurrencySelectedEvent() {
        return destinationCurrencySelectedEvent;
    }

    @Override
    public Observable<Integer> errorSourceCurrencySelectedEvent() {
        return errorSourceCurrencySelectedEvent;
    }

    @Override
    public Observable<Integer> errorDestinationCurrencySelectedEvent() {
        return errorDestinationCurrencySelectedEvent;
    }

    @Override
    public Observable<String> destinationAmountEvent() {
        return destinationAmountEvent;
    }

    @Override
    public Observable<String> sourceAmountEvent() {
        return sourceAmountEvent;
    }
}
