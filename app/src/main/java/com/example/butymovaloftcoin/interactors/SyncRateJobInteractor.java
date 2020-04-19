package com.example.butymovaloftcoin.interactors;

import com.example.butymovaloftcoin.data.extra.CompareRatesResult;

import io.reactivex.Observable;

public interface SyncRateJobInteractor {

    void compareRates(String symbol);

    void detach();

    Observable<CompareRatesResult> compareRatesResultTrueEvent();

    Observable<Integer> compareRatesResultFalseEvent();
}
