package com.example.butymovaloftcoin.screens.main.converter;

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
import com.example.butymovaloftcoin.di.ConverterComponent;
import com.example.butymovaloftcoin.di.DaggerConverterComponent;

public class ConverterFragment extends Fragment {

    private ConverterPresenter presenter;

    public ConverterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConverterComponent component = DaggerConverterComponent.createComponent(App.getAppComponent());
        ConverterView contentView = component.getConverterView();
        contentView.attach(view);
        ConverterRouter router = component.getConverterRouter();
        router.attach(this);
        presenter = component.getConverterPresenter();
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
            DaggerConverterComponent.clearComponent();
        super.onDestroy();
    }
}
