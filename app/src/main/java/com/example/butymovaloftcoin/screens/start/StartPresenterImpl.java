package com.example.butymovaloftcoin.screens.start;

import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.interactors.RateApiInteractor;
import com.example.butymovaloftcoin.interactors.RateDatabaseInteractor;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@ActivityScope
public class StartPresenterImpl implements StartPresenter {

    private RateApiInteractor rateApiInteractor;
    private RateDatabaseInteractor rateDatabaseInteractor;
    private Prefs prefs;
    private CompositeDisposable compositeDisposable;

    private StartView view;
    private StartRouter router;

    @Inject
    StartPresenterImpl(RateApiInteractor rateApiInteractor,
                       RateDatabaseInteractor rateDatabaseInteractor,
                       Prefs prefs,
                       CompositeDisposable compositeDisposable) {
        this.rateApiInteractor = rateApiInteractor;
        this.rateDatabaseInteractor = rateDatabaseInteractor;
        this.prefs = prefs;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(StartView view, StartRouter router) {
        this.view = view;
        this.router = router;

        view.startAnimations();

        compositeDisposable.add(view.initEvent().subscribe(o -> rateApiInteractor.loadRateApi(prefs.getFiatCurrency())));

        compositeDisposable.add(rateApiInteractor.loadRateResultTrueEvent().subscribe(currency -> router.navigateToMainScreen()));

        compositeDisposable.add(rateApiInteractor.loadRateResultFalseEvent().subscribe(resId -> rateDatabaseInteractor.isRateDatabaseEmpty()));

        compositeDisposable.add(rateDatabaseInteractor.isRateDatabaseEmptyTrueEvent().subscribe(resId -> {
            router.showToast(resId);
            router.finish();
        }));

        compositeDisposable.add(rateDatabaseInteractor.isRateDatabaseEmptyFalseEvent().subscribe(o -> router.navigateToMainScreen()));
    }

    @Override
    public void detach() {
        rateApiInteractor.detach();
        rateDatabaseInteractor.detach();
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
