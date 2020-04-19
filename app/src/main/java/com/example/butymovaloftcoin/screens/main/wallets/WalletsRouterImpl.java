package com.example.butymovaloftcoin.screens.main.wallets;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheet;
import com.example.butymovaloftcoin.screens.main.wallets.dialogs.WalletRemoveDialog;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheet.EXTRA_CURRENCY;
import static com.example.butymovaloftcoin.screens.main.wallets.dialogs.WalletRemoveDialog.KEY_POSITION;

public class WalletsRouterImpl implements WalletsRouter {

    private static final int REQUEST_CODE_CURRENCY_BOTTOM_SHEET = 101;
    private static final int REQUEST_CODE_WALLET_REMOVE_DIALOG =102;

    private Fragment fragment;

    private PublishSubject<CoinEntity> onCurrencySelectedEvent = PublishSubject.create();
    private PublishSubject<Integer> onConfirmationRemoveWalletEvent = PublishSubject.create();

    @Inject
    public WalletsRouterImpl() {

    }

    @Override
    public void attach(Fragment fragment) {
        this.fragment = fragment;
    }

    public void showCurrencyBottomSheet(){
        CurrenciesBottomSheet currenciesBottomSheet = new CurrenciesBottomSheet();
        currenciesBottomSheet.setTargetFragment(fragment, REQUEST_CODE_CURRENCY_BOTTOM_SHEET);
        currenciesBottomSheet.show(fragment.requireActivity().getSupportFragmentManager(), CurrenciesBottomSheet.TAG);
   }

    @Override
    public void showWalletRemoveDialog(Integer position) {
        WalletRemoveDialog dialog = new WalletRemoveDialog();
        dialog.setWalletPosition(position);
        dialog.setTargetFragment(fragment, REQUEST_CODE_WALLET_REMOVE_DIALOG);
        dialog.show(fragment.requireActivity().getSupportFragmentManager(), WalletRemoveDialog.TAG);
    }

    @Override
    public void showToast(Integer resId) {
        Toast.makeText(fragment.requireActivity().getApplicationContext(), fragment.getString(resId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_CURRENCY_BOTTOM_SHEET:{
                onCurrencySelectedEvent.onNext(data.getParcelableExtra(EXTRA_CURRENCY));
                return;
            }
            case REQUEST_CODE_WALLET_REMOVE_DIALOG:{
                onConfirmationRemoveWalletEvent.onNext(data.getIntExtra(KEY_POSITION, -1));
                return;
            }
        }
    }

    @Override
    public void detach() {
        this.fragment=null;
    }

    @Override
    public Observable<CoinEntity> onCurrencySelectedEvent() {
        return onCurrencySelectedEvent;
    }

    @Override
    public Observable<Integer> onConfirmationRemoveWalletEvent() {
        return onConfirmationRemoveWalletEvent;
    }
}
