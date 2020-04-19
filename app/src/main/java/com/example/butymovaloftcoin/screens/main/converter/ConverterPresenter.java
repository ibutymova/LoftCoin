package com.example.butymovaloftcoin.screens.main.converter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public interface ConverterPresenter {

    void attach(ConverterView view, ConverterRouter router);

    void onSaveInstanceState(Bundle outState);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void detach();
}
