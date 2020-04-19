package com.example.butymovaloftcoin.screens.main.rate;

import android.view.View;

import androidx.annotation.StringRes;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import java.util.List;
import io.reactivex.Observable;

public interface RateView {

    void attach(View view);
    void initView();
    void setCoins(List<CoinEntity> coinEntityList);
    void showProgress();
    void hideProgress();
    void setRefresh(boolean refreshing);
    void setNotificationsOn();
    void setNotificationsOff();
    void setNotificationsRate(String symbol);
    void detach();

    Observable<Object> onMenuItemShareClickEvent();
    Observable<Object> onMenuItemCurrencyClickEvent();
    Observable<Object> onMenuItemNotifyClickEvent();
    Observable<Object> onSwipeRefreshEvent();
    Observable<String> onLongClickEvent();
    Observable<Object> initEvent();

}
