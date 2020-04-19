package com.example.butymovaloftcoin.screens.main.rate;

import android.content.Intent;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.model.Fiat;
import java.util.List;

import io.reactivex.Observable;

public interface RateRouter {

    void attach(Fragment fragment);
    void showCurrencyDialog();
    void showRateNotifyOnDialog(String symbol);
    void startJob(String symbol);
    void stopJob();
    void getNotificationState();
    void showAppSettingsNotificationsDialog();
    void showRateNotifyOffDialog();
    void startRateShareActivity(List<CoinEntity> coinEntities);
    void requestPermissionCheck();
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void showError(@StringRes Integer resId);
    void showRateNotifyOnToast(String symbol);
    void showRateNotifyToast();
    void showRateNotifyOffToast();
    void detach();

    Observable<Fiat> onCurrencySelectedEvent();
    Observable<String> onJobStartedEvent();
    Observable<Object> onJobStoppedEvent();
    Observable<String> onConfirmationRateNotifyOnEvent();
    Observable<Object> onConfirmationRateNotifyOffEvent();
    Observable<Boolean> getNotificationStateEvent();
    Observable<Object> onRequestPermissionTrueEvent();
    Observable<Integer> onRequestPermissionFalseEvent();
    Observable<Integer> onRequestNotificationFalseEvent();
    Observable<Integer> onCreatePdfFalseEvent();
}
