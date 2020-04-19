package com.example.butymovaloftcoin.data.prefs;

import com.example.butymovaloftcoin.data.model.Fiat;

public interface Prefs {

    boolean isFirstLaunch();

    void setFirstLaunch(boolean firstLaunch);

    Fiat getFiatCurrency();

    void setFiatCurrency(Fiat currency);

    String getRateNotify();

    void setRateNotify(String symbol);
}
