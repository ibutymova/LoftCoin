package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.FragmentScope;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsPresenter;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsRouter;
import com.example.butymovaloftcoin.screens.main.wallets.WalletsView;
import com.example.butymovaloftcoin.screens.main.wallets.adapters.TransactionsAdapter;
import com.example.butymovaloftcoin.screens.main.wallets.adapters.WalletsPageAdapter;
import com.example.butymovaloftcoin.screens.main.wallets.utils.WalletsPosition;

import dagger.Component;

@Component(dependencies = {AppComponent.class}, modules = {WalletsModule.class})
@FragmentScope
public abstract class WalletsComponent {

    private static volatile WalletsComponent component;

    public static WalletsComponent createComponent(AppComponent appComponent){
        if (component==null){
            component = DaggerWalletsComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
        }
        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract WalletsPresenter getWalletsPresenter();
    public abstract WalletsRouter getWalletsRouter();
    public abstract WalletsView getWalletsView();

    @Component.Builder
    interface Builder{
        WalletsComponent build();
        WalletsComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
