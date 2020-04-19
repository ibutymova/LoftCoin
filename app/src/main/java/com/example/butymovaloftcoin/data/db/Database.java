package com.example.butymovaloftcoin.data.db;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.db.model.Transaction;
import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.db.model.Wallet;
import com.example.butymovaloftcoin.data.db.model.WalletModel;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface Database {

    void saveCoins(List<CoinEntity> coinEntities);

    Observable<List<CoinEntity>> getCoins();

    CoinEntity getCoin(String symbol);

    Observable<List<WalletModel>> getWallets();

    void saveWallet(Wallet wallet);

    Single<List<TransactionModel>> getTransactions(String walletId);

    void saveTransaction(List<Transaction> transactionList);

    void deleteWallet(Wallet wallet);

    void deleteTransactions(String walletId);

    Integer countCoins();
}
