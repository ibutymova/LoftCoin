package com.example.butymovaloftcoin.screens.main;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.di.scopes.ActivityScope;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


@ActivityScope
public class MainPresenterImpl implements MainPresenter {

    private MainView view;
    private MainRouter router;
    private CompositeDisposable compositeDisposable;

    @Inject
    public MainPresenterImpl(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(MainView view, MainRouter router) {
        this.view = view;
        this.router = router;

        compositeDisposable.add(view.itemBottomNavigationClickEvent().subscribe(itemID -> {
            switch (itemID) {
                case R.id.menu_wallets:{
                    router.showWalletsFragment();
                    break;
                }
                case R.id.menu_rate:{
                    router.showRateFragment();
                    break;
                }
                case R.id.menu_converter:{
                    router.showConverterFragment();
                    break;
                }
            }
        }));

        compositeDisposable.add(view.initEvent().subscribe(bundle -> {
            if (bundle.size()==0)
                view.setSelectedItemId(R.id.menu_rate);
        }));

        compositeDisposable.add(view.newIntentEvent().subscribe(isOpenFromNotification -> {
            if (isOpenFromNotification)
                view.setSelectedItemId(R.id.menu_rate);
        }));
    }

    @Override
    public void detach() {
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
