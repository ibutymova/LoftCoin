package com.example.butymovaloftcoin.data.db.model;

import com.example.butymovaloftcoin.data.api.model.Coin;
import com.example.butymovaloftcoin.data.api.model.Quote;
import com.example.butymovaloftcoin.data.model.Fiat;

import java.util.ArrayList;
import java.util.List;

public class CoinEntityMapper {
    public List<CoinEntity> mapCoins(List<Coin> coins){
        List<CoinEntity> coinEntities = new ArrayList<>();
        for (Coin coin: coins){
            coinEntities.add(mapCoin(coin));
        }
        return coinEntities;
    }

    private CoinEntity mapCoin(Coin coin){
        CoinEntity coinEntity = new CoinEntity();
        coinEntity.id= coin.id;
        coinEntity.symbol = coin.symbol;
        coinEntity.name = coin.name;
        coinEntity.updated = coin.updated;
        coinEntity.added = coin.added;

        coinEntity.usd = mapQuota(coin.quote.get(Fiat.USD.name()));
        coinEntity.rub = mapQuota(coin.quote.get(Fiat.RUB.name()));
        coinEntity.eur = mapQuota(coin.quote.get(Fiat.EUR.name()));
        return coinEntity;
    }

    private QuoteEntity mapQuota(Quote quote){
        if (quote == null)
            return null;

        QuoteEntity quoteEntity = new QuoteEntity();
        quoteEntity.price = quote.price;
        quoteEntity.percentChange1h = quote.percentChange1h;
        quoteEntity.percentChange7d = quote.percentChange7d;
        quoteEntity.percentChange24h = quote.percentChange24h;
        return  quoteEntity;
    }
}
