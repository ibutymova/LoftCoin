package com.example.butymovaloftcoin.data.db.room;

import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.db.model.Transaction;
import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.db.model.Wallet;
import com.example.butymovaloftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class DatabaseImplRoom implements Database {

    private AppDatabase database;

    public DatabaseImplRoom(AppDatabase database) {
        this.database = database;
    }


    @Override
    public void saveCoins(List<CoinEntity> coinEntities) {
         database.coinDao().saveCoins(coinEntities);
    }

    @Override
    public Observable<List<CoinEntity>> getCoins() {
        return database.coinDao().getCoins();
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return database.coinDao().getCoin(symbol);
    }

    @Override
    public Observable<List<WalletModel>> getWallets() {
        return database.walletDao().getWallets();
    }

    @Override
    public void saveWallet(Wallet wallet) {
        database.walletDao().saveWallet(wallet);
    }

    @Override
    public Single<List<TransactionModel>> getTransactions(String walletId) {
        return database.walletDao().getTransactions(walletId);
    }

    @Override
    public void saveTransaction(List<Transaction> transactionList) {
        database.walletDao().saveTransaction(transactionList);
    }

    @Override
    public void deleteWallet(Wallet wallet) {
        database.walletDao().deleteWallet(wallet);
    }

    @Override
    public void deleteTransactions(String walletId) {
        database.walletDao().deleteTransactions(walletId);
    }

    @Override
    public Integer countCoins() {
        return database.coinDao().countCoins();
    }
}
