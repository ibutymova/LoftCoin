package com.example.butymovaloftcoin.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.butymovaloftcoin.data.model.Fiat;

public class PrefsImpl implements Prefs {

    private static String PREFS_NAME = "prefs";
    private static String KEY_FIRST_LAUNCH = "first_launch";
    private static String KEY_FIAT_CURRENCY = "fiat_currency";
    private static String KEY_RATE_NOTIFY = "rate_notify";

    private Context context;

    public PrefsImpl(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs(){
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean isFirstLaunch() {
        return getPrefs().getBoolean(KEY_FIRST_LAUNCH, true);
    }

    @Override
    public void setFirstLaunch(boolean firstLaunch) {
        getPrefs().edit().putBoolean(KEY_FIRST_LAUNCH, firstLaunch).apply();
    }

    @Override
    public Fiat getFiatCurrency() {
        return Fiat.valueOf(getPrefs().getString(KEY_FIAT_CURRENCY, Fiat.USD.name()));
    }

    @Override
    public void setFiatCurrency(Fiat currency) {
        getPrefs().edit().putString(KEY_FIAT_CURRENCY, currency.name()).apply();

    }

    @Override
    public String getRateNotify() {
        return getPrefs().getString(KEY_RATE_NOTIFY, null);
    }

    @Override
    public void setRateNotify(String symbol) {
        getPrefs().edit().putString(KEY_RATE_NOTIFY, symbol).apply();
    }
}
