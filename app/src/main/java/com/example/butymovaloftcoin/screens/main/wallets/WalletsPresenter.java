package com.example.butymovaloftcoin.screens.main.wallets;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public interface WalletsPresenter {

    void attach(WalletsView view, WalletsRouter router);

    void onSaveInstanceState(Bundle outState);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void detach();
}
