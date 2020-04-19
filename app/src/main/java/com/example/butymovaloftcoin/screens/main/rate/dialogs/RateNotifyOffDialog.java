package com.example.butymovaloftcoin.screens.main.rate.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.butymovaloftcoin.R;

import java.util.Objects;

public class RateNotifyOffDialog extends DialogFragment {
    public static final String TAG = "rate_notify_off_dialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.app_name)
                .setMessage(R.string.rate_notify_off_message)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null))
                .setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dismiss());
        return builder.create();
    }
}
