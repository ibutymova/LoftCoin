package com.example.butymovaloftcoin.interactors;

import android.os.Bundle;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import io.reactivex.Observable;

public interface ConverterInteractor {

    void restoreInstanceState(Bundle savedInstanceState);
    void convert(CoinEntity sourceCurrency, CoinEntity destinationCurrency, String sourceAmount);
    void saveInstanceState(Bundle outState, CoinEntity sourceCurrency, CoinEntity destinationCurrency);
    void setSourceCurrency(String symbol);
    void setDestinationCurrency(String symbol);
    void detach();

    Observable<CoinEntity> sourceCurrencySelectedEvent();
    Observable<CoinEntity> destinationCurrencySelectedEvent();
    Observable<String> destinationAmountEvent();
    Observable<String> sourceAmountEvent();
    Observable<Integer> errorSourceCurrencySelectedEvent();
    Observable<Integer> errorDestinationCurrencySelectedEvent();
}
