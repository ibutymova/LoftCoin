package com.example.butymovaloftcoin.screens.main.rate;

import android.content.Intent;

import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.interactors.RateApiInteractor;
import com.example.butymovaloftcoin.interactors.RateDatabaseInteractor;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

@FragmentScope
public class RatePresenterImpl implements RatePresenter {

    private static String CURRENT_MENU_ITEM_NOTIFY;

    private RateApiInteractor rateApiInteractor;
    private RateDatabaseInteractor rateDatabaseInteractor;
    private RateRouter router;
    private RateView view;
    private Prefs prefs;
    private CompositeDisposable compositeDisposable;

    @Inject
    RatePresenterImpl(RateApiInteractor rateApiInteractor,
                      RateDatabaseInteractor rateDatabaseInteractor,
                      Prefs prefs,
                      CompositeDisposable compositeDisposable) {
       this.rateApiInteractor = rateApiInteractor;
       this.rateDatabaseInteractor = rateDatabaseInteractor;
       this.prefs = prefs;
       this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(RateView view, RateRouter router) {
        this.view = view;
        this.router = router;

        compositeDisposable.add(view.initEvent().subscribe(o -> rateDatabaseInteractor.loadRateDatabase()));

        compositeDisposable.add(view.onMenuItemShareClickEvent().subscribe(o -> router.requestPermissionCheck()));

        compositeDisposable.add(rateDatabaseInteractor.getRateShareTrueEvent().subscribe(router::startRateShareActivity));

        compositeDisposable.add(rateDatabaseInteractor.getRateShareFalseEvent().subscribe(router::showError));

        compositeDisposable.add(view.onMenuItemCurrencyClickEvent().subscribe(o -> router.showCurrencyDialog()));

        compositeDisposable.add(view.onMenuItemNotifyClickEvent().subscribe(tag -> {
            switch (CURRENT_MENU_ITEM_NOTIFY){
                case RateViewImpl.MENU_ITEM_NOTIFY_OFF:{
                    router.showAppSettingsNotificationsDialog();
                    break;
                }
                case RateViewImpl.MENU_ITEM_NOTIFY_ON:{
                    router.showRateNotifyToast();
                    break;
                }
                case RateViewImpl.MENU_ITEM_NOTIFY_SET_RATE:{
                    router.showRateNotifyOffDialog();
                    break;
                }
            }
        }));

        compositeDisposable.add(view.onSwipeRefreshEvent().subscribe(o -> rateApiInteractor.loadRateApi(prefs.getFiatCurrency())));

        compositeDisposable.add(view.onLongClickEvent().subscribe(router::showRateNotifyOnDialog));

        compositeDisposable.add(router.onRequestNotificationFalseEvent().subscribe(router::showError));

        compositeDisposable.add(router.onCurrencySelectedEvent().subscribe(currency -> {
            view.showProgress();
            rateApiInteractor.loadRateApi(currency);
        }));

        compositeDisposable.add(router.onJobStartedEvent().subscribe(symbol -> {
            prefs.setRateNotify(symbol);
            router.getNotificationState();
            router.showRateNotifyOnToast(symbol);
        }));

        compositeDisposable.add(router.onJobStoppedEvent().subscribe(o -> {
            prefs.setRateNotify(null);
            router.getNotificationState();
            router.showRateNotifyOffToast();
        }));

        compositeDisposable.add(router.onConfirmationRateNotifyOnEvent().subscribe(router::startJob));

        compositeDisposable.add(router.onConfirmationRateNotifyOffEvent().subscribe(o -> router.stopJob()));

        compositeDisposable.add(router.getNotificationStateEvent().subscribe(areNotificationsEnabled -> {
            if (areNotificationsEnabled){
                if (prefs.getRateNotify() != null) {
                    CURRENT_MENU_ITEM_NOTIFY = RateViewImpl.MENU_ITEM_NOTIFY_SET_RATE;
                    view.setNotificationsRate(prefs.getRateNotify());
                }
                else {
                    CURRENT_MENU_ITEM_NOTIFY = RateViewImpl.MENU_ITEM_NOTIFY_ON;
                    view.setNotificationsOn();
                }
            }
            else {
                CURRENT_MENU_ITEM_NOTIFY = RateViewImpl.MENU_ITEM_NOTIFY_OFF;
                view.setNotificationsOff();
            }
        }));

        compositeDisposable.add(router.onRequestPermissionTrueEvent().subscribe(o -> rateDatabaseInteractor.getRateForShare()));

        compositeDisposable.add(router.onRequestPermissionFalseEvent().subscribe(router::showError));

        compositeDisposable.add(router.onCreatePdfFalseEvent().subscribe(resId1 -> {router.showError(resId1); view.hideProgress();}));

        compositeDisposable.add(rateApiInteractor.loadRateResultTrueEvent().subscribe(currency -> hideAll()));

        compositeDisposable.add(rateApiInteractor.loadRateResultFalseEvent().subscribe(resId -> {
            hideAll();
            router.showError(resId);
        }));

        compositeDisposable.add(rateDatabaseInteractor.loadRateResultTrueEvent().subscribe(view::setCoins));

        compositeDisposable.add(rateDatabaseInteractor.loadRateResultFalseEvent().subscribe(router::showError));
    }

    @Override
    public void onStart() {
        if (router!=null)
            router.getNotificationState();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (router!=null)
            router.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (router!=null)
            router.onActivityResult(requestCode, resultCode, data);
    }

    private void hideAll(){
        if (view != null) {
            view.hideProgress();
            view.setRefresh(false);
        }
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
