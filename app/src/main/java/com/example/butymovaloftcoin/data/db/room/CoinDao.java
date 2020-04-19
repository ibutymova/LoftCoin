package com.example.butymovaloftcoin.data.db.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import java.util.List;
import io.reactivex.Observable;

@Dao
public abstract class CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertCoins(List<CoinEntity> coinEntities);

    @Query("DELETE FROM Coin")
    abstract void deleteCoins();

    @Transaction
    void saveCoins(List<CoinEntity> coinEntities){
        deleteCoins();
        insertCoins(coinEntities);
    }

    @Query("SELECT * FROM  Coin ORDER BY symbol")
    abstract Observable<List<CoinEntity>> getCoins();

    @Query("SELECT * FROM Coin WHERE symbol = :symbol")
    abstract CoinEntity getCoin(String symbol);

    @Query("SELECT COUNT(*) FROM Coin")
    abstract Integer countCoins();
}
