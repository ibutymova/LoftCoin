package com.example.butymovaloftcoin.screens.main.wallets.dialogs;

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

public class WalletRemoveDialog extends DialogFragment {
    public static final String TAG = "wallet_remove_dialog";
    public static final String KEY_POSITION = "wallet_position";

    private int walletPosition;
    public void setWalletPosition(int walletPosition) {
        this.walletPosition = walletPosition;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null)
            walletPosition = savedInstanceState.getInt(KEY_POSITION, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setTitle(R.string.app_name)
                .setMessage(R.string.remove_wallet_message)
                .setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(KEY_POSITION, walletPosition)))
                .setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dismiss());

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_POSITION, walletPosition);
    }
}
