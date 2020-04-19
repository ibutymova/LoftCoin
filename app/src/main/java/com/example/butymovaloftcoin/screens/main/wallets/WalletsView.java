package com.example.butymovaloftcoin.screens.main.wallets;

import android.os.Bundle;
import android.view.View;

import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.db.model.WalletModel;

import java.util.List;
import io.reactivex.Observable;

public interface WalletsView {

    void attach(View view);
    void initView(Bundle savedInstanceState);
    void setPageAdapterList(List<WalletModel> walletModels);
    void setAdapterList(List<TransactionModel> transactionModels);
    void setWalletsVisible(Boolean isVisible);
    void setNewWalletVisible(Boolean isVisible);
    void moveLastWallet(List<WalletModel> walletModels);
    void setMenuItemRemoveVisible(Boolean isVisible);
    void setCurrentWallet();
    void setCurrentWallet(Integer walletPosition);
    void detach();

    Observable<Object> onNewWalletClickEvent();
    Observable<Integer> onRemoveWalletClickEvent();
    Observable<Integer> onWalletChangePositionEvent();
    Observable<Bundle> initEvent();
}
