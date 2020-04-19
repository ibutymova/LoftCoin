package com.example.butymovaloftcoin.screens.main.wallets;

import android.content.Intent;
import android.os.Bundle;

import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.interactors.WalletsInteractor;
import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

@FragmentScope
public class WalletsPresenterImpl implements WalletsPresenter {

    private WalletsInteractor walletsInteractor;
    private CompositeDisposable compositeDisposable;

    private WalletsView view;
    private WalletsRouter router;

    private int walletPosition = 0;

    @Inject
    public WalletsPresenterImpl(WalletsInteractor walletsInteractor,
                                CompositeDisposable compositeDisposable) {
        this.walletsInteractor = walletsInteractor;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(WalletsView view, WalletsRouter router) {
        this.view = view;
        this.router = router;


        compositeDisposable.add(view.initEvent().subscribe(savedInstanceState -> walletsInteractor.init(savedInstanceState)));

        compositeDisposable.add(view.onNewWalletClickEvent().subscribe(o -> router.showCurrencyBottomSheet()));

        compositeDisposable.add(view.onRemoveWalletClickEvent().subscribe(router::showWalletRemoveDialog));

        compositeDisposable.add(view.onWalletChangePositionEvent().subscribe(position -> {
            walletPosition = position;
            walletsInteractor.getTransactions(position);}));

        compositeDisposable.add(router.onCurrencySelectedEvent().subscribe(coinEntity -> walletsInteractor.createWallet(coinEntity)));

        compositeDisposable.add(walletsInteractor.errorRemovingWalletEvent().subscribe(router::showToast));

        compositeDisposable.add(router.onConfirmationRemoveWalletEvent().subscribe(position -> walletsInteractor.removeWallet(position)));

        compositeDisposable.add(walletsInteractor.moveLastWalletEvent().subscribe(view::moveLastWallet));

        compositeDisposable.add(walletsInteractor.removedWalletEvent().subscribe(router::showToast));

        compositeDisposable.add(walletsInteractor.errorRemovingWalletEvent().subscribe(router::showToast));

        compositeDisposable.add(walletsInteractor.setCurrentWalletEvent().subscribe(o -> view.setCurrentWallet()));

        compositeDisposable.add(walletsInteractor.newWalletVisibleEvent().subscribe(view::setNewWalletVisible));

        compositeDisposable.add(walletsInteractor.errorSavingWalletEvent().subscribe(router::showToast));

        compositeDisposable.add(walletsInteractor.transactionItemsEvent().subscribe(view::setAdapterList));

        compositeDisposable.add(walletsInteractor.errorLoadingTransactionsEvent().subscribe(router::showToast));

        compositeDisposable.add(walletsInteractor.walletsItemsEvent().subscribe(view::setPageAdapterList));

        compositeDisposable.add(walletsInteractor.errorLoadingWalletsEvent().subscribe(router::showToast));

        compositeDisposable.add(walletsInteractor.walletsVisibleEvent().subscribe(view::setWalletsVisible));

        compositeDisposable.add(walletsInteractor.menuItemRemoveVisibleEvent().subscribe(view::setMenuItemRemoveVisible));

        compositeDisposable.add(walletsInteractor.setCurrentWalletPosEvent().subscribe(view::setCurrentWallet));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        walletsInteractor.saveInstanceState(outState, walletPosition);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (router!=null)
            router.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void detach() {
        walletsInteractor.detach();
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
