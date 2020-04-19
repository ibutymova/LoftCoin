package com.example.butymovaloftcoin.screens.main.converter;

import android.os.Bundle;
import android.view.View;
import io.reactivex.Observable;

public interface ConverterView {

    void attach(View view);
    void initView(Bundle savedInstanceState);
    void setSourceCurrency(String currency);
    void setDestinationCurrency(String currency);
    void setDestinationAmount(String amount);
    void setSourceAmount(String amount);
    void detach();

    Observable<Object> onSourceCurrencyClickEvent();
    Observable<Object> onDestinationCurrencyClickEvent();
    Observable<String> onSourceAmountChangeEvent();
    Observable<Bundle> initEvent();
}
