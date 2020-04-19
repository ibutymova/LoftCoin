package com.example.butymovaloftcoin.screens.main.rate.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.butymovaloftcoin.R;

import java.util.Objects;

public class AppSettingsDialog extends DialogFragment {
    public static final String TAG = "app_settings_dialog";
    private static final String KEY_MESSAGE_ID = "message_id";

    @StringRes
    private int messageId;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState!=null)
            messageId = savedInstanceState.getInt(KEY_MESSAGE_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.app_settings_title)
                .setMessage(messageId)
                .setPositiveButton(R.string.app_settings_positive_button, (dialog, which) -> Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null))
                .setNegativeButton(R.string.app_settings_negative_button, (dialog, which) -> Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, null));
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MESSAGE_ID, messageId);
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
