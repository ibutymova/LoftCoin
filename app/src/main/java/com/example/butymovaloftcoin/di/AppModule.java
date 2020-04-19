package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.App;
import com.example.butymovaloftcoin.data.api.Api;
import com.example.butymovaloftcoin.data.api.ApiInitializer;
import com.example.butymovaloftcoin.data.api.SignInterceptor;
import com.example.butymovaloftcoin.data.db.Database;
import com.example.butymovaloftcoin.data.db.DatabaseInitializer;
import com.example.butymovaloftcoin.data.db.model.CoinEntityMapper;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.data.prefs.PrefsImpl;
import com.example.butymovaloftcoin.di.scopes.AppScope;
import com.example.butymovaloftcoin.screens.main.wallets.utils.WalletsPosition;
import com.example.butymovaloftcoin.utils.CurrencyFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public interface AppModule {

    @Provides
    @AppScope
    static Prefs providePrefs(App app){
        return new PrefsImpl(app);
    }

    @Provides
    @AppScope
    static ApiInitializer provideApiInitializer(SignInterceptor signInterceptor){
        return new ApiInitializer(signInterceptor);
    }

    @Provides
    @AppScope
    static Api provideApi(ApiInitializer apiInitializer){
        return apiInitializer.init();
    }

    @Provides
    @AppScope
    static Database provideDatabase(App app){
        return new DatabaseInitializer().init(app);
    }

    @Provides
    @AppScope
    static CoinEntityMapper provideCoinEntityMapper(){
        return new CoinEntityMapper();
    }

    @Provides
    @AppScope
    static int[] provideColors(){
        return new int[] {
                0xFFF5FF30,
                0xFFFFFFFF,
                0xFF2ABDF5,
                0xFFFF7416,
                0xFFFF74FF,
                0xFF534FFF
                };
    }

    @Provides
    @AppScope
    static Random provideRandom(){
        return new Random();
    }

    @Provides
    static CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }

    @Provides
    @AppScope
    static CurrencyFormatter provideCurrencyFormatter(){
        return new CurrencyFormatter();
    }

    @Provides
    @AppScope
    static SimpleDateFormat provideSimpleDateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
    }

    @AppScope
    @Provides
    static WalletsPosition provideWalletsPosition(){
        return new WalletsPosition(0);
    }

    @AppScope
    @Provides
    static SignInterceptor provideSignInterceptor(){
        return new SignInterceptor();
    }
}
