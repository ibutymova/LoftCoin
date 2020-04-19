package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.data.api.Api;
import com.example.butymovaloftcoin.data.api.SignInterceptor;
import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.model.CoinEntityMapper;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.di.scopes.AppScope;
import com.example.butymovaloftcoin.interactors.ConverterInteractor;
import com.example.butymovaloftcoin.interactors.RateDatabaseInteractor;
import com.example.butymovaloftcoin.interactors.RateApiInteractor;
import com.example.butymovaloftcoin.interactors.SyncRateJobInteractor;
import com.example.butymovaloftcoin.interactors.WalletsInteractor;
import com.example.butymovaloftcoin.screens.main.wallets.utils.WalletsPosition;
import com.example.butymovaloftcoin.utils.CurrencyFormatter;

import java.text.SimpleDateFormat;
import java.util.Random;

import dagger.BindsInstance;
import dagger.Component;
import io.reactivex.disposables.CompositeDisposable;

@AppScope
@Component(modules = {AppModule.class, InteractorModule.class})
public abstract class AppComponent {

    private static volatile AppComponent component;

    public static AppComponent createComponent(App app){
        if (component==null)
            component = DaggerAppComponent
                    .builder()
                    .withApp(app)
                    .build();
        return component;
    }

    public static void clearComponent(){
        component = null;
    }

    public abstract Prefs getPrefs();
    public abstract Api getApi();
    public abstract Database getDatabase();
    public abstract CoinEntityMapper getCoinEntityMapper();
    public abstract App getApp();
    public abstract int[] getColors();
    public abstract Random getRandom();
    public abstract CompositeDisposable getCompositeDisposable();
    public abstract CurrencyFormatter getCurrencyFormatter();
    public abstract SimpleDateFormat getSimpleDateFormat();
    public abstract WalletsPosition getWalletsPosition();

    public abstract RateApiInteractor getRateInteractor();
    public abstract RateDatabaseInteractor getRateInnerInteractor();
    public abstract ConverterInteractor getConverterInteractor();
    public abstract WalletsInteractor getWalletsInteractor();
    public abstract SyncRateJobInteractor getSyncRateJobInteractor();
    public abstract SignInterceptor getSignInterceptor();

    @Component.Builder
    interface Builder{
        AppComponent build();
        @BindsInstance
        Builder withApp(App app);
    }
}
