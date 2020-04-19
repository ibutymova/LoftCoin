package com.example.butymovaloftcoin.screens.main.converter;

import android.content.Intent;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;

import io.reactivex.Observable;

public interface ConverterRouter {

    void attach(Fragment fragment);
    void showCurrencyBottomSheet(Boolean flag);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void showToast(@StringRes Integer errorId);
    void detach();

    Observable<CoinEntity> onSourceCurrencySelectedEvent();

    Observable<CoinEntity> onDestinationCurrencySelectedEvent();
}
