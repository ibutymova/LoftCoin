package com.example.butymovaloftcoin.screens.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.screens.main.converter.ConverterFragment;
import com.example.butymovaloftcoin.screens.main.rate.RateFragment;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsFragment;

import javax.inject.Inject;

public class MainRouterImpl implements MainRouter {

    private static final String CONVERTER_FRAGMENT_TAG = "converter_fragment_tag";
    private static final String WALLETS_FRAGMENT_TAG = "wallets_fragment_tag";
    private static final String RATE_FRAGMENT_TAG = "rate_fragment_tag";

    private AppCompatActivity activity;

    @Inject
    public MainRouterImpl() {

    }

    @Override
    public void attach(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void showRateFragment() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        hideWalletsFragment(transaction);
        hideConverterFragment(transaction);

        Fragment rateFragment = activity.getSupportFragmentManager().findFragmentByTag(RATE_FRAGMENT_TAG);
        if (rateFragment==null){
            rateFragment = new RateFragment();
            transaction.add(R.id.container_fragment, rateFragment, RATE_FRAGMENT_TAG);
        }

        transaction.show(rateFragment)
                 .commit();
    }

    @Override
    public void showWalletsFragment() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        hideRateFragment(transaction);
        hideConverterFragment(transaction);

        Fragment walletsFragment = activity.getSupportFragmentManager().findFragmentByTag(WALLETS_FRAGMENT_TAG);
        if (walletsFragment == null){
            walletsFragment = new WalletsFragment();
            transaction.add(R.id.container_fragment, walletsFragment, WALLETS_FRAGMENT_TAG);
        }

        transaction.show(walletsFragment)
                 .commit();
    }

    @Override
    public void showConverterFragment() {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        hideWalletsFragment(transaction);
        hideRateFragment(transaction);

        Fragment converterFragment = activity.getSupportFragmentManager().findFragmentByTag(CONVERTER_FRAGMENT_TAG);
        if (converterFragment == null){
            converterFragment = new ConverterFragment();
            transaction.add(R.id.container_fragment, converterFragment, CONVERTER_FRAGMENT_TAG);
        }

        transaction.show(converterFragment)
                 .commit();
    }

    @Override
    public void detach() {
        activity = null;
    }

    private void hideWalletsFragment(FragmentTransaction transaction){
        Fragment walletsFragment = activity.getSupportFragmentManager().findFragmentByTag(WALLETS_FRAGMENT_TAG);
        if (walletsFragment!=null)
            transaction.hide(walletsFragment);
    }

    private void hideRateFragment(FragmentTransaction transaction){
        Fragment rateFragment = activity.getSupportFragmentManager().findFragmentByTag(RATE_FRAGMENT_TAG);
        if (rateFragment!=null)
            transaction.hide(rateFragment);
    }

    private void hideConverterFragment(FragmentTransaction transaction){
        Fragment converterFragment = activity.getSupportFragmentManager().findFragmentByTag(CONVERTER_FRAGMENT_TAG);
        if (converterFragment!=null)
            transaction.hide(converterFragment);
    }
}
