package com.example.butymovaloftcoin.screens.main.converter;

import android.content.Intent;
import android.os.Bundle;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.interactors.ConverterInteractor;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@FragmentScope
public class ConverterPresenterImpl implements ConverterPresenter {

    private ConverterInteractor converterInteractor;
    private ConverterRouter router;
    private ConverterView view;
    private CompositeDisposable compositeDisposable;

    private CoinEntity sourceCurrency;
    private CoinEntity destinationCurrency;
    private String sourceAmount;

    @Inject
    public ConverterPresenterImpl(ConverterInteractor converterInteractor,
                                  CompositeDisposable compositeDisposable) {
        this.converterInteractor = converterInteractor;
        this.compositeDisposable = compositeDisposable;
    }

    public void attach(ConverterView view, ConverterRouter router){
        this.view = view;
        this.router = router;

        compositeDisposable.add(view.initEvent().subscribe(savedInstanceState -> converterInteractor.restoreInstanceState(savedInstanceState)));

        compositeDisposable.add(view.onSourceCurrencyClickEvent().subscribe(o -> router.showCurrencyBottomSheet(true)));

        compositeDisposable.add(view.onDestinationCurrencyClickEvent().subscribe(o -> router.showCurrencyBottomSheet(false)));

        compositeDisposable.add(view.onSourceAmountChangeEvent().subscribe(amount -> {
            sourceAmount = amount;
            converterInteractor.convert(sourceCurrency, destinationCurrency, sourceAmount);
        }));

        compositeDisposable.add(router.onSourceCurrencySelectedEvent().subscribe(this::setSourceCurrency));

        compositeDisposable.add(router.onDestinationCurrencySelectedEvent().subscribe(this::setDestinationCurrency));

        compositeDisposable.add(converterInteractor.sourceCurrencySelectedEvent().subscribe(this::setSourceCurrency));

        compositeDisposable.add(converterInteractor.destinationCurrencySelectedEvent().subscribe(this::setDestinationCurrency));

        compositeDisposable.add(converterInteractor.errorSourceCurrencySelectedEvent().subscribe(router::showToast));

        compositeDisposable.add(converterInteractor.errorDestinationCurrencySelectedEvent().subscribe(router::showToast));

        compositeDisposable.add(converterInteractor.sourceAmountEvent().subscribe(view::setSourceAmount));

        compositeDisposable.add(converterInteractor.destinationAmountEvent().subscribe(view::setDestinationAmount));
    }

    private void setSourceCurrency(CoinEntity coinEntity){
        if (view==null)
            return;
        if (coinEntity==null)
            return;
        sourceCurrency = coinEntity;
        view.setSourceCurrency(coinEntity.symbol);
        converterInteractor.setSourceCurrency(coinEntity.symbol);
        converterInteractor.convert(sourceCurrency, destinationCurrency, sourceAmount);
    }

    private void setDestinationCurrency(CoinEntity coinEntity){
        if (view==null)
            return;
        if (coinEntity==null)
            return;
        destinationCurrency = coinEntity;
        view.setDestinationCurrency(coinEntity.symbol);
        converterInteractor.setDestinationCurrency(coinEntity.symbol);
        converterInteractor.convert(sourceCurrency, destinationCurrency, sourceAmount);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        converterInteractor.saveInstanceState(outState, sourceCurrency, destinationCurrency);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (router!=null)
            router.onActivityResult(requestCode, resultCode, data);
    }

    public void detach(){
        converterInteractor.detach();
        compositeDisposable.clear();
        if (view!=null){
            view.detach();
            view = null;
        }
        if (router!=null){
            router.detach();
            router = null;
        }
    }
}
