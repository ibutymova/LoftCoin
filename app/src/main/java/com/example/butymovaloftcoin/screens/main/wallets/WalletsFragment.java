package com.example.butymovaloftcoin.screens.main.wallets;

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
import com.example.butymovaloftcoin.di.DaggerWalletsComponent;
import com.example.butymovaloftcoin.di.WalletsComponent;

public class WalletsFragment extends Fragment {

    private WalletsPresenter presenter;

    public WalletsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WalletsComponent component = DaggerWalletsComponent.createComponent(App.getAppComponent());
        WalletsView contentView = component.getWalletsView();
        contentView.attach(view);
        WalletsRouter router = component.getWalletsRouter();
        router.attach(this);
        presenter = component.getWalletsPresenter();
        presenter.attach(contentView, router);
        contentView.initView(savedInstanceState); 
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        presenter.detach();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (requireActivity().isFinishing())
            DaggerWalletsComponent.clearComponent();
        super.onDestroy();
    }
}
