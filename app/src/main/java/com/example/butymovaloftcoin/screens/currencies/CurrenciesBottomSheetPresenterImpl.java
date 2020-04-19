package com.example.butymovaloftcoin.screens.currencies;

import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.interactors.RateDatabaseInteractor;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@FragmentScope
public class CurrenciesBottomSheetPresenterImpl implements CurrenciesBottomSheetPresenter {

    private RateDatabaseInteractor rateDatabaseInteractor;
    private CurrenciesBottomSheetView view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CurrenciesBottomSheetPresenterImpl(RateDatabaseInteractor rateDatabaseInteractor,
                                              CompositeDisposable compositeDisposable) {
        this.rateDatabaseInteractor = rateDatabaseInteractor;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(CurrenciesBottomSheetView view) {
        this.view = view;

        compositeDisposable.add(rateDatabaseInteractor.loadRateResultTrueEvent().subscribe(view::setAdapterList));

        compositeDisposable.add(view.initEvent().subscribe(o -> rateDatabaseInteractor.loadRateDatabase()));
    }

    @Override
    public void detach() {
        rateDatabaseInteractor.detach();
        compositeDisposable.clear();
        if (view!=null) {
            view.detach();
            view = null;
        }
    }
}
