package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.screens.main.rate.RateRouter;
import com.example.butymovaloftcoin.screens.main.rate.RateView;
import com.example.butymovaloftcoin.screens.main.rate.adapters.RateAdapter;
import com.example.butymovaloftcoin.screens.main.rate.RatePresenter;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {RateModule.class})
public abstract class RateComponent {
    private static volatile RateComponent component;

    public static RateComponent createComponent(AppComponent appComponent){
        if (component==null){
            component = DaggerRateComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
        }
        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract RatePresenter getRatePresenter();
    public abstract RateRouter getRateRouter();
    public abstract RateView getRateView();


    @Component.Builder
    interface Builder{
        RateComponent build();
        Builder withAppComponent(AppComponent appComponent);
    }
}
