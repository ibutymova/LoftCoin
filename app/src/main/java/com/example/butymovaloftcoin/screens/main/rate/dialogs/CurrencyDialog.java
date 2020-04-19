package com.example.butymovaloftcoin.screens.main.rate.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.model.Fiat;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CurrencyDialog extends DialogFragment {
    public static final String TAG = "currency_dialog";
    public static final String EXTRA_CURRENCY = "currency";

    private Unbinder unbinder;

    @BindView(R.id.usd)
    ViewGroup usd;

    @BindView(R.id.eur)
    ViewGroup eur;

    @BindView(R.id.rub)
    ViewGroup rub;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_currency, null);

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();

        unbinder = ButterKnife.bind(this, view);

        usd.setOnClickListener(v -> {
            Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(EXTRA_CURRENCY, Fiat.USD));
            dismiss();
        });

        eur.setOnClickListener(v -> {
            Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(EXTRA_CURRENCY, Fiat.EUR));
            dismiss();
        });

        rub.setOnClickListener(v -> {
            Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(EXTRA_CURRENCY, Fiat.RUB));
            dismiss();
        });

        return alertDialog;
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
