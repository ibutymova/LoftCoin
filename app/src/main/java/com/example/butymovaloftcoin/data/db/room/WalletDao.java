package com.example.butymovaloftcoin.data.db.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.butymovaloftcoin.data.db.model.Transaction;
import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.db.model.Wallet;
import com.example.butymovaloftcoin.data.db.model.WalletModel;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWallet(Wallet wallet);

    @Query("select w.*, c.* from Wallet w, Coin c where w.currencyId = c.id")
    Observable<List<WalletModel>> getWallets();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveTransaction(List<Transaction> transactionList);

    @Delete
    void deleteWallet(Wallet wallet);

    @Query("DELETE FROM `transaction` where walletId = :walletId")
    void deleteTransactions(String walletId);

    @Query("select t.*, c.* from 'transaction' t, coin c where t.currencyId = c.id and t.walletId = :walletId order by t.date desc")
    Single<List<TransactionModel>> getTransactions(String walletId);
}
