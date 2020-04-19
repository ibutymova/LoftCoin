package com.example.butymovaloftcoin.screens.main.wallets;

import android.content.Intent;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import io.reactivex.Observable;

public interface WalletsRouter {

    void attach(Fragment fragment);

    void showCurrencyBottomSheet();

    void showWalletRemoveDialog(Integer position);

    void showToast(@StringRes Integer resId);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void detach();

    Observable<CoinEntity> onCurrencySelectedEvent();

    Observable<Integer> onConfirmationRemoveWalletEvent();

}
