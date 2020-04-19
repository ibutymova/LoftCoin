package com.example.butymovaloftcoin.screens.currencies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.di.CurrenciesComponent;
import com.example.butymovaloftcoin.di.DaggerCurrenciesComponent;

import java.util.Objects;

public class CurrenciesBottomSheet extends BottomSheetDialogFragment implements CurrenciesBottomSheetView.Listener {

    public static final String TAG = "currencies_bottom_sheet";
    public static final String EXTRA_CURRENCY = "currency";

    private CurrenciesBottomSheetPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_currencies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CurrenciesComponent component = DaggerCurrenciesComponent.createComponent(App.getAppComponent());
        CurrenciesBottomSheetView contentView = component.getCurrenciesBottomSheetView();
        contentView.attach(view, this);
        presenter = component.getCurrenciesBottomSheetPresenter();
        presenter.attach(contentView);
        contentView.initView();
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
    }

    @Override
    public void onCurrencySelected(CoinEntity coinEntity) {
        dismiss();
        Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(EXTRA_CURRENCY, coinEntity));
    }

    @Override
    public void onDestroy() {
        if (requireActivity().isFinishing())
            DaggerCurrenciesComponent.clearComponent();
        super.onDestroy();
    }
}
