package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesAdapter;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheetPresenter;
import com.example.butymovaloftcoin.screens.currencies.CurrenciesBottomSheetView;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {CurrenciesModule.class})
public abstract class CurrenciesComponent {
    private static volatile CurrenciesComponent component;

    public static CurrenciesComponent createComponent(AppComponent appComponent){
        if (component==null){
            component = DaggerCurrenciesComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
        }
        return component;
    }

    public static void clearComponent(){
        component = null;
    }

    public abstract CurrenciesAdapter getCurrenciesAdapter();

    public abstract CurrenciesBottomSheetPresenter getCurrenciesBottomSheetPresenter();
    public abstract CurrenciesBottomSheetView getCurrenciesBottomSheetView();

    @Component.Builder
    interface Builder{
        CurrenciesComponent build();
        CurrenciesComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
