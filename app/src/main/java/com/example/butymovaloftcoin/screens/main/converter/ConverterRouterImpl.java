package com.example.butymovaloftcoin.screens.main.converter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheet;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheetView;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheet.EXTRA_CURRENCY;

public class ConverterRouterImpl implements ConverterRouter {

    private static final int REQUEST_CODE_SOURCE_CURRENCY_BOTTOM_SHEET = 101;
    private static final int REQUEST_CODE_DESTINATION_CURRENCY_BOTTOM_SHEET = 102;

    private static final String SOURCE_CURRENCY_BOTTOM_SHEET_TAG = "source_currency_bottom_sheet_tag";
    private static final String DESTINATION_CURRENCY_BOTTOM_SHEET_TAG = "destination_currency_bottom_sheet_tag";

    private BehaviorSubject<CoinEntity> onSourceCurrencySelectedEvent = BehaviorSubject.create();
    private BehaviorSubject<CoinEntity> onDestinationCurrencySelectedEvent = BehaviorSubject.create();

    private Fragment fragment;
  //  private CurrenciesBottomSheetView.Listener sourceListener, destinationListener;

    @Inject
    public ConverterRouterImpl() {
    }

    @Override
    public void attach(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void showCurrencyBottomSheet(Boolean flag) {
        CurrenciesBottomSheet currenciesBottomSheet = new CurrenciesBottomSheet();
        if (flag){
            currenciesBottomSheet.show(fragment.requireActivity().getSupportFragmentManager(), SOURCE_CURRENCY_BOTTOM_SHEET_TAG);
            currenciesBottomSheet.setTargetFragment(fragment, REQUEST_CODE_SOURCE_CURRENCY_BOTTOM_SHEET);
        }
        else
        {
            currenciesBottomSheet.show(fragment.requireActivity().getSupportFragmentManager(), DESTINATION_CURRENCY_BOTTOM_SHEET_TAG);
            currenciesBottomSheet.setTargetFragment(fragment, REQUEST_CODE_DESTINATION_CURRENCY_BOTTOM_SHEET);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SOURCE_CURRENCY_BOTTOM_SHEET:{
                onSourceCurrencySelectedEvent.onNext(data.getParcelableExtra(EXTRA_CURRENCY));
                return;
            }
            case REQUEST_CODE_DESTINATION_CURRENCY_BOTTOM_SHEET:{
                onDestinationCurrencySelectedEvent.onNext(data.getParcelableExtra(EXTRA_CURRENCY));
                return;
            }
        }
    }

    @Override
    public void showToast(Integer errorId) {
        Toast.makeText(fragment.requireActivity().getApplicationContext(), fragment.getString(errorId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void detach() {
        this.fragment=null;
    }

    @Override
    public Observable<CoinEntity> onSourceCurrencySelectedEvent() {
        return onSourceCurrencySelectedEvent;
    }

    @Override
    public Observable<CoinEntity> onDestinationCurrencySelectedEvent() {
        return onDestinationCurrencySelectedEvent;
    }
}
