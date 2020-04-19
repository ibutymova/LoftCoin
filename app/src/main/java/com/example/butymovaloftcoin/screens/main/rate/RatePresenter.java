package com.example.butymovaloftcoin.screens.main.rate;

import android.content.Intent;

public interface RatePresenter {

    void attach(RateView view, RateRouter router);
    void onStart();
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void detach();
}
