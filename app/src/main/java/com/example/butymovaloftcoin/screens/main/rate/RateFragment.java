package com.example.butymovaloftcoin.screens.main.rate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.di.DaggerRateComponent;
import com.example.butymovaloftcoin.di.RateComponent;

public class RateFragment extends Fragment  {

    private RatePresenter presenter;

    public RateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RateComponent component = DaggerRateComponent.createComponent(App.getAppComponent());
        RateView contentView = component.getRateView();
        contentView.attach(view);
        RateRouter router = component.getRateRouter();
        router.attach(this);
        presenter = component.getRatePresenter();
        presenter.attach(contentView, router);
        contentView.initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (requireActivity().isFinishing())
            DaggerRateComponent.clearComponent();
        super.onDestroy();
    }
}
