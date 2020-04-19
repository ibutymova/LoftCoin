package com.example.butymovaloftcoin.data.db.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.db.model.Transaction;
import com.example.butymovaloftcoin.data.db.model.Wallet;

@Database(entities = {CoinEntity.class, Wallet.class, Transaction.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CoinDao coinDao();
    public abstract WalletDao walletDao();

}
