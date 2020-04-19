package com.example.butymovaloftcoin.interactors;

import android.os.Bundle;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.db.model.WalletModel;

import java.util.List;
import io.reactivex.Observable;

public interface WalletsInteractor {

    void init(Bundle bundle);
    void getTransactions(Integer walletPosition);
    void removeWallet(Integer walletPosition);
    void getWallets();
    void createWallet(CoinEntity coinEntity);
    void saveInstanceState(Bundle outState, int walletPosition);
    void detach();

    Observable<List<WalletModel>> moveLastWalletEvent();
    Observable<Integer> removedWalletEvent();
    Observable<Object> setCurrentWalletEvent();
    Observable<Integer> setCurrentWalletPosEvent();
    Observable<Boolean> walletsVisibleEvent();
    Observable<Boolean> newWalletVisibleEvent();
    Observable<List<WalletModel>> walletsItemsEvent();
    Observable<List<TransactionModel>> transactionItemsEvent();
    Observable<Boolean> menuItemRemoveVisibleEvent();

    Observable<Integer> errorLoadingWalletsEvent();
    Observable<Integer> errorLoadingTransactionsEvent();
    Observable<Integer> errorRemovingWalletEvent();
    Observable<Integer> errorSavingWalletEvent();


}
