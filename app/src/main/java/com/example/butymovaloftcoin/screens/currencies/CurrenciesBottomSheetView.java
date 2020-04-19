package com.example.butymovaloftcoin.screens.currencies;

import android.view.View;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;

import java.util.List;

import io.reactivex.Observable;

public interface CurrenciesBottomSheetView {

    void attach(View view, CurrenciesBottomSheetView.Listener listener);

    void initView();

    void setAdapterList(List<CoinEntity> coinEntitis);

    void detach();

    Observable<Object> initEvent();

    interface Listener{
        void onCurrencySelected(CoinEntity coinEntity);
    }
}
