package com.example.butymovaloftcoin.interactors;

import android.os.Bundle;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.db.model.Transaction;
import com.example.butymovaloftcoin.data.db.model.TransactionModel;
import com.example.butymovaloftcoin.data.db.model.Wallet;
import com.example.butymovaloftcoin.data.db.model.WalletModel;
import com.example.butymovaloftcoin.screens.main.wallets.utils.WalletsPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class WalletsInteractorImpl implements WalletsInteractor {

    private static final String WALLET_POSITION_KEY = "wallet_position";

    private PublishSubject<List<WalletModel>> moveLastWalletEvent = PublishSubject.create();
    private PublishSubject<Integer> removedWalletEvent = PublishSubject.create();
    private PublishSubject<Object> setCurrentWalletEvent = PublishSubject.create();
    private PublishSubject<Integer> setCurrentWalletPosEvent = PublishSubject.create();

    private BehaviorSubject<Boolean> walletsVisibleEvent = BehaviorSubject.create();
    private BehaviorSubject<Boolean> newWalletVisibleEvent = BehaviorSubject.create();

    private BehaviorSubject<List<WalletModel>> walletsItemsEvent = BehaviorSubject.create();
    private BehaviorSubject<List<TransactionModel>> transactionItemsEvent = BehaviorSubject.create();
    private PublishSubject<Boolean> menuItemRemoveVisibleEvent = PublishSubject.create();

    private PublishSubject<Integer> errorLoadingWalletsEvent = PublishSubject.create();
    private PublishSubject<Integer> errorLoadingTransactionsEvent = PublishSubject.create();
    private PublishSubject<Integer> errorRemovingWalletEvent = PublishSubject.create();
    private PublishSubject<Integer> errorSavingWalletEvent = PublishSubject.create();

    private Database database;
    private Random  random;
    private WalletsPosition walletsPosition;
    private CompositeDisposable compositeDisposable;

    @Inject
    WalletsInteractorImpl(Database database,
                          Random random,
                          WalletsPosition walletsPosition,
                          CompositeDisposable compositeDisposable) {
        this.database = database;
        this.random = random;
        this.walletsPosition = walletsPosition;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        walletsPosition.setWalletsPosition(savedInstanceState.getInt(WALLET_POSITION_KEY, 0));
        getWallets();
    }

    @Override
    public void getTransactions(Integer walletPosition) {
        walletsPosition.setWalletsPosition(walletPosition);
        getTransactionsInner(walletsItemsEvent.getValue().get(walletPosition).wallet.walletId);
    }

    private void getTransactionsInner(String walletId){
        compositeDisposable.add(database.getTransactions(walletId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactionModels -> transactionItemsEvent.onNext(transactionModels),
                        throwable -> errorLoadingTransactionsEvent.onNext(R.string.error_load_transactions))
        );
    }

    @Override
    public void removeWallet(Integer walletPosition) {
        Wallet wallet = walletsItemsEvent.getValue().get(walletPosition).wallet;

        compositeDisposable.add(Observable.fromCallable(() -> {
            database.deleteTransactions(wallet.walletId);
            database.deleteWallet(wallet);
            return new Object();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        o -> removedWalletEvent.onNext(R.string.result_remove_wallet),
                        throwable -> errorRemovingWalletEvent.onNext(R.string.error_remove_wallet)
                )
        );
    }


    @Override
    public void getWallets(){
        compositeDisposable.add( database.getWallets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletModels -> {
                    int oldSize;

                    if (walletModels.isEmpty()){
                        newWalletVisibleEvent.onNext(true);
                        walletsVisibleEvent.onNext(false);
                        menuItemRemoveVisibleEvent.onNext(false);
                        getTransactionsInner(null);
                    }
                    else
                    {
                        newWalletVisibleEvent.onNext(false);
                        walletsVisibleEvent.onNext(true);
                        menuItemRemoveVisibleEvent.onNext(true);

                        if (walletsItemsEvent.getValue() == null || walletsItemsEvent.getValue().isEmpty())
                            oldSize = walletModels.size();
                        else
                            oldSize = walletsItemsEvent.getValue().size();

                        walletsItemsEvent.onNext(walletModels);

                        if (walletsItemsEvent.getValue().size() > oldSize) /* wallet added */
                            moveLastWalletEvent.onNext(walletModels);
                        else
                            if (oldSize==walletModels.size())
                                setCurrentWalletPosEvent.onNext(walletsPosition.getWalletsPosition()); /*refresh*/
                            else
                                setCurrentWalletEvent.onNext(new Object()); /* wallet removed */
                    }
                }, throwable -> errorLoadingWalletsEvent.onNext(R.string.error_load_wallets))
        );
    }

    private void saveWallet(Wallet wallet, List<Transaction> transactionList){
        compositeDisposable.add(Observable.fromCallable(() -> {
            database.saveWallet(wallet);
            database.saveTransaction(transactionList);
            return new Object();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> errorSavingWalletEvent.onNext(R.string.error_save_wallet))
                .subscribe()
        );
    }

    @Override
    public void createWallet(CoinEntity coinEntity) {
        Wallet wallet = createRandomWallet(coinEntity);
        List<Transaction> transactionList = createRandomTransactions(wallet);
        saveWallet(wallet, transactionList);
    }

    @Override
    public void saveInstanceState(Bundle outState, int walletPosition) {
        outState.putInt(WALLET_POSITION_KEY, walletPosition);
    }

    private Wallet createRandomWallet(CoinEntity coinEntity){
        return new Wallet(UUID.randomUUID().toString(), coinEntity.id, 0);
    }

    private Transaction createRandonTransaction(Wallet wallet){
        long startDate = 1577836800000L;
        long nowDate = System.currentTimeMillis();
        long date = startDate + (long)(random.nextDouble()*(nowDate - startDate));

        double amount = 2*random.nextDouble();
        boolean amountSign = random.nextBoolean();

        return new Transaction(wallet.walletId, wallet.currencyId, amountSign ? amount : -amount, date );
    }

    private List<Transaction> createRandomTransactions(Wallet wallet){
        List<Transaction> transactionList = new ArrayList<>();
        Transaction transaction;
        double walletAmount = 0;
        for(int i=0; i<20 ;i++){
            transaction = createRandonTransaction(wallet);
            walletAmount = walletAmount + transaction.amount;
            transactionList.add(transaction);
        }
        wallet.amount = walletAmount;
        return transactionList;
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
    }

    @Override
    public Observable<List<WalletModel>> moveLastWalletEvent() {
        return moveLastWalletEvent;
    }

    @Override
    public Observable<Integer> removedWalletEvent() {
        return removedWalletEvent;
    }

    @Override
    public Observable<Object> setCurrentWalletEvent() {
        return setCurrentWalletEvent;
    }

    @Override
    public Observable<Integer> setCurrentWalletPosEvent() {
        return setCurrentWalletPosEvent;
    }

    @Override
    public Observable<Boolean> walletsVisibleEvent() {
        return walletsVisibleEvent;
    }

    @Override
    public Observable<Boolean> newWalletVisibleEvent() {
        return newWalletVisibleEvent;
    }

    @Override
    public Observable<List<WalletModel>> walletsItemsEvent() {
        return walletsItemsEvent;
    }

    @Override
    public Observable<List<TransactionModel>> transactionItemsEvent() {
        return transactionItemsEvent;
    }

    @Override
    public Observable<Boolean> menuItemRemoveVisibleEvent() {
        return menuItemRemoveVisibleEvent;
    }

    @Override
    public Observable<Integer> errorLoadingWalletsEvent() {
        return errorLoadingWalletsEvent;
    }

    @Override
    public Observable<Integer> errorLoadingTransactionsEvent() {
        return errorLoadingTransactionsEvent;
    }

    @Override
    public Observable<Integer> errorRemovingWalletEvent() {
        return errorRemovingWalletEvent;
    }

    @Override
    public Observable<Integer> errorSavingWalletEvent() {
        return errorSavingWalletEvent;
    }

}
