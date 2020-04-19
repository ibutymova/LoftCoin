package com.example.butymovaloftcoin.interactors;

import com.example.butymovaloftcoin.data.model.Fiat;

import io.reactivex.Observable;

public interface RateApiInteractor {

    void loadRateApi(Fiat currency);
    void detach();

    Observable<Fiat> loadRateResultTrueEvent();
    Observable<Integer> loadRateResultFalseEvent();
}
