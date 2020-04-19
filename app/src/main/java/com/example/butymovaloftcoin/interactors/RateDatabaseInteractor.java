package com.example.butymovaloftcoin.interactors;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;

import java.util.List;

import io.reactivex.Observable;

public interface RateDatabaseInteractor {

    void loadRateDatabase();

    void getRateForShare();

    void isRateDatabaseEmpty();

    void detach();

    Observable<List<CoinEntity>> loadRateResultTrueEvent();

    Observable<Integer> loadRateResultFalseEvent();

    Observable<List<CoinEntity>> getRateShareTrueEvent();

    Observable<Integer> getRateShareFalseEvent();

    Observable<Integer> isRateDatabaseEmptyTrueEvent();

    Observable<Object> isRateDatabaseEmptyFalseEvent();
}
