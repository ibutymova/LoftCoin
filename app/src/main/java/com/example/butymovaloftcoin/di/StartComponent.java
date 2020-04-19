package com.example.butymovaloftcoin.di;

import com.example.butymovaloftcoin.di.scopes.ActivityScope;
import com.example.butymovaloftcoin.screens.start.StartPresenter;
import com.example.butymovaloftcoin.screens.start.StartRouter;
import com.example.butymovaloftcoin.screens.start.StartView;

import dagger.Component;

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {StartModule.class})
public abstract class StartComponent {

    private static volatile StartComponent component;

    public static StartComponent createComponent(AppComponent appComponent){
        if (component==null){
            component = DaggerStartComponent
                    .builder()
                    .withAppComponent(appComponent)
                    .build();
            }
        return component;
    }

    public static void clearComponent(){
        component=null;
    }

    public abstract StartPresenter getStartPresenter();
    public abstract StartRouter getStartRouter();
    public abstract StartView getStartView();

    @Component.Builder
    interface Builder{
        StartComponent build();
        StartComponent.Builder withAppComponent(AppComponent appComponent);
    }
}
