package com.example.butymovaloftcoin.screens.main.converter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.model.Currency;

import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class ConverterViewImpl implements ConverterView, View.OnClickListener {

    private View view;

    private PublishSubject<Object> onSourceCurrencyClickEvent = PublishSubject.create();
    private PublishSubject<Object> onDestinationCurrencyClickEvent = PublishSubject.create();
    private PublishSubject<String> onSourceAmountChangeEvent = PublishSubject.create();
    private PublishSubject<Bundle> initEvent = PublishSubject.create();

    @BindView(R.id.destination_amount)
    EditText destinationAmount;

    @BindView(R.id.destination_currency)
    ViewGroup destinationCurrency;

    @BindView(R.id.source_amount)
    EditText sourceAmount;

    @BindView(R.id.source_currency)
    ViewGroup sourceCurrency;

    @BindView(R.id.converter_toolbar)
    Toolbar toolbar;

    private int[] colors;
    private Random random;
    private CompositeDisposable compositeDisposable;
    private Unbinder unbinder;

    private TextView sourceSymbolText;
    private ImageView sourceSymbolIcon;
    private TextView sourceName;

    private TextView destinationSymbolText;
    private ImageView destinationSymbolIcon;
    private TextView destinationName;

    @Inject
    public ConverterViewImpl(int[] colors, Random random, CompositeDisposable compositeDisposable) {
        this.colors = colors;
        this.random = random;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(View view) {
        this.view = view;

        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.converter_screen_title);

        compositeDisposable.add(RxTextView.afterTextChangeEvents(sourceAmount)
                .subscribe(event -> onSourceAmountChangeEvent.onNext(Objects.requireNonNull(event.editable()).toString())));

        sourceSymbolText = sourceCurrency.findViewById(R.id.symbol_text);
        sourceSymbolIcon = sourceCurrency.findViewById(R.id.symbol_icon);
        sourceName = sourceCurrency.findViewById(R.id.currency_name);

        destinationSymbolText = destinationCurrency.findViewById(R.id.symbol_text);
        destinationSymbolIcon = destinationCurrency.findViewById(R.id.symbol_icon);
        destinationName = destinationCurrency.findViewById(R.id.currency_name);

        sourceCurrency.setOnClickListener(this);
        destinationCurrency.setOnClickListener(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initEvent.onNext(savedInstanceState==null ? new Bundle() : savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.source_currency:{
                onSourceCurrencyClickEvent.onNext(new Object());
                return;
            }
            case R.id.destination_currency:{
                onDestinationCurrencyClickEvent.onNext(new Object());
                return;
            }
        }
    }

    private void bind(String curr, ImageView symbolIcon, TextView symbolText, TextView currencyName){
        Currency currency = Currency.getCurrency(curr);
        if (currency != null){
            symbolIcon.setImageResource(currency.iconRes);
            symbolText.setVisibility(View.GONE);
            symbolIcon.setVisibility(View.VISIBLE);

        }
        else {
            symbolText.setText(String.valueOf(curr.charAt(0)));
            symbolText.setVisibility(View.VISIBLE);
            symbolIcon.setVisibility(View.GONE);

            Drawable background = symbolText.getBackground();
            Drawable wrapped = DrawableCompat.wrap(background);
            DrawableCompat.setTint(wrapped, colors[random.nextInt(colors.length)]);
        }

        currencyName.setText(curr);
    }

    @Override
    public void setSourceCurrency(String currency) {
        bind(currency, sourceSymbolIcon, sourceSymbolText, sourceName);
    }

    @Override
    public void setDestinationCurrency(String currency) {
        bind(currency, destinationSymbolIcon, destinationSymbolText, destinationName);
    }

    @Override
    public void setDestinationAmount(String amount) {
        destinationAmount.setText(amount);
    }

    @Override
    public void setSourceAmount(String amount) {
        sourceAmount.setText(amount);
    }

    @Override
    public void detach() {
        view=null;
        compositeDisposable.dispose();
        unbinder.unbind();
    }

    @Override
    public Observable<Object> onSourceCurrencyClickEvent() {
        return onSourceCurrencyClickEvent;
    }

    @Override
    public Observable<Object> onDestinationCurrencyClickEvent() {
        return onDestinationCurrencyClickEvent;
    }

    @Override
    public Observable<String> onSourceAmountChangeEvent() {
        return onSourceAmountChangeEvent;
    }

    @Override
    public Observable<Bundle> initEvent() {
        return initEvent;
    }
}
