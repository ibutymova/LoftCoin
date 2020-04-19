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

public class PermissionDialog extends DialogFragment {
    public static final String TAG = "permission_dialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.permission_dialog_title)
                .setMessage(R.string.permission_dialog_message)
                .setPositiveButton(R.string.permission_dialog_positive_button, (dialog, which) -> Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null))
                .setNegativeButton(R.string.permission_dialog_negative_button, (dialog, which) -> Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null));

        return builder.create();
    }
}
