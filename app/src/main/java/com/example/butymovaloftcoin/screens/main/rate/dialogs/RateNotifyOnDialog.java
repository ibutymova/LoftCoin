package com.example.butymovaloftcoin.screens.main.rate.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.butymovaloftcoin.R;

import java.util.Objects;

public class RateNotifyOnDialog extends DialogFragment {

    public static final String TAG = "rate_notify_on_dialog";
    public static final String KEY_RATE_SYMBOL = "rate_symbol";

    private String symbol;
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null && savedInstanceState.containsKey(KEY_RATE_SYMBOL))
            symbol = savedInstanceState.getString(KEY_RATE_SYMBOL);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(R.string.app_name)
                .setMessage(String.format(getResources().getString(R.string.rate_notify_on_message), symbol))
                .setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(KEY_RATE_SYMBOL, symbol)))
                .setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dismiss());

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RATE_SYMBOL, symbol);
    }
}
