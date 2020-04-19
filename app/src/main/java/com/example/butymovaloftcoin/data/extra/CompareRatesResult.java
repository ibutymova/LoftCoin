package com.example.butymovaloftcoin.data.extra;

import androidx.annotation.IdRes;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;

public class CompareRatesResult {

    private CoinEntity coinEntity;
    private String priceDiffStr;
    private @IdRes int color;


    public CompareRatesResult(CoinEntity coinEntity, String priceDiffStr, int color) {
        this.coinEntity = coinEntity;
        this.priceDiffStr = priceDiffStr;
        this.color = color;
    }

    public CoinEntity getCoinEntity() {
        return coinEntity;
    }

    public void setCoinEntity(CoinEntity coinEntity) {
        this.coinEntity = coinEntity;
    }

    public String getPriceDiffStr() {
        return priceDiffStr;
    }

    public void setPriceDiffStr(String priceDiffStr) {
        this.priceDiffStr = priceDiffStr;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
